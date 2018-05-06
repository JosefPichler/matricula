package matricula.data;

public class ImageLoader012301 {

	/**
	 * Lädt alle Bilder aus dem Buch 0123-01 (1613 - 1628) von der Quelle
	 * www.data.matricula.info.
	 * 
	 * 
	 * Einband:
	 * http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761656&count=0&key=9835c648ea5e3a157dcdfc9a8382ba38
	 * 
	 * Index Taufe 1:
	 * http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761656&count=1&key=bfc7c89e01d82fa4b583717e4bd04fd6
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761656&count=<<<>>>&key=9835c648ea5e3a157dcdfc9a8382ba38";
		loadEinband(url);
		loadIndexTaufe(url);
		loadTaufe(url);
		loadTod(url);
		loadTrauung(url);

		System.out.println("Done.");
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-0123-01/0123-01-Einband-0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 0, 0);
	}

	private static void loadIndexTaufe(String url) {
		String fileNameFormat = "files-0123-01/0123-01-Index-Taufe-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(1, 20, 0);
	}

	private static void loadTaufe(String url) {
		String fileNameFormat = "files-0123-01/0123-01-Taufe-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(21, 22, -20);
		main.loadAndSaveImagesDoublePage(22, 94, 2, -21);
	}

	private static void loadTod(String url) {
		String fileNameFormat = "files-0123-01/0123-01-Tod-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(95, 95, -94);
		main.loadAndSaveImagesDoublePage(96, 117, 2, -95);
	}

	private static void loadTrauung(String url) {
		String fileNameFormat = "files-0123-01/0123-01-Trauung-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(118, 118, -117);
		main.loadAndSaveImagesDoublePage(119, 137, 2, -118);
	}

}
