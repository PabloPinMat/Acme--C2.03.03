
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
	protected EnrolmentCreateService		createService;

	@Autowired
	protected EnrolmentShowService			showService;

	@Autowired
	protected EnrolmentUpdateService		updateService;

	@Autowired
	protected EnrolmentDeleteService		deleteService;

	@Autowired
	protected EnrolmentFinaliseService		finaliseService;

	@Autowired
	protected EnrolmentMyEnrolmentService	myEnrolmentService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		//Comandos custom

		super.addCustomCommand("list-mine", "list", this.myEnrolmentService);
		super.addCustomCommand("finalise", "update", this.finaliseService);
	}
}
