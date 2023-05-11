
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialSessionRepository extends AbstractRepository {

	@Query("select a from Tutorial a where a.id = :id")
	Tutorial findTutorialById(Integer id);

	@Query("select a from TutorialSession a where a.id = :id")
	TutorialSession findTutorialSessionById(Integer id);

	@Query("select a from TutorialSession a where a.tutorial.id = :tutorialId")
	Collection<TutorialSession> findSessionsByTutorialId(Integer tutorialId);

	@Query("select a.tutorial from TutorialSession a where a.id = :tutorialSessionId")
	Tutorial findTutorialByTutorialSessionId(Integer tutorialSessionId);

}
