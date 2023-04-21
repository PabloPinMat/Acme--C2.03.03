
package acme.features.company.practicumSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.session.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionListService extends AbstractService<Company, PracticumSession> {

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
		int masterId;
		Practicum practicum;

		masterId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(masterId);
		status = practicum != null && super.getRequest().getPrincipal().hasRole(practicum.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<PracticumSession> objects;
		int practicumId;

		practicumId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findPracticumSessionsByPracticumId(practicumId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "startTimePeriod", "endTimePeriod");

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<PracticumSession> objects) {
		assert objects != null;

		int practicumId;
		Practicum practicum;
		final boolean showCreate;
		final boolean exceptionalCreate;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findPracticumById(practicumId);
		showCreate = super.getRequest().getPrincipal().hasRole(practicum.getCompany());
		exceptionalCreate = practicum.isPublish();

		super.getResponse().setGlobal("masterId", practicumId);
		super.getResponse().setGlobal("showCreate", showCreate);
		super.getResponse().setGlobal("exceptionalCreate", exceptionalCreate);
	}
}
