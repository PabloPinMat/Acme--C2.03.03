
package acme.testing.features.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractt, final String estimatedLearningTime, final String body, final String lectureType, final String furtherInformation) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Teacher", "My lectures");

		super.checkListingExists();

		super.clickOnButton("Create lecture");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractt", abstractt);
		super.fillInputBoxIn("estimatedLearningTime", estimatedLearningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("furtherInformation", furtherInformation);

		super.clickOnSubmit("Create");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int lectureIndex, final String title, final String abstractt, final String estimatedLearningTime, final String body, final String lectureType, final String furtherInformation) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Teacher", "My lectures");

		super.checkListingExists();

		super.clickOnButton("Create lecture");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractt", abstractt);
		super.fillInputBoxIn("estimatedLearningTime", estimatedLearningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("furtherInformation", furtherInformation);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();
	}

}
