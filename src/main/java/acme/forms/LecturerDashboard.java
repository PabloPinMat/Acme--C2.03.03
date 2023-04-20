
package acme.forms;

import java.util.Collection;
import java.util.Map;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LecturerDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	Map<String, Integer>		lecturesByTypeMap;
	Double						averageLearningTimeOfLectures;
	Double						deviationLearningTimeOfLectures;
	Double						minimumLearningTimeOfLectures;
	Double						maximumLearningTimeOfLectures;
	Double						averageLearningTimeOfCourses;
	Double						deviationLearningTimeOfCourses;
	Double						minimumLearningTimeOfCourses;
	Double						maximumLearningTimeOfCourses;


	public void calculateDerivedAttributes(final Collection<Double> learningTimes) {
		this.averageCalculation(learningTimes);
		this.deviationCalculation(learningTimes);
		this.minimumCalculation(learningTimes);
		this.maximumCalculation(learningTimes);
	}

	public void averageCalculation(final Collection<Double> values) {
		double result = 0.0;
		if (!values.isEmpty()) {
			final Double total = values.stream().mapToDouble(Double::doubleValue).sum();
			result = total / values.size();
		}
		this.averageLearningTimeOfCourses = result;
	}

	public void deviationCalculation(final Collection<Double> values) {
		Double result = 0.0;
		Double aux = 0.0;
		if (!values.isEmpty()) {
			for (final Double value : values)
				aux = Math.pow(value - this.averageLearningTimeOfCourses, 2);
			result = Math.sqrt(aux / values.size());
		}
		this.deviationLearningTimeOfCourses = result;
	}

	public void minimumCalculation(final Collection<Double> values) {
		Double result = 0.0;
		if (!values.isEmpty())
			result = values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
		this.minimumLearningTimeOfCourses = result;
	}

	public void maximumCalculation(final Collection<Double> values) {
		Double result = 0.0;
		if (!values.isEmpty())
			result = values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
		this.maximumLearningTimeOfCourses = result;
	}
}
