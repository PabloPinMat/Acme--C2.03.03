
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialSessionRepositoryTest extends AbstractRepository {

	@Query("SELECT e.title FROM Tutorial e WHERE e.code = :code")
	String findTutorialTitleByCode(String code);

	@Query("SELECT e FROM Tutorial e WHERE e.assitant.userAccount.username = :assistantId")
	Collection<Tutorial> findTutorialsByAssistantId(String assistantId);

	@Query("SELECT e from TutorialSession e where e.tutorial = :tutorial")
	Collection<TutorialSession> findSessionsByTutorial(Tutorial tutorial);

}
