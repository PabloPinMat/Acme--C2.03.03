
package acme.testing.features.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class PeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/peep/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int peepIndex, final String instantiationMoment, final String title, final String nick, final String message, final String email, final String link) {

		super.clickOnMenu("Any", "Peep");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnButton("Create");

		super.fillInputBoxIn("instantiationMoment", instantiationMoment);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.clickOnMenu("Any", "Peep");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(peepIndex, 0, title);
		super.checkColumnHasValue(peepIndex, 1, message);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int peepIndex, final String instantiationMoment, final String title, final String nick, final String message, final String email, final String link) {

		super.clickOnMenu("Any", "Peep");

		super.checkListingExists();

		super.sortListing(0, "asc");

		super.clickOnButton("Create");

		super.fillInputBoxIn("instantiationMoment", instantiationMoment);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("email", email);
		super.fillInputBoxIn("link", link);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

	}

}
