
package acme.testing.student.enrolment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class StudentEnrolmentDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/enrolment/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code) {
		// HINT: this test logs in as an student, lists his or her enrolments, 
		// HINT+ selects one of them, deletes it, and then checks that 
		// HINT+ the deletion has actually been performed.

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");

		super.checkListingExists();

		super.checkColumnHasValue(enrolmentIndex, 0, code);

		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();

		super.clickOnSubmit("Delete");

		super.checkListingExists();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a deletion that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/student/enrolment/delete", "id=50");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/enrolment/delete", "id=50");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/enrolment/delete", "id=50");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/student/enrolment/delete", "id=50");
		super.checkPanicExists();
		super.signOut();

	}

}
