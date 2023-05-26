
package acme.testing.features.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class PeepShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/peep/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int peepIndex, final String instantiationMoment, final String title, final String nick, final String message, final String email, final String link) {

		super.clickOnMenu("Any", "Peep");

		super.checkListingExists();

		super.sortListing(0, "asc");

		super.clickOnListingRecord(peepIndex);

		super.checkInputBoxHasValue("instantiationMoment", instantiationMoment);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);
	}

}
