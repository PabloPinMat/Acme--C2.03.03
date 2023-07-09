
package acme.features.lecturer.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseCreateService extends AbstractService<Lecturer, Course> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {

		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Lecturer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		object = new Course();
		final Lecturer lecturer = this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setLecturer(lecturer);
		object.setDraftMode(true);
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
			final Course instance = this.repository.findCourseByCode(object.getCode());
			super.state(instance == null, "code", "lecturer.course.error.code.duplicated");
		}

		if (object.getRetailPrice() != null) {
			if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
				final double retailPrice = object.getRetailPrice().getAmount();
				super.state(retailPrice >= 0, "retailPrice", "Must be greater than 0");
			}
		} else
			super.state(false, "*", "Price must not be null");

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
		super.getResponse().setData(tuple);
	}
}
