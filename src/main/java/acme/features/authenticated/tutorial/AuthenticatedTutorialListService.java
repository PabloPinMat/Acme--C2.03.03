
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialListService extends AbstractService<Authenticated, Tutorial> {

	@Autowired
	protected AuthenticatedTutorialRepository repositorio;


	@Override
	public void check() {
		boolean estado;
		estado = super.getRequest().hasData("courseId", int.class);
		super.getResponse().setChecked(estado);
	}

	@Override
	public void authorise() {
		Boolean estado;
		estado = true;
		super.getResponse().setAuthorised(estado);
	}

	@Override
	public void load() {
		Collection<Tutorial> tutoriales;
		Integer courseId;

		courseId = super.getRequest().getData("courseId", int.class);
		tutoriales = this.repositorio.findTutorialByCourseId(courseId);
		super.getBuffer().setData(tutoriales);
	}

	@Override
	public void unbind(final Tutorial tutoriales) {
		assert tutoriales != null;
		Tuple tupla;

		tupla = super.unbind(tutoriales, "title", "abstract$", "goals");
		super.getResponse().setData(tupla);
	}

}
