
package acme.testing.features.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionCreateTest extends TestHarness {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/features/company/practicumSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int practicumIndex, final int practicumSessionIndex, final String title, final String abstractSession, final String startTimePeriod, final String endTimePeriod, final String furtherInformationLink,
	//		final String confirmation) {
	//
	//		super.signIn("company1", "company1");
	//
	//		super.clickOnMenu("Company", "My practicums");
	//		super.checkListingExists();
	//
	//		super.clickOnListingRecord(practicumIndex);
	//		super.clickOnButton("Sessions");
	//
	//		super.clickOnButton("Create");
	//		super.fillInputBoxIn("title", title);
	//		super.fillInputBoxIn("abstractSession", abstractSession);
	//		super.fillInputBoxIn("startTimePeriod", startTimePeriod);
	//		super.fillInputBoxIn("endTimePeriod", endTimePeriod);
	//		super.fillInputBoxIn("furtherInformationLink", furtherInformationLink);
	//
	//		if (practicumIndex == 0)
	//			super.clickOnSubmit("Create");
	//		else {
	//			super.fillInputBoxIn("confirmation", confirmation);
	//			super.clickOnSubmit("Exceptional create");
	//		}
	//
	//		super.checkListingExists();
	//		super.checkColumnHasValue(practicumSessionIndex, 0, title);
	//		super.checkColumnHasValue(practicumSessionIndex, 1, startTimePeriod);
	//		super.checkColumnHasValue(practicumSessionIndex, 2, endTimePeriod);
	//
	//		super.clickOnListingRecord(practicumSessionIndex);
	//		super.checkInputBoxHasValue("title", title);
	//		super.checkInputBoxHasValue("abstractSession", abstractSession);
	//		super.checkInputBoxHasValue("startTimePeriod", startTimePeriod);
	//		super.checkInputBoxHasValue("endTimePeriod", endTimePeriod);
	//		super.checkInputBoxHasValue("furtherInformationLink", furtherInformationLink);
	//
	//		super.signOut();
	//	}


	@ParameterizedTest
	@CsvFileSource(resources = "/features/company/practicumSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumIndex, final int practicumSessionIndex, final String title, final String abstractSession, final String startTimePeriod, final String endTimePeriod, final String furtherInformationLink,
		final String confirmation) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "My practicums");
		super.checkListingExists();

		super.clickOnListingRecord(practicumIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("startTimePeriod", startTimePeriod);
		super.fillInputBoxIn("endTimePeriod", endTimePeriod);
		super.fillInputBoxIn("furtherInformationLink", furtherInformationLink);
		if (practicumIndex == 0)
			super.clickOnSubmit("Create");
		else {
			super.fillInputBoxIn("confirmation", confirmation);
			super.clickOnSubmit("Exceptional create");
		}
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

	@Test
	public void test302Hacking() {

		Collection<Practicum> practicums;
		String param;

		super.checkLinkExists("Sign in");
		super.signIn("company1", "company1");
		practicums = this.repository.findManyPracticumsByCompanyUsername("company2");
		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();
		}
	}
}
