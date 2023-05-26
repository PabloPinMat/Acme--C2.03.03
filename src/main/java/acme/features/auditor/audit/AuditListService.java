
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.audit.Audit;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditListService extends AbstractService<Auditor, Audit> {

	@Autowired
	protected AuditRepository repository;
	
	@Autowired
	protected SystemConfigurationService configuration;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	

	@Override
	public void load() {
		Collection<Audit> objects;
		final Principal principal = super.getRequest().getPrincipal();
		final int id = principal.getActiveRoleId();
		objects = this.repository.findAuditsByAuditorId(id);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;
		final String lang = super.getRequest().getLocale().getLanguage();
		tuple = super.unbind(object,  "code", "strongPoints","weakPoints", "conclusion");
		tuple.put("published", this.configuration.booleanTranslated(object.isPublished(),lang));
		tuple.put("courseTitle", object.getCourse().getTitle());
		super.getResponse().setData(tuple);
	}
}
