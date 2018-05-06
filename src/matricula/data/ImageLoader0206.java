package matricula.data;

public class ImageLoader0206 {

	/**
	 * Lädt alle Bilder aus dem Buch 02-06 (1784 - 1815) von der Quelle
	 * www.data.matricula.info.
	 * 
	 * 
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761667&count=<<<>>>&key=fb2a828b5e386da5f473bf90870eb96a";
		loadEinband(url);
		loadTaufe(url);
		System.out.println("Done.");
	}

	private static void loadTaufe(String url) {
		String fileNameFormat = "files-02-06/02-06-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(1, 84, 0);
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-02-06/02-06-Einband_0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 0, -4711);
	}

}
