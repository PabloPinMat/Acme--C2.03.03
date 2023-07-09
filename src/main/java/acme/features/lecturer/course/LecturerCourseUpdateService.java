
package acme.features.lecturer.course;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.entities.lecture.Lecture;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseUpdateService extends AbstractService<Lecturer, Course> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService<Employer, Job> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Course object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;
		super.bind(object, "code", "title", "courseAbstract", "retailPrice", "link");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
		SystemConfiguration conf;
		conf = this.repository.findSystemConfiguration();

		if (!super.getBuffer().getErrors().hasErrors("draftMode")) {

			final boolean draftMode = object.isDraftMode();
			super.state(draftMode, "draftMode", "lecturer.course.error.draftMode.published");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {

			final String code = object.getCode();
			final Course course = this.repository.findCourseByCode(code);
			final boolean boo = course == null || object.getId() == course.getId();
			super.state(boo, "code", "lecturer.course.error.code.duplicated");

		}

		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			final double retailPrice = object.getRetailPrice().getAmount();
			super.state(retailPrice >= 0, "retailPrice", "lecturer.course.error.retailPrice.negative");
		}

		if (object.getRetailPrice() != null) {
			if (!conf.getAcceptedCurrencies().contains(object.getRetailPrice().getCurrency()) || conf.getAcceptedCurrencies().contains(object.getRetailPrice().getCurrency()) && object.getRetailPrice().getCurrency().length() < 3)
				super.state(false, "*", "Wrong price format");
		} else
			super.state(false, "*", "Price must not be null");
	}

	@Override
	public void perform(final Course object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "code", "title", "courseAbstract", "retailPrice", "link", "draftMode", "lecturer");
		final List<Lecture> lectures = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());
		final CourseType courseType = object.calculateCourseType(lectures);
		tuple.put("courseType", courseType);
		super.getResponse().setData(tuple);
	}
}
