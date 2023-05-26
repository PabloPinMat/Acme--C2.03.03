
package acme.features.student.lecturer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface StudentLecturerRepository extends AbstractRepository {

	@Query("SELECT c.lecturer FROM Course c WHERE c.id = :id")
	Lecturer findLecturerByCourseId(int id);

}
