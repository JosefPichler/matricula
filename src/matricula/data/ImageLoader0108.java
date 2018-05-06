package matricula.data;

public class ImageLoader0108 {

	/**
	 * Lädt alle Bilder aus dem Buch 01-08 (1801 - 1813) von der Quelle
	 * www.data.matricula.info.
	 * 
	 * 
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761661&count=<<<>>>&key=07e59f9b23b17dd06a533a62ccfb49b3";
		loadEinband(url);
		loadIndexTaufe(url);
		loadTaufe(url);
		System.out.println("Done.");
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-01-08/01-08-Einband_0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 0, -4711);
	}

	private static void loadIndexTaufe(String url) {
		String fileNameFormat = "files-01-08/01-08-Index-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(1, 35, 0);
	}

	private static void loadTaufe(String url) {
		String fileNameFormat = "files-01-08/01-08-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(36, 189, -35);
	}

}
