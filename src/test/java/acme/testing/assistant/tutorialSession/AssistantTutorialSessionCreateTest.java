
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.sessions.TutorialSession;
import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionCreateTest extends TestHarness {

	@Autowired
	protected AssistantTutorialSessionRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorialSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
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

		super.clickOnButton("Create session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", resume);
		super.fillInputBoxIn("startSession", startSession);
		super.fillInputBoxIn("finishSession", finishSession);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("furtherInformation", furtherInformation);

		super.clickOnSubmit("Create");

		super.signOut();
		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialIndex, 0, titulo);
		super.clickOnListingRecord(tutorialIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Sessions");

		super.checkColumnHasValue(tutorialSessionIndex, 0, title);
		super.checkColumnHasValue(tutorialSessionIndex, 1, sessionType);
		super.checkColumnHasValue(tutorialSessionIndex, 2, startSession);
		super.checkColumnHasValue(tutorialSessionIndex, 3, finishSession);

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

	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorialSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int tutorialSessionIndex, final String code, final String title, final String resume, final String startSession, final String finishSession, final String sessionType, final String furtherInformation) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		final String titulo = this.repository.findTutorialTitleByCode(code);

		super.checkColumnHasValue(tutorialSessionIndex, 0, titulo);
		super.clickOnListingRecord(tutorialSessionIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create session");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", resume);
		super.fillInputBoxIn("startSession", startSession);
		super.fillInputBoxIn("finishSession", finishSession);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("furtherInformation", furtherInformation);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

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
				super.request("/assistant/tutorial-session/create", uri);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/create", uri);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/create", uri);
				super.checkPanicExists();
				super.signOut();

			}

		}
	}
}
