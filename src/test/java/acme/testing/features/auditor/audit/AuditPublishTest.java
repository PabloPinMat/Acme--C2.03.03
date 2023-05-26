
package acme.testing.features.auditor.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditPublishTest extends TestHarness {
	
	@Autowired
	protected AuditTestRepository repository;
	
	@Test
	public void test100Positive() {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();

		super.checkColumnHasValue(0, 4, "false");
		
		super.clickOnListingRecord(0);

		super.clickOnSubmit("Publish");

		super.checkListingExists();

		super.checkColumnHasValue(0, 4, "true");

		super.signOut();

	}
	
	
	@Test
	public void test300Hacking() {
		

		Audit audit;
		String param;

		audit = this.repository.findAuditByCode("A2345");
		param = String.format("id=%d", audit.getId());

		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/publish", param);
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/audit/publish", param);
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/audit/publish", param);
		super.checkPanicExists();
		super.signOut();
	}
	

}
