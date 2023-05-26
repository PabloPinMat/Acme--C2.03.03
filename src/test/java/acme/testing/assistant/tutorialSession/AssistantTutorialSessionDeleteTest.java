
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionDeleteTest extends TestHarness {

	@Autowired
	protected AssistantTutorialSessionRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorialSession/delete.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final int tutorialSessionIndex, final String code, final String title, final String resume, final String startSession, final String finishSession, final String sessionType,
		final String furtherInformation) {
		// HINT: this test authenticates as an employer, lists his or her applications,
		// HINT+ changes their status and checks that it's been updated.

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		final String titulo = this.repository.findTutorialTitleByCode(code);

		super.checkColumnHasValue(tutorialIndex, 0, titulo);
		super.clickOnListingRecord(tutorialIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialSessionIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstract$", resume);
		super.checkInputBoxHasValue("startSession", startSession);
		super.checkInputBoxHasValue("finishSession", finishSession);
		super.checkInputBoxHasValue("sessionType", sessionType);
		super.checkInputBoxHasValue("furtherInformation", furtherInformation);

		super.clickOnSubmit("Delete");

		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		final Collection<Tutorial> tutorials;
		String uri = "";
		Collection<TutorialSession> sessions;

		tutorials = this.repository.findTutorialsByAssistantId("assistant2");
		for (final Tutorial tutorial : tutorials) {
			sessions = this.repository.findSessionsByTutorial(tutorial);
			for (final TutorialSession session : sessions) {
				uri = String.format("tutorialId=%d", session.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/delete", uri);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/delete", uri);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/delete", uri);
				super.checkPanicExists();
				super.signOut();

			}

		}
	}

}
