
package acme.features.authenticated.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedShowAuditService extends AbstractService<Authenticated, Audit> {

	@Autowired
	protected AuthenticatedAuditRepository repository;

	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Audit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;
		tuple = super.unbind(object, "code", "conclusion","strongPoints","weakPoints");
		tuple.put("auditor", object.getAuditor().getUserAccount().getUsername());
		super.getResponse().setData(tuple);
	}
}
