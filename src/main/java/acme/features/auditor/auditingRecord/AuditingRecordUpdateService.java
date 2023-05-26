
package acme.features.auditor.auditingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.AuditingRecord;
import acme.entities.audit.Mark;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditingRecordUpdateService extends AbstractService<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordRepository	repository;


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		final AuditingRecord audintingRecord = this.repository.findAuditingRecordById(super.getRequest().getData("id", int.class));
		status = super.getRequest().getPrincipal().hasRole(audintingRecord.getAudit().getAuditor()) && !audintingRecord.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecord object;

		object = this.repository.findAuditingRecordById(super.getRequest().getData("id", int.class));
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditingRecord object) {
		assert object != null;

		final Mark mark = super.getRequest().getData("mark", Mark.class);
		super.bind(object, "subject", "assessment", "startPeriod", "endPeriod", "link");
		object.setMark(mark);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("startPeriod") && !super.getBuffer().getErrors().hasErrors("endPeriod"))
			if (!MomentHelper.isBefore(object.getStartPeriod(), object.getEndPeriod()))
				super.state(false, "startPeriod", "auditor.auditingRecord.error.date.StartFinError");
			else
				super.state(!(object.periodDuration()<= 1), "periodStart", "auditor.auditingRecord.error.date.invalidPeriod");
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
		tuple = super.unbind(object, "subject", "assessment", "startPeriod", "endPeriod", "mark", "link");
		tuple.put("elecs", elecs);

		super.getResponse().setData(tuple);
	}
}
