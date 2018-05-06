package matricula.report;

public class Toggle {

	private String lastKey;
	private int index;
	private final String[] values;

	public Toggle(String[] values) {
		this.values = values;
		this.index = 0;
	}

	public String getValue(String key) {
		if (!key.equals(lastKey)) {
			index = (index + 1) % values.length;
		}
		lastKey = key;

		return values[index];
	}

}
