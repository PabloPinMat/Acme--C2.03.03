
package acme.features.assistant.tutorialSession;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.TutorialSession;
import acme.entities.sessions.sessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionUpdateService extends AbstractService<Assistant, TutorialSession> {

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
		Integer tutorialSessionId;

		tutorialSessionId = super.getRequest().getData("id", int.class);
		tutorialSession = this.repositorio.findTutorialSessionById(tutorialSessionId);
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

		if (!super.getBuffer().getErrors().hasErrors("finishDate"))
			super.state(MomentHelper.isBefore(tutorialSession.getStartSession(), tutorialSession.getFinishSession()), "finishDate", "La fecha final no puede ser anterior a la inicial");

	}

	@Override
	public void perform(final TutorialSession tutorialSession) {
		assert tutorialSession != null;

		Tutorial tutorial;
		Double horas;
		Double timeSession;
		Double timeSessionFormat;
		Duration duration;
		String timeSessionString;

		tutorial = tutorialSession.getTutorial();
		duration = MomentHelper.computeDuration(tutorialSession.getStartSession(), tutorialSession.getFinishSession());
		timeSession = duration.getSeconds() / 3600.0;
		timeSessionString = String.format("%.2f", timeSession);
		timeSessionFormat = Double.parseDouble(timeSessionString);
		horas = timeSessionFormat + tutorial.getEstimatedTime();
		tutorial.setEstimatedTime(horas);
		this.repositorio.save(tutorial);
		this.repositorio.save(tutorialSession);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tuple tupla;
		final SelectChoices opciones;
		Tutorial tutorial;

		opciones = SelectChoices.from(sessionType.class, tutorialSession.getSessionType());
		tutorial = tutorialSession.getTutorial();
		tupla = super.unbind(tutorialSession, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
		tupla.put("draftMode", tutorial.isDraftMode());
		tupla.put("tutorialId", tutorial.getId());
		tupla.put("sessionType", opciones);
		super.getResponse().setData(tupla);
	}

}
