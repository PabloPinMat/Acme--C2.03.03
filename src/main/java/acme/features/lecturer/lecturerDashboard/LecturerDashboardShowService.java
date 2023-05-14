
package acme.features.lecturer.lecturerDashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.LectureType;
import acme.features.forms.LecturerDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerDashboardShowService extends AbstractService<Lecturer, LecturerDashboard> {

	@Autowired
	protected LecturerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		LecturerDashboard dashboard;
		Lecturer lecturer;

		dashboard = new LecturerDashboard();

		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		lecturer = this.repository.findLecturerByUserAccountId(userAccountId);

		Double averageLectureLearningTime;
		Double maxLectureLearningTime;
		Double minLectureLearningTime;
		Double devLectureLearningTime;

		averageLectureLearningTime = this.repository.findAverageLectureLearningTime(lecturer).orElse(null);
		maxLectureLearningTime = this.repository.findMaxLectureLearningTime(lecturer).orElse(null);
		minLectureLearningTime = this.repository.findMinLectureLearningTime(lecturer).orElse(null);
		devLectureLearningTime = this.repository.findLinearDevLectureLearningTime(lecturer).orElse(null);

		dashboard.setAverageTimeOfLectures(averageLectureLearningTime);
		dashboard.setMinimumTimeOfLectures(minLectureLearningTime);
		dashboard.setMaximumTimeOfLectures(maxLectureLearningTime);
		dashboard.setDeviationTimeOfLectures(devLectureLearningTime);

		Collection<Double> courseEstimatedLearningTime;
		courseEstimatedLearningTime = this.repository.findEstimatedLearningTimeByCourse(lecturer);
		dashboard.calculateCourseAverage(courseEstimatedLearningTime);
		dashboard.calculateCourseMax(courseEstimatedLearningTime);
		dashboard.calculateCourseMin(courseEstimatedLearningTime);
		dashboard.calculateCourseDev(courseEstimatedLearningTime);

		Integer handsOnLectures;
		Integer theoricalLectures;
		handsOnLectures = this.repository.findNumOfLecturesByType(lecturer, LectureType.HANDS_ON).orElse(0);
		theoricalLectures = this.repository.findNumOfLecturesByType(lecturer, LectureType.THEORICAL).orElse(0);

		dashboard.setTotalLectures(handsOnLectures + theoricalLectures);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		Principal principal;
		int userAccountId;
		Lecturer lecturer;
		Integer handsOnLectures;
		Integer theoricalLectures;
		Tuple tuple;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		lecturer = this.repository.findLecturerByUserAccountId(userAccountId);
		handsOnLectures = this.repository.findNumOfLecturesByType(lecturer, LectureType.HANDS_ON).orElse(0);
		theoricalLectures = this.repository.findNumOfLecturesByType(lecturer, LectureType.THEORICAL).orElse(0);

		tuple = super.unbind(object, "totalLectures", "averageTimeOfLectures", "deviationTimeOfLectures", "minimumTimeOfLectures", "maximumTimeOfLectures", "averageTimeOfCourses", "deviationTimeOfCourses", "minimumTimeOfCourses", "maximumTimeOfCourses");
		tuple.put("handsOnLectures", handsOnLectures);
		tuple.put("theoricalLectures", theoricalLectures);

		super.getResponse().setData(tuple);
	}
}
