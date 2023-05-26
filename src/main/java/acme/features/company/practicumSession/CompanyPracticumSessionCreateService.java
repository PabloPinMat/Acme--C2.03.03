
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.session.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	@Autowired
	protected CompanyPracticumSessionRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession object;
		int practicumId;
		Practicum practicum;
		boolean publish;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		publish = practicum.isPublish();

		object = new PracticumSession();
		object.setPracticum(practicum);

		if (publish)
			object.setConfirmationSession(true);
		else
			object.setConfirmationSession(false);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;

		super.bind(object, "title", "abstractSession", "startTimePeriod", "endTimePeriod", "furtherInformationLink");
	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;

		boolean confirmation;

		if (!object.getPracticum().isPublish())
			confirmation = true;
		else {
			confirmation = super.getRequest().getData("confirmation", boolean.class);
			if (confirmation) {
				final Collection<PracticumSession> res = this.repository.findPracticumSessionsByPracticumId(object.getPracticum().getId());
				for (final PracticumSession ps : res)
					if (ps.isConfirmationSession())
						confirmation = false;
			} else
				confirmation = false;
		}
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");

		if (!super.getBuffer().getErrors().hasErrors("startTimePeriod")) {
			Date minimumStartDate;
			minimumStartDate = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getStartTimePeriod(), minimumStartDate), "startTimePeriod", "company.practicum-session.form.error.start-time-period");

			if (!super.getBuffer().getErrors().hasErrors("endTimePeriod")) {
				Date minimumEndDate;
				minimumEndDate = MomentHelper.deltaFromMoment(object.getStartTimePeriod(), 7, ChronoUnit.DAYS);
				super.state(MomentHelper.isAfterOrEqual(object.getEndTimePeriod(), minimumEndDate), "endTimePeriod", "company.practicum-session.form.error.end-time-period");
			}
		}

	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractSession", "startTimePeriod", "endTimePeriod", "furtherInformationLink");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("publish", object.getPracticum().isPublish());
		tuple.put("confirmation", false);

		super.getResponse().setData(tuple);
	}
}
