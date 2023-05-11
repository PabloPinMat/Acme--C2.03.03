
package acme.features.student.activity;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.activity.Activity;
import acme.entities.enrolments.Enrolment;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface StudentActivityRepository extends AbstractRepository {

	@Query("select a from Activity a where a.id = :id")
	Activity findOneActivityById(int id);

	@Query("select e from Enrolment e where e.id = :id")
	Enrolment findOneEnrolmentById(int id);

	@Query("select a from Activity a where a.enrolment.id = :masterId")
	Collection<Activity> findManyActivitiesByEnrolmentId(int masterId);

	@Query("select a.enrolment from Activity a where a.id = :id")
	Enrolment findOneEnrolmentByActivityId(int id);

}
