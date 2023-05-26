
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final String code, final String title, final String resume, final String goals, final String course, final String estimatedTime) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", resume);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("estimatedTime", estimatedTime);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: this is a listing, which implies that no data must be entered in any forms.
		// HINT+ Then, there are not any negative test cases for this feature.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show enrolments that the principal cannot show.

		final Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findTutorialsByAssistantId("assistant2");
		for (final Tutorial tutorial : tutorials) {
			param = String.format("id=%d", tutorial.getId());

			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial/show", param);
			super.checkPanicExists();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/assistant/tutorial/show", param);
			super.checkPanicExists();
			super.signOut();
		}

	}
}
