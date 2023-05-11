
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedTutorialRepository extends AbstractRepository {

	@Query("select p from Tutorial p where p.course.id = :courseId")
	Collection<Tutorial> findTutorialByCourseId(Integer courseId);

	@Query("select p from Tutorial p where p.id = :id")
	Tutorial findTutorialById(Integer id);

}
