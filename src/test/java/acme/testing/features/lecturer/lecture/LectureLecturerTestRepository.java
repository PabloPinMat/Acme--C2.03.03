
package acme.testing.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

public interface LectureLecturerTestRepository extends AbstractRepository {

	@Query("select l from Lecture l where l.lecturer.userAccount.username = :username")
	Collection<Lecture> findManyLecturesByLecturerUsername(String username);
}
