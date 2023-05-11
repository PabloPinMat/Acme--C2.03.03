
package acme.features.authenticated.tutorial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialShowService extends AbstractService<Authenticated, Tutorial> {

	@Autowired
	protected AuthenticatedTutorialRepository repositorio;


	@Override
	public void check() {
		boolean estado;
		estado = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(estado);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Tutorial tutorial;
		Integer id;

		id = super.getRequest().getData("id", int.class);
		tutorial = this.repositorio.findTutorialById(id);
		super.getBuffer().setData(tutorial);
	}

	@Override
	public void unbind(final Tutorial tutorial) {
		assert tutorial != null;
		Tuple tupla;

		tupla = super.unbind(tutorial, "code", "title", "course.title", "abstract$", "goals", "estimatedTime", "assitant.supervisor", "assitant.expertiseField", "assitant.resume", "assitant.furtherInformation");
		super.getResponse().setData(tupla);
	}

}
