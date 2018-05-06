package matricula.data;

public class ImageLoader0207 {

	/**
	 * Lädt alle Seite aus Trauungsbuch 02-07.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {
		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761668&count=<<<>>>&sess_id=8c56bc4ce6fbdbc193cffb29aab3e82e&key=72f0d6650eb104514539a8024b876c5a";
		loadIndexTrauung(url);
		loadTrauung(url);

		System.out.println("Done.");
	}

	private static void loadIndexTrauung(String url) {
		String fileNameFormat = "files-02-07/02-07-Index-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(1, 22, 0);
	}

	private static void loadTrauung(String url) {
		String fileNameFormat = "files-02-07/02-07-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(23, 309, -22);
	}

}
