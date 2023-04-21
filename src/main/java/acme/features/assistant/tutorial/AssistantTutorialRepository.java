
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.sessions.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assitant.userAccount.id = :id")
	Collection<Tutorial> findTutorialsByAssistantId(int id);

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findTutorialById(int id);

	@Query("select s from Session s where s.tutorial = :tutorial")
	Collection<Session> findTutorialSessionsByTutorial(Tutorial tutorial);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select a from Assistant a where a.id = :id")
	Assistant findOneAssistantById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select e from Tutorial e where e.code = :code")
	Tutorial findOneTutorialByCode(String code);

}
