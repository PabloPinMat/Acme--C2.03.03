
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
public class AuditingRecordDeleteService extends AbstractService<Auditor, AuditingRecord> {

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
		AuditingRecord auditingRecord;
		auditingRecord = this.repository.findAuditingRecordById(super.getRequest().getData("id", int.class));
		status = auditingRecord != null && super.getRequest().getPrincipal().hasRole(auditingRecord.getAudit().getAuditor()) && auditingRecord.isPublished();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecord object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditingRecordById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;
		final String mark = super.getRequest().getData("mark", String.class);
		super.bind(object, "subject", "assessment", "startPeriod", "endPeriod", "link");
		object.setMark(Mark.transform(mark));
		object.setPublished(true);;
		object.setCorrection(false);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditingRecord object) {
		assert object != null;
		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		final Tuple tuple;
		final SelectChoices marks = SelectChoices.from(Mark.class, object.getMark());
		tuple = super.unbind(object, "subject", "assessment", "startPeriod", "endPeriod","mark", "link");
		tuple.put("elecs", marks);
		super.getResponse().setData(tuple);

	}
}
