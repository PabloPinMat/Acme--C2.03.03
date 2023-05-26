
package acme.testing.student.enrolment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentEnrolmentListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/enrolment/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code, final String motivations, final String goals) {

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");
		super.checkListingExists();

		super.checkColumnHasValue(enrolmentIndex, 0, code);
		super.checkColumnHasValue(enrolmentIndex, 1, motivations);
		super.checkColumnHasValue(enrolmentIndex, 2, goals);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the applications of an employer as a
		// HINT+ principal with the wrong role.

		super.checkLinkExists("Sign in");
		super.request("/student/enrolment/list-mine");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/enrolment/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/enrolment/list-mine");
		super.checkPanicExists();
		super.signOut();
	}

}
