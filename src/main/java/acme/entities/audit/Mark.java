package acme.entities.audit;


public enum Mark {
	
	A_PLUS, A, B, C, F, F_MINUS;

	public static Mark transform(final String mark) {
		Mark result = null;
		for (final Mark i : Mark.values())
			if (i.toString().equals(mark))
				result = i;
		return result;
	}
}
