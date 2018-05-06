package matricula.data;

public class PageEntryGenerator {

	public static void main(String[] args) {
		String format0102 = "page;01-Taufe-%04d;files-0123-02/0123-02-Taufe-%04d.jpg";
		PageEntryGenerator g = new PageEntryGenerator(format0102);
		g.print(158, 264);
	}

	private final String format;

	public PageEntryGenerator(String format) {
		this.format = format;
	}

	private void print(int fromIndex, int toIndex) {
		for (int i = fromIndex; i <= toIndex; i += 2) {
			String entry = String.format(format, i, i);
			System.out.println(entry);
		}
	}
}
