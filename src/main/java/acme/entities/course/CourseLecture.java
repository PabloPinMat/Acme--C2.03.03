
package acme.entities.course;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.entities.lecture.Lecture;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CourseLecture extends AbstractEntity {

	//	Serialisation identifier ----------------------------
	protected static final long	serialVersionUID	= 1L;

	//	Relationships ---------------------------------------
	@NotNull
	@ManyToOne(optional = false)
	@Valid
	protected Course			course;

	@NotNull
	@ManyToOne(optional = false)
	@Valid
	protected Lecture			lecture;

}
