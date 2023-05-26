
package acme.testing.features.auditor.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuditCreateTest extends TestHarness {

	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/audit/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();

		super.clickOnButton("Create");

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);

		super.clickOnSubmit("Create");

		super.checkListingExists();

		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("conclusion", conclusion);
		super.checkInputBoxHasValue("strongPoints", strongPoints);
		super.checkInputBoxHasValue("weakPoints", weakPoints);

		super.clickOnButton("Auditing records");
		super.checkListingExists();
		super.checkListingEmpty();

		super.signOut();

	}
	

	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/audit/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();

		super.clickOnButton("Create");

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	 
	
	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/audit/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/audit/create");
		super.checkPanicExists();
		super.signOut();
	}
	

}
