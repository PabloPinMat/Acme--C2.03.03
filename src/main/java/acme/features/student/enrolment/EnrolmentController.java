
package acme.features.student.enrolment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.enrolments.Enrolment;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class EnrolmentController extends AbstractController<Student, Enrolment> {

	@Autowired
	protected EnrolmentListService listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
