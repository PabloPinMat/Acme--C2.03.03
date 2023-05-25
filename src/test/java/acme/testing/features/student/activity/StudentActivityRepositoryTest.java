
package acme.testing.features.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.activity.Activity;
import acme.entities.enrolments.Enrolment;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface StudentActivityRepositoryTest extends AbstractRepository {

	@Query("SELECT e FROM Enrolment e WHERE e.student.id = :studentId")
	Collection<Enrolment> findEnrolmentsByStudentId(int studentId);

	@Query("select a from Activity a where a.enrolment.id = :masterId")
	Collection<Activity> findManyActivitiesByEnrolmentId(int masterId);

}
