 
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecord;
import acme.entities.audit.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditingRecordCreateService extends AbstractService<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordRepository	repository;


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("auditId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		final Audit audit = this.repository.findAuditById(super.getRequest().getData("auditId", int.class));
		status = super.getRequest().getPrincipal().hasRole(audit.getAuditor()) && !audit.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecord object;

		object = new AuditingRecord();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;

		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);
		final String mark = super.getRequest().getData("mark", String.class);
		super.bind(object, "subject", "assessment", "startPeriod", "endPeriod", "link");
		object.setMark(Mark.transform(mark));
		object.setAudit(audit);
		object.setPublished(false);
		object.setCorrection(false);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("startPeriod") && !super.getBuffer().getErrors().hasErrors("endPeriod"))
			if (!MomentHelper.isBefore(object.getStartPeriod(), object.getEndPeriod()))
				super.state(false, "startPeriod", "auditor.auditingRecord.error.date.startFinError");
			else
				super.state(!(object.periodDuration() < 1), "startPeriod", "auditor.auditingRecord.error.date.invalidPeriod");

	}

	@Override
	public void perform(final AuditingRecord object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		final Tuple tuple;
		final SelectChoices elecs = SelectChoices.from(Mark.class, object.getMark());
		tuple = super.unbind(object,"subject", "assessment", "startPeriod", "endPeriod","mark", "link");
		tuple.put("elecs", elecs);
		tuple.put("auditId", super.getRequest().getData("auditId", int.class));

		super.getResponse().setData(tuple);
	}
}
