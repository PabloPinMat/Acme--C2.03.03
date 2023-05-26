
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumUpdateService extends AbstractService<Company, Practicum> {

	@Autowired
	protected CompanyPracticumRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Practicum object;
		Principal principal;
		int practicumId;

		practicumId = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(practicumId);
		principal = super.getRequest().getPrincipal();

		status = object.getCompany().getId() == principal.getActiveRoleId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);

		super.bind(object, "code", "title", "abstractPracticum", "goals", "estimatedTotalTime");
		object.setCourse(course);

	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.repository.findPracticumByCode(object.getCode()) == null || this.repository.findPracticumByCode(object.getCode()).equals(object), "code", "company.practicum.form.error.code");

		if (!super.getBuffer().getErrors().hasErrors("course"))
			super.state(!object.getCourse().isDraftMode(), "course", "No puedes usar ese curso trápala");

	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "code", "title", "abstractPracticum", "goals", "estimatedTotalTime");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("publish", false);

		super.getResponse().setData(tuple);
	}
}
