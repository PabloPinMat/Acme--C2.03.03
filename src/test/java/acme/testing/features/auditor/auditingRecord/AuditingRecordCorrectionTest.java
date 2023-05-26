
package acme.testing.features.auditor.auditingRecord;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AuditingRecordCorrectionTest extends TestHarness {

	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/auditingRecord/correction-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final String subject, final String assessment, final String link, final String startHour, final String finishHour, final String mark, final String confirmation) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");
		
		super.clickOnListingRecord(1);
		
		super.clickOnButton("Add correction auditing record");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("startPeriod", startHour);
		super.fillInputBoxIn("endPeriod", finishHour);
		super.fillInputBoxIn("mark", mark); 	
		super.fillInputBoxIn("confirmation", confirmation);
		
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
	
	
	@ParameterizedTest
	@CsvFileSource(resources = "/features/auditor/auditingRecord/correction-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final String subject, final String assessment, final String link, final String startHour, final String finishHour, final String mark, final String confirmation) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");
		
		super.clickOnListingRecord(1);
		
		super.clickOnButton("Add correction auditing record");

		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("assessment", assessment);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("startPeriod", startHour);
		super.fillInputBoxIn("endPeriod", finishHour);
		super.fillInputBoxIn("mark", mark); 	
		super.fillInputBoxIn("confirmation", confirmation);
		
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}
	

}
