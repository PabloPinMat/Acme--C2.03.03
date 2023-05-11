
package acme.features.authenticated.assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AuthenticatedAssistantUpdateService extends AbstractService<Authenticated, Assistant> {

	@Autowired
	protected AuthenticatedAssistantRepository repositorio;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		Assistant assistant;
		Principal principal;
		Integer userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repositorio.findAssistantByUserAccountId(userAccountId);
		super.getBuffer().setData(assistant);
	}

	@Override
	public void bind(final Assistant assistant) {
		assert assistant != null;
		super.bind(assistant, "supervisor", "expertiseField", "resume", "furtherInformation");
	}

	@Override
	public void validate(final Assistant assistant) {
		assert assistant != null;
	}

	@Override
	public void perform(final Assistant assistant) {
		assert assistant != null;
		this.repositorio.save(assistant);
	}

	@Override
	public void unbind(final Assistant assistant) {
		assert assistant != null;
		Tuple tupla;

		tupla = BinderHelper.unbind(assistant, "supervisor", "expertiseField", "resume", "furtherInformation");
		super.getResponse().setData(tupla);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
