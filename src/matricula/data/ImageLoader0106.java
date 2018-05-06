package matricula.data;

public class ImageLoader0106 {

	/**
	 * Lädt alle Bilder aus dem Buch 01-06 (1784 - 1801) von der Quelle
	 * www.data.matricula.info.
	 * 
	 * 
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761659&count=<<<>>>&key=e0bcb58f3f13c4b90b7ac8379d489840";
		loadTaufe(url);
		loadEinband(url);
		System.out.println("Done.");
	}

	private static void loadTaufe(String url) {
		String fileNameFormat = "files-01-06/01-06-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 103, 1);
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-01-06/01-06-Einband_0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(104, 104, -4711);
	}

}
