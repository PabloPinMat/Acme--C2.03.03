
package acme.testing.features.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class PeepListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/peep/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int peepIndex, final String title, final String message) {

		super.clickOnMenu("Any", "Peep");

		super.checkListingExists();

		super.sortListing(0, "asc");

		super.checkColumnHasValue(peepIndex, 0, title);
		super.checkColumnHasValue(peepIndex, 1, message);

	}

}
