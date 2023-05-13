
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditUpdateService extends AbstractService<Auditor, Audit> {

	@Autowired
	protected AuditRepository	repository;

	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Audit object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getAuditor().getUserAccount().getId() == userAccountId && !object.isPublished());
	}
	
	@Override
	public void validate(final Audit object) {
		assert object != null;

	}

	@Override
	public void load() {
		Audit object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;
		int courseId;
		Course course;
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);
		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints");
		object.setCourse(course);
	}

	@Override
	public void perform(final Audit object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;
		Tuple tuple;
		Collection<Course> courses;
		SelectChoices choices;
		courses = this.repository.findPublishedCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints","published");
		tuple.put("courses", choices);
		tuple.put("course", choices.getSelected().getKey());
		super.getResponse().setData(tuple);
	}
}
