
package acme.testing.features.auditor.auditingRecord;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;
import acme.testing.features.auditor.audit.AuditTestRepository;

public class AuditingRecordCreateTest extends TestHarness {

	@Autowired
	protected AuditTestRepository repository;
	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/auditingRecord/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final String subject, final String assessment, final String link, final String startHour, final String finishHour, final String mark) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");
		
		super.clickOnListingRecord(0);
		
		super.clickOnButton("Create auditing record");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("startPeriod", startHour);
		super.fillInputBoxIn("endPeriod", finishHour);
		super.fillInputBoxIn("mark", mark); 	

		super.clickOnSubmit("Create");
		
		super.clickOnButton("Auditing records");

		super.checkColumnHasValue(0, 0, subject);

		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assessment", assessment);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("startPeriod", startHour);
		super.checkInputBoxHasValue("endPeriod", finishHour);
		super.checkInputBoxHasValue("mark", mark);

		super.signOut();

	}
	
	/*
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/auditingRecord/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final String subject, final String assessment, final String link, final String startHour, final String finishHour, final String mark) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");
		
		super.clickOnListingRecord(0);
		
		super.clickOnButton("Create auditing record");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("startPeriod", startHour);
		super.fillInputBoxIn("endPeriod", finishHour);
		super.fillInputBoxIn("mark", mark); 	

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}
	
	*/
	@Test
	public void test300Hacking() {
		
		Audit audit;
		String param;

		audit = this.repository.findAuditByCode("X2345");
		param = String.format("auditId=%d", audit.getId());

		super.checkLinkExists("Sign in");
		super.request("/auditor/auditing-record/create", param);
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/auditor/auditing-record/create", param);
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/auditor/auditing-record/create", param);
		super.checkPanicExists();
		super.signOut();
	}
	

}
