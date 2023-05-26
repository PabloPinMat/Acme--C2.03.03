
package acme.testing.features.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.activity.Activity;
import acme.testing.TestHarness;

public class StudentActivityUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepositoryTest repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/activity/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int enrolmentIndex, final int activityIndex, final String title, final String summary, final String activityType, final String startDate, final String endDate, final String link) {

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");
		super.checkListingExists();

		super.clickOnListingRecord(enrolmentIndex);
		super.clickOnButton("Activities");

		super.clickOnListingRecord(activityIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("endDate", endDate);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.clickOnMenu("Student", "My enrolments");
		super.checkListingExists();

		super.clickOnListingRecord(enrolmentIndex);
		super.clickOnButton("Activities");

		super.checkListingExists();
		super.checkColumnHasValue(enrolmentIndex, 0, title);
		super.checkColumnHasValue(enrolmentIndex, 1, activityType);
		super.checkColumnHasValue(enrolmentIndex, 2, startDate);
		super.checkColumnHasValue(enrolmentIndex, 3, endDate);

		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("summary", summary);
		super.checkInputBoxHasValue("activityType", activityType);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("endDate", endDate);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/student/activity/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int enrolmentIndex, final int activityRecordIndex, final String title, final String summary, final String activityType, final String startDate, final String endDate, final String link) {

		super.signIn("student2", "student2");

		super.clickOnMenu("Student", "My enrolments");
		super.checkListingExists();

		super.clickOnListingRecord(enrolmentIndex);
		super.clickOnButton("Activities");

		super.clickOnListingRecord(enrolmentIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("summary", summary);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("endDate", endDate);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Activity> activities;
		String param;

		activities = this.repository.findManyActivitiesByStudentUsername("student2");
		for (final Activity activity : activities) {
			param = String.format("id=%d", activity.getId());

			super.checkLinkExists("Sign in");
			super.request("/student/activity/update", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/student/activity/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/student/activity/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("assistant1", "assistant1");
			super.request("/student/activity/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}
