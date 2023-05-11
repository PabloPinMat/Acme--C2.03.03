
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.AuditingRecord;
import acme.entities.audit.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditingRecordShowService extends AbstractService<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordRepository repository;

	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		AuditingRecord ar;
		ar = this.repository.findAuditingRecordById(super.getRequest().getData("id", int.class));
		status = ar != null && super.getRequest().getPrincipal().hasRole(ar.getAudit().getAuditor());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecord object;
		int auditingRecordId;
		auditingRecordId = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditingRecordById(auditingRecordId);
		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		Tuple tuple;
		final SelectChoices choices = SelectChoices.from(Mark.class, object.getMark());
		tuple = super.unbind(object,"subject", "assessment", "startPeriod", "endPeriod", "link");
		tuple.put("elecs", choices);
		tuple.put("published", object.isPublished());
		tuple.put("correction", object.isCorrection());
		tuple.put("hours", object.periodDuration());

		super.getResponse().setData(tuple);

	}
}
