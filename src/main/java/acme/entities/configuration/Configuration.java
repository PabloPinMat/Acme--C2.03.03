
package acme.entities.configuration;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Configuration extends AbstractEntity {

	@NotBlank
	@Pattern(regexp = "^([A-Z]{3}(,\\b|\\Z))+$", message = "{validation.configuration.code2}")
	protected String acceptedCurrencies;
}
