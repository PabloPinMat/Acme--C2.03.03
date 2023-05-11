
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
public class AssistantTutorialSessionDeleteService extends AbstractService<Assistant, TutorialSession> {

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
		Boolean estado;
		Integer sessionId;
		Tutorial tutorial;

		sessionId = super.getRequest().getData("id", int.class);
		tutorial = this.repositorio.findTutorialByTutorialSessionId(sessionId);
		estado = tutorial != null && !tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(tutorial.getAssitant());
		super.getResponse().setAuthorised(estado);
	}

	@Override
	public void load() {
		TutorialSession tutorialSession;
		Integer sessionId;

		sessionId = super.getRequest().getData("id", int.class);
		tutorialSession = this.repositorio.findTutorialSessionById(sessionId);
		super.getBuffer().setData(tutorialSession);
	}

	@Override
	public void bind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		super.bind(tutorialSession, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");

	}

	@Override
	public void validate(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
	}

	@Override
	public void perform(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		this.repositorio.delete(tutorialSession);

	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		final Tuple tupla;
		SelectChoices opciones;
		Tutorial tutorial;

		opciones = SelectChoices.from(sessionType.class, tutorialSession.getSessionType());
		tutorial = tutorialSession.getTutorial();

		tupla = super.unbind(tutorialSession, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
		tupla.put("draftMode", tutorial.isDraftMode());
		tupla.put("sessionType", opciones);
		tupla.put("tutorialId", tutorial.getId());
		super.getResponse().setData(tupla);
	}

}
