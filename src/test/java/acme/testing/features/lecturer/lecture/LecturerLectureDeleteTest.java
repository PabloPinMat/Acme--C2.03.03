
package acme.testing.features.lecturer.lecture;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureDeleteTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex, final String title, final String abstractt, final String estimatedLearningTime, final String body, final String lectureType, final String furtherInformation) {
		super.signIn("lecturer2", "lecturer2");
		super.clickOnMenu("Teacher", "My lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.checkListingExists();

		super.signOut();

	}
}
