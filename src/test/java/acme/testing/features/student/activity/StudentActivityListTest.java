//
//package acme.testing.features.student.activity;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import acme.entities.enrolments.Enrolment;
//import acme.testing.TestHarness;
//
//public class StudentActivityListTest extends TestHarness {
//
//	// Internal state ---------------------------------------------------------
//
//	@Autowired
//	protected StudentActivityRepositoryTest repository;
//
//	// Test methods -----------------------------------------------------------
//
//
//	@ParameterizedTest
//	@CsvFileSource(resources = "/features/student/activity/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
//	public void test100Positive(final int enrolmentIndex, final String code, final int activityIndex, final String title, final String type, final String startDate, final String endDate) {
//		// HINT: this test authenticates as an student, then lists his or her enrolments, 
//		// HINT+ selects one of them, and check that it has the expected activities.
//
//		super.signIn("student2", "student2");
//
//		super.clickOnMenu("Student", "My enrolments");
//		super.checkListingExists();
//
//		super.checkColumnHasValue(enrolmentIndex, 0, code);
//		super.clickOnListingRecord(enrolmentIndex);
//		super.checkInputBoxHasValue("code", code);
//		super.clickOnButton("Activities");
//
//		super.checkListingExists();
//		super.checkColumnHasValue(activityIndex, 0, title);
//		super.checkColumnHasValue(activityIndex, 1, type);
//		super.checkColumnHasValue(activityIndex, 2, startDate);
//		super.checkColumnHasValue(activityIndex, 3, endDate);
//		super.clickOnListingRecord(activityIndex);
//
//		super.signOut();
//	}
//
//	@Test
//	public void test200Negative() {
//		// HINT: there's no negative test case for this listing, since it doesn't
//		// HINT+ involve filling in any forms.
//	}
//
//	@Test
//	public void test300Hacking() {
//		// HINT: this test tries to list the activities of an enrolment that is finished
//		// HINT+ using a principal that didn't create it. 
//
//		Collection<Enrolment> enrolments;
//		String param;
//
//		enrolments = this.repository.findEnrolmentsByStudentId();
//		param = String.format("enrolmentId=%d", enrolment.getId());
//
//		super.checkLinkExists("Sign in");
//		super.request("/student/activity/list", param);
//		super.checkPanicExists();
//
//		super.signIn("administrator1", "administrator1");
//		super.request("/student/activity/list", param);
//		super.checkPanicExists();
//		super.signOut();
//
//		super.signIn("student2", "student2");
//		super.request("/student/activity/list", param);
//		super.checkPanicExists();
//		super.signOut();
//
//		super.signIn("lecturer1", "lecturer1");
//		super.request("/student/activity/list", param);
//		super.checkPanicExists();
//		super.signOut();
//	}
//
//}
