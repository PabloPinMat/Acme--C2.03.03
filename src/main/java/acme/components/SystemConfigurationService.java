
package acme.components;

import org.springframework.stereotype.Service;

@Service
public class SystemConfigurationService {

	public String booleanTranslated(final boolean bool, final String lang) {
		String res = "";
		if (lang.equals("en"))
			res = bool ? "Yes" : "No";
		else if (lang.equals("es"))
			res = bool ? "Si" : "No";
		return res;
	}
}
