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
public class AuditingRecordPublishService extends AbstractService<Auditor, AuditingRecord> {

	@Autowired
	protected AuditingRecordRepository repository;


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		boolean status;
		final AuditingRecord ar = this.repository.findAuditingRecordById(super.getRequest().getData("id", int.class));
		status = super.getRequest().getPrincipal().hasRole(ar.getAudit().getAuditor()) && !ar.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditingRecord ar;
		ar = this.repository.findAuditingRecordById(super.getRequest().getData("id", int.class));
		super.getBuffer().setData(ar);
	}

	@Override
	public void bind(final AuditingRecord ar) {
		assert ar != null;

		final Mark mark = super.getRequest().getData("mark", Mark.class);
		super.bind(ar, "subject", "assesment", "startPeriod", "endPeriod", "link");
		ar.setMark(mark);
	}

	@Override
	public void validate(final AuditingRecord object) {
		assert object != null;
	}

	@Override
	public void perform(final AuditingRecord ar) {
		assert ar != null;
		ar.setPublished(true);
		this.repository.save(ar);
	}

	@Override
	public void unbind(final AuditingRecord object) {
		assert object != null;
		final Tuple tuple;
		final SelectChoices elecs = SelectChoices.from(Mark.class, object.getMark());
		tuple = super.unbind(object, "subject", "assessment", "startPeriod", "endPeriod", "mark", "link","published");
		tuple.put("elecs", elecs);

		super.getResponse().setData(tuple);
	}
}