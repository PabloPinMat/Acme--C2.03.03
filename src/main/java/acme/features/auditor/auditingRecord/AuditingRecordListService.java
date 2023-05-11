
package acme.features.auditor.auditingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecord;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditingRecordListService extends AbstractService<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordRepository repository;

	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("masterId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Audit object;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findAuditById(masterId);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getAuditor().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		Collection<AuditingRecord> objects;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findAuditingRecordsByAuditId(masterId);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "subject", "assessment", "startPeriod", "endPeriod","mark","link");
		tuple.put("published", object.getAudit().isPublished());
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<AuditingRecord> object) {
		assert object != null;
		boolean createButton = false;
		final int masterId = super.getRequest().getData("masterId", int.class);
		final Audit audit = this.repository.findAuditById(masterId);
		if (super.getRequest().getPrincipal().getAccountId() == audit.getAuditor().getUserAccount().getId())
			createButton = true;
		super.getResponse().setGlobal("createButton", createButton);
		super.getResponse().setGlobal("masterId", masterId);
	}
}
