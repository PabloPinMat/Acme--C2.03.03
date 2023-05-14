
package acme.testing.features.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumUpdateTest extends TestHarness {

	@Autowired
	protected CompanyPracticumTestRepository repository;

	// Test methods ------------------------------------------------------------

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/features/company/practicum/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int practicumIndex, final String code, final String course, final String title, final String abstractPracticum, final String goals, final String estimatedTotalTime) {
	//
	//		super.signIn("company1", "company1");
	//
	//		super.clickOnMenu("Company", "My practicums");
	//		super.checkListingExists();
	//
	//		super.checkColumnHasValue(practicumIndex, 0, code);
	//		super.clickOnListingRecord(practicumIndex);
	//		super.checkFormExists();
	//		super.fillInputBoxIn("code", code);
	//		super.fillInputBoxIn("course", course);
	//		super.fillInputBoxIn("title", title);
	//		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
	//		super.fillInputBoxIn("goals", goals);
	//		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
	//
	//		super.clickOnSubmit("Update");
	//
	//		super.checkListingExists();
	//		super.checkColumnHasValue(practicumIndex, 0, code);
	//		super.checkColumnHasValue(practicumIndex, 1, title);
	//		super.checkColumnHasValue(practicumIndex, 2, estimatedTotalTime);
	//
	//		super.clickOnListingRecord(practicumIndex);
	//		super.checkFormExists();
	//		super.checkInputBoxHasValue("code", code);
	//		super.checkInputBoxHasValue("course", course);
	//		super.checkInputBoxHasValue("title", title);
	//		super.checkInputBoxHasValue("abstractPracticum", abstractPracticum);
	//		super.checkInputBoxHasValue("goals", goals);
	//		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
	//
	//		super.signOut();
	//	}

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/features/company/practicum/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test200Negative(final int practicumIndex, final String code, final String course, final String title, final String abstractPracticum, final String goals, final String estimatedTotalTime) {
	//
	//		super.signIn("company1", "company1");
	//
	//		super.clickOnMenu("Company", "My practicums");
	//		super.checkListingExists();
	//
	//		super.checkColumnHasValue(practicumIndex, 0, code);
	//		super.clickOnListingRecord(practicumIndex);
	//		super.checkFormExists();
	//		super.fillInputBoxIn("code", code);
	//		super.fillInputBoxIn("course", course);
	//		super.fillInputBoxIn("title", title);
	//		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
	//		super.fillInputBoxIn("goals", goals);
	//		super.fillInputBoxIn("estimatedTotalTime", estimatedTotalTime);
	//
	//		super.clickOnSubmit("Update");
	//
	//		super.checkErrorsExist();
	//
	//		super.signOut();
	//	}


	@Test
	public void test300Hacking() {

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			param = String.format("id=%d", practicum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();

			super.signIn("administrator1", "administrator1");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company2", "company2");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/practicum/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}
