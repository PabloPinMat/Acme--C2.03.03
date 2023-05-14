
package acme.features.lecturer.lecturerDashboard;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerDashboardRepository extends AbstractRepository {

	@Query("select l from Lecturer l where l.userAccount.id = :id")
	Lecturer findLecturerByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select count(l)  from Lecture l where l.lecturer = :lecturer and l.lectureType = :lectureType")
	Optional<Integer> findNumOfLecturesByType(Lecturer lecturer, LectureType lectureType);

	@Query("select max(l.estimatedLearningTime) from Lecture l where l.lecturer = :lecturer")
	Optional<Double> findMaxLectureLearningTime(Lecturer lecturer);

	@Query("select min(l.estimatedLearningTime) from Lecture l where l.lecturer = :lecturer")
	Optional<Double> findMinLectureLearningTime(Lecturer lecturer);

	@Query("select stddev(l.estimatedLearningTime) from Lecture l where l.lecturer = :lecturer")
	Optional<Double> findLinearDevLectureLearningTime(Lecturer lecturer);

	@Query("select avg(l.estimatedLearningTime) from Lecture l where l.lecturer = :lecturer")
	Optional<Double> findAverageLectureLearningTime(Lecturer lecturer);

	@Query("select sum(l.estimatedLearningTime) from Course c join CourseLecture cl on c = cl.course join Lecture l on cl.lecture = l where c.lecturer = :lecturer group by c")
	Collection<Double> findEstimatedLearningTimeByCourse(Lecturer lecturer);

	@Query("select l from Lecturer l where l.userAccount.id = :id")
	Lecturer findLecturerByIdUserAccount(int id);
}
