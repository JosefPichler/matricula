package matricula.data;

public class ImageLoader0107 {

	/**
	 * Lädt alle Bilder aus dem Buch 01-07 (1799 - 1813) von der Quelle
	 * www.data.matricula.info.
	 * 
	 * 
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761660&count=<<<>>>&key=8195a671c55d0a5cb28cbb44140ddc47";
		loadTaufe(url);
		loadEinband(url);
		loadIndexTaufe(url);
		System.out.println("Done.");
	}

	private static void loadTaufe(String url) {
		String fileNameFormat = "files-01-07/01-07-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 137, 1);
		main.loadAndSaveImages(138, 157, 0);
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-01-07/01-07-Einband_0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(158, 158, -4711);
	}

	private static void loadIndexTaufe(String url) {
		String fileNameFormat = "files-01-07/01-07-Index-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(159, 193, -158);
	}
}
