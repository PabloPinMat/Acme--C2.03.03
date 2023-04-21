
package acme.features.auditor.audit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.audit.Audit;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditController extends AbstractController<Auditor, Audit> {

	@Autowired
	protected AuditListService		listAllService;

	@Autowired
	protected AuditShowService		showService;

	@Autowired
	protected AuditCreateService		createService;

	@Autowired
	protected AuditDeleteService		deleteService;

	@Autowired
	protected AuditUpdateService		updateService;

	@Autowired
	protected AuditPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listAllService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
