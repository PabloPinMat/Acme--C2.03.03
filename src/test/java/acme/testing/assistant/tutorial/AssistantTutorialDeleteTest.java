
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialDeleteTest extends TestHarness {

	@Autowired
	protected AssistantTutorialRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/delete.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final String code, final String title, final String resume, final String goals, final String course, final String estimatedTime) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialIndex);

		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", resume);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("estimatedTime", estimatedTime);
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findTutorialsByAssistantId("assistant2s");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode() || !tutorial.isDraftMode()) {
				param = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer2", "lecturer2");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
