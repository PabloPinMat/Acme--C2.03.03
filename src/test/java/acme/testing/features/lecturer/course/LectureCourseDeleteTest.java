
package acme.testing.features.lecturer.course;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LectureCourseDeleteTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/course/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final String code, final String title, final String courseAbstract, final String retailPrice, final String link) {
		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Teacher", "List Courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.checkListingExists();

		super.signOut();

	}
}
