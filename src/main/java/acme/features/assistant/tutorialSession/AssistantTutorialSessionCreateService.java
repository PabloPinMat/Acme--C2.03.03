
package acme.features.assistant.tutorialSession;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.sessions.TutorialSession;
import acme.entities.sessions.sessionType;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionCreateService extends AbstractService<Assistant, TutorialSession> {

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
		Boolean estado;
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
		final TutorialSession tutorialSession;
		Integer tutorialId;
		Tutorial tutorial;

		tutorialId = super.getRequest().getData("tutorialId", int.class);
		tutorial = this.repositorio.findTutorialById(tutorialId);
		tutorialSession = new TutorialSession();
		tutorialSession.setTutorial(tutorial);
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
		Boolean duracionMin;
		Boolean duracionMax;
		Boolean fechaInicio;
		final Boolean fechaInicio2;
		final Date fechaActual2 = new Date(System.currentTimeMillis());

		if (tutorialSession.getStartSession() != null && tutorialSession.getFinishSession() != null) {

			if (!super.getBuffer().getErrors().hasErrors("startSession")) {
				fechaInicio = MomentHelper.isLongEnough(fechaActual2, tutorialSession.getStartSession(), 24, ChronoUnit.HOURS);
				super.state(fechaInicio, "startSession", "assistant.tutorial.session.form.error.dateAfterCurrent");
				fechaInicio2 = MomentHelper.isBefore(fechaActual2, tutorialSession.getStartSession());
				super.state(fechaInicio2, "startSession", "assistant.tutorial.session.form.error.initialDateBeforeCurrent");

			}

			if (!super.getBuffer().getErrors().hasErrors("finishSession"))
				super.state(MomentHelper.isBefore(tutorialSession.getStartSession(), tutorialSession.getFinishSession()), "finishSession", "assistant.tutorial.session.form.error.endDateEarlierInitial");

			if (!super.getBuffer().getErrors().hasErrors("startSession") && !super.getBuffer().getErrors().hasErrors("finishDate")) {
				duracionMin = MomentHelper.isLongEnough(tutorialSession.getStartSession(), tutorialSession.getFinishSession(), 1, ChronoUnit.HOURS);
				super.state(duracionMin, "startSession", "assistant.tutorial.session.form.error.minimunDuration");
				duracionMax = MomentHelper.computeDuration(tutorialSession.getStartSession(), tutorialSession.getFinishSession()).getSeconds() <= Duration.ofHours(5).getSeconds();
				super.state(duracionMax, "finishSession", "assistant.tutorial.session.form.error.maximunDuration");
			}

		} else
			super.state(false, "*", "assistant.tutorial.session.form.error.datesMisspelled");
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
		boolean draftMode;

		tutorial = tutorialSession.getTutorial();
		draftMode = tutorial.isDraftMode();
		opciones = SelectChoices.from(sessionType.class, tutorialSession.getSessionType());
		tupla = super.unbind(tutorialSession, "title", "abstract$", "sessionType", "startSession", "finishSession", "furtherInformation");
		tupla.put("draftMode", draftMode);
		tupla.put("sessionType", opciones);
		tupla.put("tutorialId", super.getRequest().getData("tutorialId", int.class));

		super.getResponse().setData(tupla);
	}

}
