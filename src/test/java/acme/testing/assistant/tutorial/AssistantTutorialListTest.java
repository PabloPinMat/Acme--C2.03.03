
package acme.testing.assistant.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AssistantTutorialListTest extends TestHarness {

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

		super.checkLinkExists("Sign in");
		super.request("/assistant/tutorial/list");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/assistant/tutorial/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/assistant/tutorial/list");
		super.checkPanicExists();
		super.signOut();
	}

}
