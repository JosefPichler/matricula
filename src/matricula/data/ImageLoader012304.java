package matricula.data;

public class ImageLoader012304 {

	/**
	 * Lädt Index (Taufe und Trauung) aus Buch 0123-04 (1746 - 1773, Quelle:
	 * www.data.matricula.info).
	 * 
	 * 
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761684&count=<<<>>>&key=d3a38b97123e725abee94711031588de";
		loadEinband(url);
		loadIndexTaufe(url);
		loadIndexTrauung(url);

		System.out.println("Done.");
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-0123-04/0123-04-Einband_0000.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 0, 666);
	}

	private static void loadIndexTaufe(String url) {
		String fileNameFormat = "files-0123-04/0123-04-Index-Taufe_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(213, 267, -212);
	}

	private static void loadIndexTrauung(String url) {
		String fileNameFormat = "files-0123-04/0123-04-Index-Trauung_%04d.jpg";
		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(268, 291, -267);
	}

}
