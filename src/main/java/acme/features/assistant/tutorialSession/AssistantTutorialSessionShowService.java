
package acme.features.assistant.tutorialSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.TutorialSession;
import acme.entities.sessions.sessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionShowService extends AbstractService<Assistant, TutorialSession> {

	@Autowired
	protected AssistantTutorialSessionRepository repositorio;


	@Override
	public void check() {
		Boolean estado;
		estado = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(estado);
	}

	@Override
	public void authorise() {
		final Boolean estado;
		Integer tutorialId;
		Tutorial tutorial;
		TutorialSession tutorialSession;
		Assistant assistant;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorialSession = this.repositorio.findTutorialSessionById(tutorialId);
		if (tutorialSession == null)
			tutorial = null;
		else
			tutorial = tutorialSession.getTutorial();
		if (tutorial == null)
			assistant = null;
		else
			assistant = tutorial.getAssitant();

		estado = tutorial != null && tutorialSession != null && super.getRequest().getPrincipal().hasRole(assistant) && tutorial.getAssitant().getId() == super.getRequest().getPrincipal().getActiveRoleId();
		super.getResponse().setAuthorised(estado);
	}

	@Override
	public void load() {
		TutorialSession tutorialSession;
		Integer tutorialSessionId;

		tutorialSessionId = super.getRequest().getData("id", int.class);
		tutorialSession = this.repositorio.findTutorialSessionById(tutorialSessionId);
		super.getBuffer().setData(tutorialSession);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;

		Tuple tupla;
		final SelectChoices opciones;
		Tutorial tutorial;
		Boolean draftMode;

		tutorial = tutorialSession.getTutorial();
		draftMode = tutorial.isDraftMode();
		opciones = SelectChoices.from(sessionType.class, tutorialSession.getSessionType());
		tupla = super.unbind(tutorialSession, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
		tupla.put("tutorialId", super.getRequest().getData("id", int.class));
		tupla.put("draftMode", draftMode);
		tupla.put("tutorial", tutorial);
		tupla.put("sessionType", opciones);
		super.getResponse().setData(tupla);
	}

}
