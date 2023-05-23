
package acme.testing.features.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/lecture/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractt, final String estimatedLearningTime, final String body, final String lectureType, final String furtherInformation) {

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Teacher", "My lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(lectureIndex, 0, title);
		super.checkColumnHasValue(lectureIndex, 1, abstractt);
		super.checkColumnHasValue(lectureIndex, 2, estimatedLearningTime);

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/list-all");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/lecture/list-all");
		super.checkPanicExists();
		super.signOut();
	}
}
