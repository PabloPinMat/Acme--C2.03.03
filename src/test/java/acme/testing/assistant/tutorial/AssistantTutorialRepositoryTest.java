
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialRepositoryTest extends AbstractRepository {

	@Query("SELECT e FROM Tutorial e WHERE e.assitant.userAccount.username = :assistantId")
	Collection<Tutorial> findTutorialsByAssistantId(String assistantId);

}
