
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionRepositoryTest repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorialSession/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final int tutorialSessionIndex, final String code, final String title, final String startSession, final String finishSession, final String sessionType) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		final String titulo = this.repository.findTutorialTitleByCode(code);

		super.checkColumnHasValue(tutorialIndex, 0, titulo);
		super.clickOnListingRecord(tutorialIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Sessions");

		super.checkColumnHasValue(tutorialSessionIndex, 0, title);
		super.checkColumnHasValue(tutorialSessionIndex, 1, sessionType);
		super.checkColumnHasValue(tutorialSessionIndex, 2, startSession);
		super.checkColumnHasValue(tutorialSessionIndex, 3, finishSession);

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
				super.request("/assistant/tutorial-session/list", uri);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/list", uri);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/list", uri);
				super.checkPanicExists();
				super.signOut();

			}

		}
	}

}
