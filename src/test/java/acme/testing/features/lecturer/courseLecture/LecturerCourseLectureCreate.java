
package acme.testing.features.lecturer.courseLecture;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerCourseLectureCreate extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/courseLecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final Integer lectureIndex, final String course) {

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Teacher", "My lectures");

		super.checkListingExists();

		super.clickOnListingRecord(lectureIndex);

		super.clickOnButton("Add lecture to a course");

		super.fillInputBoxIn("course", course);

		super.clickOnSubmit("Create");

		super.checkNotErrorsExist();

		super.checkFormExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/courseLecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final Integer lectureIndex, final String course) {

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Teacher", "My lectures");

		super.checkListingExists();

		super.clickOnListingRecord(lectureIndex);

		super.clickOnButton("Add lecture to a course");

		super.fillInputBoxIn("course", course);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.checkFormExists();

		super.signOut();
	}
}
