package matricula.maps;

public class JavaScript {
	public static String encode(double value) {
		String str = Double.toString(value);
		String result = str.replace(',', '.');
		return result;
	}

	public static String encode(String str) {
		String result = str.replace(" ", "X");
		result = result.replace("/", "X");
		result = result.replace(".", "X");
		result = result.replace("-", "X");
		result = result.replace("ö", "oe");
		result = result.replace("ü", "ue");
		return result;

	}
}
