
package acme.features.lecturer.course;

import java.util.Collection;
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
public class LecturerCoursePublishService extends AbstractService<Lecturer, Course> {

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

		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			final double retailPrice = object.getRetailPrice().getAmount();
			super.state(retailPrice >= 0, "retailPrice", "lecturer.course.error.retailPrice.negative");
		}

		if (object.getRetailPrice() != null) {
			if (!conf.getAcceptedCurrencies().contains(object.getRetailPrice().getCurrency()))
				super.state(false, "*", "Wrong price format");
		} else
			super.state(false, "*", "Price must not be null");

		final Collection<Lecture> lectures = this.repository.findLecturesByCourse(object.getId());
		super.state(!lectures.isEmpty(), "*", "lecturer.course.form.error.noLectures");

		boolean publishedLectures;
		publishedLectures = lectures.stream().allMatch(x -> x.isDraftMode() == false);
		super.state(publishedLectures, "*", "lecturer.course.form.error.lectureNotPublished");

		final String code = object.getCode();
		final Course course = this.repository.findCourseByCode(code);
		final boolean boo = course == null || object.getId() == course.getId();
		super.state(boo, "code", "lecturer.course.error.code.duplicated");

		final List<Lecture> lecturesInCourse = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());
		final CourseType courseType = object.calculateCourseType(lecturesInCourse);
		super.state(courseType != CourseType.THEORY_COURSE, "*", "you cant publish a theoretical course");
	}

	@Override
	public void perform(final Course object) {

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "code", "title", "courseAbstract", "retailPrice", "link", "draftMode");
		final List<Lecture> lectures = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());
		final CourseType courseType = object.calculateCourseType(lectures);
		tuple.put("courseType", courseType);
		super.getResponse().setData(tuple);
	}
}
