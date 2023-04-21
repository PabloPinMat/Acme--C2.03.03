
package acme.entities.enrolments;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.course.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}[0-9][0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			motivation;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	protected Boolean			finalised;

	@Length(max = 75)
	protected String			ccHolder;

	@Pattern(regexp = "\\d{4}")
	protected String			ccLowerNibble;

	// Derived attributes -----------------------------------------------------

	protected Double			workTime;

	// Relationships ----------------------------------------------------------

	@ManyToOne
	protected Course			course;

	@ManyToOne
	protected Student			student;

}
