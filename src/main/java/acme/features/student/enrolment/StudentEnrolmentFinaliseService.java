
package acme.features.student.enrolment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.enrolments.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentFinaliseService extends AbstractService<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

	// AbstractService interface ----------------------------------------------


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
		Enrolment enrolment;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);
		student = enrolment == null ? null : enrolment.getStudent();
		status = enrolment != null && !enrolment.getFinalised() && super.getRequest().getPrincipal().hasRole(student);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		final int LOWER_NIBBLE_START = 12;

		int courseId;
		Course course;
		String creditCardNumber;
		String creditCardLowerNibble;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		creditCardNumber = super.getRequest().getData("ccNumber", String.class);

		super.bind(object, "code", "motivation", "goals", "ccHolder");
		object.setCourse(course);
		if (creditCardNumber.length() == 16) {
			creditCardLowerNibble = creditCardNumber.substring(LOWER_NIBBLE_START);
			object.setCcLowerNibble(creditCardLowerNibble);
		}
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		final Date expiryDate;

		if (!super.getBuffer().getErrors().hasErrors("ccHolder")) {
			String creditCardHolder;

			creditCardHolder = super.getRequest().getData("ccHolder", String.class);
			super.state(!creditCardHolder.equals(""), "ccHolder", "Cannot be null");
		}

		if (!super.getBuffer().getErrors().hasErrors("ccNumber")) {
			String creditCardNumber;

			creditCardNumber = super.getRequest().getData("ccNumber", String.class);
			super.state(creditCardNumber.matches("\\d{16}"), "ccNumber", "Wrong credic card number");
		}

		if (!super.getBuffer().getErrors().hasErrors("expiryDate")) {
			String expiryDateString;

			expiryDateString = super.getRequest().getData("expiryDate", String.class);

			super.state(expiryDateString != null, "expiryDate", "student.enrolment.form.error.null-expiryDate");

			if (expiryDateString != null)
				if (!expiryDateString.matches("^\\d{4}([\\/])(0?[1-9]|1[1-2])\\1(3[01]|[12][0-9]|0?[1-9]) ([01]?[0-9]|2[0-3]):[0-5][0-9]$"))
					super.state(false, "expiryDate", "student.enrolment.form.error.wrongFormat");
				else {
					expiryDate = super.getRequest().getData("expiryDate", Date.class);
					super.state(MomentHelper.isFuture(expiryDate), "expiryDate", "student.enrolment.form.error.cardExpired");
				}
		}

		if (!super.getBuffer().getErrors().hasErrors("cvc")) {
			String cvc;

			cvc = super.getRequest().getData("cvc", String.class);
			super.state(cvc.matches("\\d{3}"), "cvc", "Wrong CVC");
		}
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		object.setFinalised(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;

		final int LOWER_NIBBLE_START = 12;

		Collection<Course> courses;
		SelectChoices choices;
		String creditCardNumber;
		String creditCardLowerNibble;
		Tuple tuple;

		courses = this.repository.findPublishedCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		creditCardNumber = super.getRequest().getData("ccNumber", String.class);

		tuple = super.unbind(object, "code", "motivation", "goals", "finalised", "ccHolder");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		if (creditCardNumber.length() == 16) {
			creditCardLowerNibble = creditCardNumber.substring(LOWER_NIBBLE_START);
			tuple.put("ccLowerNibble", creditCardLowerNibble);
		}

		super.getResponse().setData(tuple);
	}

}
