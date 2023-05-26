
package acme.testing.assistant.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AssistantTutorialCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialIndex, final String code, final String title, final String resume, final String goals, final String course, final String estimatedTime) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");

		super.checkListingExists();

		super.clickOnButton("Create tutorial");

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", resume);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("estimatedTime", estimatedTime);

		super.clickOnSubmit("Create");

		super.signOut();
		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialIndex, 0, title);
		super.checkColumnHasValue(tutorialIndex, 1, resume);
		super.checkColumnHasValue(tutorialIndex, 2, goals);

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

	@ParameterizedTest
	@CsvFileSource(resources = "/features/assistant/tutorials/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int tutorialIndex, final String code, final String title, final String resume, final String goals, final String course, final String estimatedTime) {

		super.signIn("assistant2", "assistant2");

		super.clickOnMenu("Assistant", "My tutorials");

		super.checkListingExists();

		super.clickOnButton("Create tutorial");

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstract$", resume);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("estimatedTime", estimatedTime);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/assistant/tutorial/create");
		super.checkPanicExists();
		super.signOut();
	}

}
