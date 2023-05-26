
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialListTest extends TestHarness {

	@Autowired
	protected AssistantTutorialRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final String title, final String resume, final String goals) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialIndex, 0, title);
		super.checkColumnHasValue(tutorialIndex, 1, resume);
		super.checkColumnHasValue(tutorialIndex, 2, goals);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the applications of an employer as a
		// HINT+ principal with the wrong role.

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findTutorialsByAssistantId("assistant2");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				param = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer2", "lecturer2");
				super.request("/assistant/tutorial/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/list", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
