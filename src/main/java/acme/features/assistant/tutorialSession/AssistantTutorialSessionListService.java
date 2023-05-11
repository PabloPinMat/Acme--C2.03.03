
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionListService extends AbstractService<Assistant, TutorialSession> {

	@Autowired
	protected AssistantTutorialSessionRepository repositorio;


	@Override
	public void check() {
		boolean estado;
		estado = super.getRequest().hasData("tutorialId", int.class);
		super.getResponse().setChecked(estado);
	}

	@Override
	public void authorise() {
		final boolean estado;
		Integer tutorialId;
		Tutorial tutorial;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		tutorialId = super.getRequest().getData("tutorialId", int.class);
		tutorial = this.repositorio.findTutorialById(tutorialId);
		estado = tutorial != null && (!tutorial.isDraftMode() || tutorial.getAssitant().getId() == principal.getActiveRoleId());
		super.getResponse().setAuthorised(estado);
	}

	@Override
	public void load() {
		final Collection<TutorialSession> sesiones;
		Integer tutorialId;

		tutorialId = super.getRequest().getData("tutorialId", int.class);
		sesiones = this.repositorio.findSessionsByTutorialId(tutorialId);
		super.getBuffer().setData(sesiones);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tuple tupla;

		tupla = super.unbind(tutorialSession, "title", "sessionType", "startSession", "finishSession");
		super.getResponse().setData(tupla);
	}

	@Override
	public void unbind(final Collection<TutorialSession> tutorialSessions) {
		assert tutorialSessions != null;
		final Integer tutorialId;
		Tutorial tutorial;
		final Boolean condiciones;

		tutorialId = super.getRequest().getData("tutorialId", int.class);
		tutorial = this.repositorio.findTutorialById(tutorialId);
		condiciones = tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(tutorial.getAssitant());
		super.getResponse().setGlobal("tutorialId", tutorialId);
		super.getResponse().setGlobal("condiciones", condiciones);
	}
}
