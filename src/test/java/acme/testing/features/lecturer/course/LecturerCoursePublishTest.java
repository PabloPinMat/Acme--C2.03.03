
package acme.testing.features.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerCoursePublishTest extends TestHarness {

	@Autowired
	protected LecturerCourseTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/course/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseIndex) {

		super.signIn("lecturer1", "lecturer1");
		super.clickOnMenu("Teacher", "List Courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/features/lecturer/course/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int courseIndex) {

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Teacher", "List Courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		final Collection<Course> courses;
		String params;

		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				params = String.format("id=%d", course.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/course/publish", params);
				super.checkPanicExists();
				super.signOut();
			}

	}

	@Test
	public void test301Hacking() {

		Collection<Course> courses;
		String params;

		super.signIn("lecturer1", "lecturer1");
		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (!course.isDraftMode()) {
				params = String.format("id=%d", course.getId());
				super.request("/lecturer/course/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {

		Collection<Course> courses;
		String params;

		super.signIn("lecturer2", "lecturer2");
		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses) {
			params = String.format("id=%d", course.getId());
			super.request("/lecturer/course/publish", params);
		}
		super.signOut();
	}

}
