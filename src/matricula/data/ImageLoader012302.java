package matricula.data;

public class ImageLoader012302 {

	/**
	 * Lädt alle Bilder aus dem Buch 0123-02 (1649 - 1691) von der Quelle
	 * www.data.matricula.info.
	 * 
	 * 
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761657&count=<<<>>>&key=bfc7c89e01d82fa4b583717e4bd04fd6";
		loadTaufe(url);
		loadTrauung(url);
		loadEinband(url);
		loadIndexTaufe(url);
		loadIndexTrauungTod(url);
		loadTod(url);
		System.out.println("Done.");
	}

	private static void loadTaufe(String url) {
		String fileNameFormat = "files-0123-02/0123-02-Taufe-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 0, 1);
		main.loadAndSaveImagesDoublePage(1, 9, 2, 0);
		main.loadAndSaveImagesDoublePage(10, 28, 2, 0, -1);
		main.loadAndSaveImagesDoublePage(29, 36, 2, -1);
		main.loadAndSaveImagesDoublePage(37, 80, 2, -1, -1);
		main.loadAndSaveImagesDoublePage(81, 93, 2, -2);
		main.loadAndSaveImagesDoublePage(94, 94, 2, -2, -1);
		main.loadAndSaveImagesDoublePage(95, 136, 2, -3);
	}

	private static void loadTrauung(String url) {
		String fileNameFormat = "files-0123-02/0123-02-Trauung-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImagesDoublePage(137, 202, 2, -136, -1);
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-0123-02/0123-02-Einband-0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImagesDoublePage(203, 203, 0, 0);
	}

	private static void loadIndexTaufe(String url) {
		String fileNameFormat = "files-0123-02/0123-02-Index-Taufe-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(204, 287, -203);
	}

	private static void loadIndexTrauungTod(String url) {
		String fileNameFormat = "files-0123-02/0123-02-Index-TrauungTod-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(288, 341, -287);
	}

	private static void loadTod(String url) {
		String fileNameFormat = "files-0123-02/0123-02-Tod-%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(342, 382, -341);
	}
}
