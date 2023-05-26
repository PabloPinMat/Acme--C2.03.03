
package acme.features.student.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activity.Activity;
import acme.entities.activity.ActivityType;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Enrolment enrolment;

		masterId = super.getRequest().getData("masterId", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);
		status = enrolment != null && enrolment.getFinalised() && super.getRequest().getPrincipal().hasRole(enrolment.getStudent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Activity object;
		int masterId;
		Enrolment enrolment;

		masterId = super.getRequest().getData("masterId", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);

		object = new Activity();
		object.setEnrolment(enrolment);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, "title", "summary", "activityType", "startDate", "endDate", "link");
	}

	@Override
	public void validate(final Activity object) {
		if (object.getStartDate() != null && object.getEndDate() != null) {
			if (!super.getBuffer().getErrors().hasErrors("startDate"))
				super.state(MomentHelper.isFuture(object.getStartDate()), "startDate", "student.activity.form.error.wrong-start");
			if (!super.getBuffer().getErrors().hasErrors("endDate"))
				super.state(MomentHelper.isFuture(object.getEndDate()), "endDate", "student.activity.form.error.wrong-end");
			if (!super.getBuffer().getErrors().hasErrors("endDate"))
				super.state(MomentHelper.isBefore(object.getStartDate(), object.getEndDate()), "endDate", "student.activity.form.error.wrong-dates");
		} else
			super.state(false, "*", "student.activity.form.error.dateNull");
	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(ActivityType.class, object.getActivityType());

		tuple = super.unbind(object, "title", "summary", "activityType", "startDate", "endDate", "link");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("finalised", object.getEnrolment().getFinalised());
		tuple.put("types", choices);

		super.getResponse().setData(tuple);
	}

}
