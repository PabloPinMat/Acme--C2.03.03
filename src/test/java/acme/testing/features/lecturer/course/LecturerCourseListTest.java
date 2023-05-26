
package acme.testing.features.lecturer.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerCourseListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/course/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex, final String code, final String title, final String courseAbstract, final String retailPrice, final String link) {

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Teacher", "List Courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseIndex, 0, title);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/lecturer/course/list");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/course/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/course/list");
		super.checkPanicExists();
		super.signOut();
	}

}
