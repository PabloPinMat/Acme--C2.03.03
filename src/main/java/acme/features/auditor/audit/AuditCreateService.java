
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditCreateService extends AbstractService<Auditor, Audit> {

	@Autowired
	protected AuditRepository	repository;

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	
	@Override
	public void validate(final Audit object) {
		assert object != null;
		
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.repository.findAuditByCode(object.getCode()) == null, "code", "auditor.audit.form.error.duplicateCode");

	}

	@Override
	public void load() {
		Audit object;
		object = new Audit();
		final Auditor auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setAuditor(auditor);
		object.setPublished(false);
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
		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "published");
		tuple.put("courses", choices);
		tuple.put("course", choices.getSelected().getKey());
		super.getResponse().setData(tuple);
	}
}
