
package acme.testing.features.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;

public class LecturerLecturePublishTest extends TestHarness {

	@Autowired
	protected LectureLecturerTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/lecture/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int lectureIndex) {

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Teacher", "My lectures");
		super.checkListingExists();

		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/lecture/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Negative(final int lectureIndex, final String title, final String abstractt, final String estimatedLearningTime, final String body, final String lectureType, final String furtherInformation) {

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Teacher", "My lectures");
		super.checkListingExists();

		super.clickOnListingRecord(lectureIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractt", abstractt);
		super.fillInputBoxIn("estimatedLearningTime", estimatedLearningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("lectureType", lectureType);
		super.fillInputBoxIn("furtherInformation", furtherInformation);

		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}
	@Test
	public void test300Hacking() {

		Collection<Lecture> lectures;
		String params;

		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures)
			if (lecture.isDraftMode()) {
				params = String.format("id=%d", lecture.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/lecture/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/lecture/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/lecture/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test302Hacking() {

		Collection<Lecture> lectures;
		String params;

		super.signIn("lecturer2", "lecturer2");
		lectures = this.repository.findManyLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures) {
			params = String.format("id=%d", lecture.getId());
			super.request("/lecturer/lecture/publish", params);
		}
		super.signOut();
	}
}
