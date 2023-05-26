
package acme.features.student.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;
import acme.roles.Student;

@Service
public class StudentLecturerShowService extends AbstractService<Student, Lecturer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentLecturerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Lecturer object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLecturerByCourseId(id);

		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final Lecturer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "almaMater", "qualificationsList", "link", "resume");

		super.getResponse().setData(tuple);
	}

}
