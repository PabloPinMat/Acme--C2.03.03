
package acme.testing.student.enrolment;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class StudentEnrolmentUpdateTest extends TestHarness {

	@Autowired
	protected StudentEnrolmentRepositoryTest repository;

	// Test methods -----------------------------------------------------------

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/features/student/enrolment/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int enrolmentIndex, final String code, final String course, final String motivation, final String goals) {
	//		// HINT: this test authenticates as an employer, lists his or her applications,
	//		// HINT+ changes their status and checks that it's been updated.
	//
	//		super.signIn("student2", "student2");
	//
	//		super.clickOnMenu("Student", "My enrolments");
	//
	//		super.checkListingExists();
	//
	//		super.clickOnListingRecord(0);
	//
	//		super.checkFormExists();
	//
	//		super.fillInputBoxIn("course", course);
	//		super.fillInputBoxIn("code", code);
	//		super.fillInputBoxIn("motivation", motivation);
	//		super.fillInputBoxIn("goals", goals);
	//
	//		super.clickOnSubmit("Update");
	//
	//		super.checkListingExists();
	//
	//		super.checkColumnHasValue(0, 0, code);
	//
	//		super.signOut();
	//	}


	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/enrolment/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final String code, final String course, final String motivation, final String goals) {

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");

		super.checkListingExists();

		super.clickOnListingRecord(0);

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("motivation", motivation);
		super.fillInputBoxIn("goals", goals);

		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

}
