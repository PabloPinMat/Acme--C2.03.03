
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialPublishTest extends TestHarness {

	@Autowired
	protected AssistantTutorialRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final String code, final String title, final String resume, final String goals, final String course, final String estimatedTime) {
		// HINT: this test authenticates as an employer, lists his or her applications,
		// HINT+ changes their status and checks that it's been updated.

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialIndex);
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", resume);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("estimatedTime", estimatedTime);

		super.clickOnSubmit("Publish");

		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialIndex, final String code, final String title, final String resume, final String goals, final String course, final String estimatedTime) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialIndex);
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", resume);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("estimatedTime", estimatedTime);

		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findTutorialsByAssistantId("assistant2");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				param = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/publish", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/publish", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer2", "lecturer2");
				super.request("/assistant/tutorial/publish", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/publish", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
