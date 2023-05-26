
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureListService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository		repository;

	@Autowired
	protected SystemConfigurationService	configurationService;


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("masterId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findCourseById(masterId);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		Collection<Lecture> objects;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findLecturesByCourse(masterId);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		final String lang = super.getRequest().getLocale().getLanguage();
		Tuple tuple;
		tuple = super.unbind(object, "title", "abstractt", "estimatedLearningTime", "lectureType", "draftMode");
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().setGlobal("masterId", masterId);
		tuple.put("masterId", masterId);
		final SelectChoices choices;
		choices = SelectChoices.from(LectureType.class, object.getLectureType());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		tuple.put("draftMode", this.configurationService.booleanTranslated(object.isDraftMode(), lang));

		final Course c = this.repository.findCourseById(masterId);
		final boolean showCreate = c.isDraftMode();
		super.getResponse().setGlobal("showCreate", showCreate);
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Lecture> object) {
		assert object != null;

		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().setGlobal("masterId", masterId);

		final Course c = this.repository.findCourseById(masterId);
		final boolean showCreate = c.isDraftMode();
		super.getResponse().setGlobal("showCreate", showCreate);

	}

}
