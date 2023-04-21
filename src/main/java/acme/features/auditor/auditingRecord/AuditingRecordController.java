
package acme.features.auditor.auditingRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.audit.AuditingRecord;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditingRecordController extends AbstractController<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordCreateService	createService;
	
	@Autowired
	protected AuditingRecordDeleteService	deleteService;
	
	@Autowired
	protected AuditingRecordUpdateService	updateService;
	
	@Autowired
	protected AuditingRecordListService		listAllService;

	@Autowired
	protected AuditingRecordShowService		showService;



	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("list", this.listAllService);
		super.addBasicCommand("show", this.showService);
	}

}
