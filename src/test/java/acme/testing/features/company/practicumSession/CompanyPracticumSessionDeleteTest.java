
package acme.testing.features.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.session.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionDeleteTest extends TestHarness {

	// Internal State -----------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/features/company/practicumSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumIndex, final int practicumSessionIndex, final String title, final String abstractSession, final String startTimePeriod, final String endTimePeriod, final String furtherInformationLink) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();
		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.checkListingExists();
		super.clickOnListingRecord(practicumSessionIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("startTimePeriod", startTimePeriod);
		super.checkInputBoxHasValue("endTimePeriod", endTimePeriod);
		super.checkInputBoxHasValue("furtherInformationLink", furtherInformationLink);

		super.signOut();
	}

	@Test
	public void test200Negative() {

	}

	@Test
	public void test300Hacking() {

		Collection<PracticumSession> practicumSessions;
		String param;

		practicumSessions = this.repository.findManyPracticumSessionsByCompanyUsername("company1");
		for (final PracticumSession practicumSession : practicumSessions)
			if (!practicumSession.getPracticum().isPublish()) {
				param = String.format("id=%d", practicumSession.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();

				super.signIn("administrator1", "administrator1");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum-session/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}
