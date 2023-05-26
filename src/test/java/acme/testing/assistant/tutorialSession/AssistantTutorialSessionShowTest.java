
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorialSession/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final int tutorialSessionIndex, final String code, final String title, final String resume, final String startSession, final String finishSession, final String sessionType,
		final String furtherInformation) {

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

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: this is a listing, which implies that no data must be entered in any forms.
		// HINT+ Then, there are not any negative test cases for this feature.
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
				super.request("/assistant/tutorial-session/show", uri);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/show", uri);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/show", uri);
				super.checkPanicExists();
				super.signOut();

			}

		}
	}

}
