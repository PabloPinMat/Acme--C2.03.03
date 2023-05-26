
package acme.testing.features.auditor.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditShowTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;
	
	
	@Test
	public void test300Hacking() {

		Audit audit;
		String param;

		audit = this.repository.findAuditByCode("A2345");
		param = String.format("auditId=%d", audit.getId());
		
		super.checkLinkExists("Sign in");
		super.request("/auditor/audit/show", param);
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/audit/show", param);
		super.checkPanicExists();
		super.signOut();
		
		super.signIn("auditor1", "auditor1");
		super.request("/auditor/audit/show", param);
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/audit/show", param);
		super.checkPanicExists();
		super.signOut();
	}
	

}
