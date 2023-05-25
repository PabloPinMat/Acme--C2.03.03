
package acme.testing.assistant.tutorialSession;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantTutorialSessionRepositoryTest extends AbstractRepository {

	@Query("SELECT e.title FROM Tutorial e WHERE e.code = :code")
	String findTutorialTitleByCode(String code);

}
