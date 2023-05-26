
package acme.features.lecturer.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureShowService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository		repository;

	@Autowired
	protected SystemConfigurationService	configurationService;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
		int id;
		Lecture object;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId);
	}

	@Override
	public void load() {
		Lecture object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		final String lang = super.getRequest().getLocale().getLanguage();
		Tuple tuple;
		tuple = super.unbind(object, "title", "abstractt", "estimatedLearningTime", "body", "lectureType", "furtherInformation");
		tuple.put("confirmation", false);
		final SelectChoices choices;
		choices = SelectChoices.from(LectureType.class, object.getLectureType());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}
}
