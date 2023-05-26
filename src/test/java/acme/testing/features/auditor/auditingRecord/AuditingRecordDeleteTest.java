
package acme.testing.features.auditor.auditingRecord;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.AuditingRecord;
import acme.testing.TestHarness;
import acme.testing.features.auditor.audit.AuditTestRepository;

public class AuditingRecordDeleteTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;
	
	@Test
	public void test100Positive() {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");

		super.checkListingExists();
		super.clickOnListingRecord(0);
		
		super.clickOnButton("Create auditing record");

		super.fillInputBoxIn("subject", "AAAAAAAAA");
		super.fillInputBoxIn("assessment", "assessment valido");
		super.fillInputBoxIn("link", "http://test.com");
		super.fillInputBoxIn("startPeriod", "2023/02/20 11:20");
		super.fillInputBoxIn("endPeriod", "2023/02/22 11:25");
		super.fillInputBoxIn("mark", "A"); 	

		super.clickOnSubmit("Create");
		
		super.clickOnButton("Create auditing record");

		super.fillInputBoxIn("subject", "BBBBBBBBBB");
		super.fillInputBoxIn("assessment", "assessment valido");
		super.fillInputBoxIn("link", "http://test.com");
		super.fillInputBoxIn("startPeriod", "2023/02/20 11:20");
		super.fillInputBoxIn("endPeriod", "2023/02/22 11:25");
		super.fillInputBoxIn("mark", "A"); 	

		super.clickOnSubmit("Create");
		
		super.clickOnButton("Auditing records");

		super.checkColumnHasValue(0, 0, "AAAAAAAAA");
		super.clickOnListingRecord(0);

		super.clickOnSubmit("Delete");
		
		super.checkColumnHasValue(0, 0, "BBBBBBBBBB");

		super.signOut();

	}

	
	@Test
	public void test300Hacking() {
		
		AuditingRecord ar;
		String param;

		ar = this.repository.findAuditingRecordBySubject("Science");
		param = String.format("id=%d", ar.getId());

		super.checkLinkExists("Sign in");
		super.request("/auditor/auditing-record/delete", param);
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/auditing-record/delete", param);
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/auditing-record/delete", param);
		super.checkPanicExists();
		super.signOut();
	}
	

}
