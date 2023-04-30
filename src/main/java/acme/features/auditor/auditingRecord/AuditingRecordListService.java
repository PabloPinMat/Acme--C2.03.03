package acme.features.auditor.auditingRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecord;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditingRecordListService extends AbstractService<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordRepository repository;

	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("auditId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		final Audit audit = this.repository.findAuditById(super.getRequest().getData("auditId", int.class));
		status = super.getRequest().getPrincipal().hasRole(audit.getAuditor());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<AuditingRecord> objects;
		final int aid;
		aid = super.getRequest().getData("auditId", int.class);
		objects = this.repository.findAuditingRecordsByAuditId(aid);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "subject", "correction", "published");
		tuple.put("mark", object.getMark().toString());
		super.getResponse().setData(tuple);
	}
}
