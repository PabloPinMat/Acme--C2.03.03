
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SystemConfigurationService;
import acme.entities.course.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseListService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository		repository;

	@Autowired
	protected SystemConfigurationService	configurationService;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		final boolean status;
		final Principal principal;

		principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Lecturer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		//declaración de variables
		Collection<Course> objects;
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		objects = this.repository.findCoursesByLecturerId(userAccountId);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		final String lang = super.getRequest().getLocale().getLanguage();
		Tuple tuple;

		tuple = super.unbind(object, "title", "courseAbstract", "retailPrice", "draftMode");
		tuple.put("draftMode", this.configurationService.booleanTranslated(object.isDraftMode(), lang));

		super.getResponse().setData(tuple);
	}
}
