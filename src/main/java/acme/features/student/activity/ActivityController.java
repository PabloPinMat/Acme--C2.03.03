
package acme.features.student.activity;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolments.Activity;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

public class ActivityController extends AbstractController<Student, Activity> {

	@Autowired
	protected ActivityListService	listService;

	@Autowired
	protected ActivityCreateService	createService;

	@Autowired
	protected ActivityShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}

}
