
package acme.testing.features.auditor.audit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class AuditDeleteTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;
	
	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/audit/delete.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final String code, final String codeDeleted) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();
		
		super.checkColumnHasValue(0, 0, code);

		super.clickOnListingRecord(0);

		super.clickOnSubmit("Delete");

		super.checkListingExists();

		super.checkColumnHasValue(0, 0, codeDeleted);

		super.signOut();

	}
	 
	
	

}
