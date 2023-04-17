
package acme.features.student.enrolment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enrolments.Enrolment;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class EnrolmentListService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected EnrolmentRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		List<Enrolment> enrolments;

		final Integer studentId = super.getRequest().getPrincipal().getActiveRoleId();

		enrolments = this.repository.findEnrolmentsByStudentId(studentId);

		super.getBuffer().setData(enrolments);
	}

	@Override
	public void unbind(final Enrolment enrolment) {
		assert enrolment != null;

		Tuple tuple;

		tuple = super.unbind(enrolment, "code", "motivation", "goals");

		super.getResponse().setData(tuple);
	}
}
