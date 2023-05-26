
package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banner.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBannerRepository repository;

	// AbstractService<Employer, Company> -------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Banner banner;
		int id;

		id = super.getRequest().getData("id", int.class);
		banner = this.repository.findBannerById(id);

		super.getBuffer().setData(banner);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "displayPeriodBegin", "displayPeriodFinish", "pictureLink", "slogan", "webDocument");
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		if (object.getInstantiationMoment() != null && object.getDisplayPeriodFinish() != null && object.getDisplayPeriodBegin() != null) {
			if (!super.getBuffer().getErrors().hasErrors("displayPeriodBegin")) {
				super.state(MomentHelper.isBefore(object.getInstantiationMoment(), object.getDisplayPeriodBegin()), "displayPeriodBegin", "administrator.banner.form.error.initDate-too-soon");

				if (!super.getBuffer().getErrors().hasErrors("displayPeriodFinish")) {
					Date minimumFinishDate;
					minimumFinishDate = MomentHelper.deltaFromMoment(object.getDisplayPeriodBegin(), 7, ChronoUnit.DAYS);
					super.state(MomentHelper.isAfterOrEqual(object.getDisplayPeriodFinish(), minimumFinishDate), "displayPeriodFinish", "administrator.banner.form.error.finishDate-too-soon");
				}
			}
		} else
			super.state(false, "*", "student.activity.form.error.dateNull");
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiationMoment", "displayPeriodBegin", "displayPeriodFinish", "pictureLink", "slogan", "webDocument");

		super.getResponse().setData(tuple);
	}
}
