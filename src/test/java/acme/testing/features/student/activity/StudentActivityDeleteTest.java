
package acme.testing.features.student.activity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class StudentActivityDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepositoryTest repository;

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/features/student/activity/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int enrolmentIndex, final int activityIndex) {
	//		// HINT: this test logs in as an student, lists his or her enrolments, 
	//		// HINT+ navigates to their activities, selects one of them, deletes it,
	//		// HINT+ and then checks that the deletion has actually been performed.
	//
	//		super.signIn("student2", "student2");
	//
	//		super.clickOnMenu("Student", "My enrolments");
	//		super.checkListingExists();
	//
	//		super.clickOnListingRecord(enrolmentIndex);
	//		super.clickOnButton("Activities");
	//
	//		super.clickOnListingRecord(activityIndex);
	//		super.checkFormExists();
	//		super.clickOnSubmit("Delete");
	//
	//		super.checkListingExists();
	//		super.checkListingEmpty();
	//
	//		super.signOut();
	//	}


	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a deletion that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {

		// this test tries to delete an activity having logged in as a different user than the owner

		super.checkLinkExists("Sign in");
		super.request("/student/activity/delete", "id=54");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/activity/delete", "id=54");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/student/activity/delete", "id=54");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/student/activity/delete", "id=54");
		super.checkPanicExists();
		super.signOut();

	}

}
