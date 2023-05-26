
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureListAllService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository		repository;

	@Autowired
	protected SystemConfigurationService	configurationService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Principal principal;

		principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Lecturer.class);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<Lecture> objects;
		final Lecturer lecturer = this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		objects = this.repository.findLecturesByLecturer(lecturer);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		final String lang = super.getRequest().getLocale().getLanguage();
		Tuple tuple;
		tuple = super.unbind(object, "title", "abstractt", "estimatedLearningTime", "lectureType", "draftMode");
		tuple.put("draftMode", this.configurationService.booleanTranslated(object.isDraftMode(), lang));
		super.getResponse().setGlobal("showCreate", false);
		super.getResponse().setData(tuple);
	}

}
