
package acme.testing.features.auditor.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditUpdateTest extends TestHarness {

	
	@Autowired
	protected AuditTestRepository repository;
	
	//Ejecutar primero el de hacking y despues los otros 
	@Test
	public void test300Hacking() {
		
		Audit audit;
		String param;
		
		audit = this.repository.findAuditByCode("X2345");
		param = String.format("id=%d", audit.getId());
		
		
		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/update", param);
		super.checkPanicExists();
		
		super.signIn("administrator", "administrator");
		super.request("/auditor/audit/update", param);
		super.checkPanicExists();
		super.signOut();
		
		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/audit/update", param);
		super.checkPanicExists();
		super.signOut();
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/audit/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();

		super.clickOnListingRecord(0);
		
		super.checkFormExists();

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);

		super.clickOnSubmit("Update");

		super.checkListingExists();

		super.checkColumnHasValue(0, 0, code);

		super.signOut();

	}
	
	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/audit/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final String course, final String code, final String conclusion, final String strongPoints, final String weakPoints) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();

		super.clickOnListingRecord(0);
		
		super.checkFormExists();

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("conclusion", conclusion);
		super.fillInputBoxIn("strongPoints", strongPoints);
		super.fillInputBoxIn("weakPoints", weakPoints);

		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}
	
	
	
	
	

}
