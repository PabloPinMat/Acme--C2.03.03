
package acme.testing.features.student.enrolment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentEnrolmentCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/enrolment/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final String code, final String course, final String motivation, final String goals) {

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");

		super.checkListingExists();

		super.clickOnButton("Create");

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("motivation", motivation);
		super.fillInputBoxIn("goals", goals);

		super.clickOnSubmit("Create");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentIndex, 0, code);

		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/enrolment/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int enrolmentIndex, final String code, final String course, final String motivation, final String goals) {

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");

		super.checkListingExists();

		super.clickOnButton("Create");

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("motivation", motivation);
		super.fillInputBoxIn("goals", goals);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/student/enrolment/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/enrolment/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/enrolment/create");
		super.checkPanicExists();
		super.signOut();
	}

}
