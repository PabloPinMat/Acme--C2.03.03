
package acme.features.assistant.tutorialSession;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
		final Boolean duracionMin;
		final Boolean duracionMax;
		Boolean fechaInicio;
		final Boolean fechaInicio2;
		final Date fechaActual2 = new Date(System.currentTimeMillis());

		if (tutorialSession.getStartSession() != null && tutorialSession.getFinishSession() != null) {

			if (!super.getBuffer().getErrors().hasErrors("startSession")) {
				fechaInicio = MomentHelper.isLongEnough(fechaActual2, tutorialSession.getStartSession(), 24, ChronoUnit.HOURS);
				super.state(fechaInicio, "startSession", "La fecha debe ser de un día despues de la fecha actual");
				fechaInicio2 = MomentHelper.isBefore(fechaActual2, tutorialSession.getStartSession());
				super.state(fechaInicio2, "startSession", "La fecha inicial no puede ser anterior a la actual");

			}

			if (!super.getBuffer().getErrors().hasErrors("finishSession"))
				super.state(MomentHelper.isBefore(tutorialSession.getStartSession(), tutorialSession.getFinishSession()), "finishSession", "La fecha final no puede ser anterior a la inicial");
			if (!super.getBuffer().getErrors().hasErrors("startSession") && !super.getBuffer().getErrors().hasErrors("finishDate"))

			{
				duracionMin = MomentHelper.isLongEnough(tutorialSession.getStartSession(), tutorialSession.getFinishSession(), 1, ChronoUnit.HOURS);
				super.state(duracionMin, "startSession", "Error en la duración minima de la sesión");
				duracionMax = MomentHelper.computeDuration(tutorialSession.getStartSession(), tutorialSession.getFinishSession()).getSeconds() <= Duration.ofHours(5).getSeconds();
				super.state(duracionMax, "finishSession", "Error en la duración máxima de la sesión");
			}
		} else
			super.state(false, "*", "Las fechas estan mal escritas");

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
