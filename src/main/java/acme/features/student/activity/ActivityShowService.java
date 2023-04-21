
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enrolments.Activity;
import acme.entities.lecture.LectureType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class ActivityShowService extends AbstractService<Student, Activity> {

	@Autowired
	protected ActivityRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Activity activity;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		activity = this.repository.findActivityById(masterId);
		student = activity == null ? null : activity.getEnrolment().getStudent();
		status = super.getRequest().getPrincipal().hasRole(student);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findActivityById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(LectureType.class, object.getActivityType());

		tuple = super.unbind(object, "title", "summary", "type", "startDate", "endDate", "moreInfo");
		tuple.put("enrolmentId", object.getEnrolment().getId());
		tuple.put("draftMode", object.getEnrolment().getFinalised());
		tuple.put("types", choices);

		super.getResponse().setData(tuple);
	}

}
