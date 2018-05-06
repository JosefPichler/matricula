package matricula.data;

public class ImageLoader0208 {

	/**
	 * Lädt alle Seite aus Trauungsbuch 02-08.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {
		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761669&count=<<<>>>&key=34c8011f94feb5c4399dbed8281c43e4";
		loadEinband(url);
		loadTrauung(url);
		loadIndexTrauung(url);
		
		
		System.out.println("Done.");
	}

	private static void loadEinband(String url) {
		String fileNameFormat = "files-02-08/02-08-03-Einband_0000.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(235, 235, 9999);
	}

	private static void loadTrauung(String url) {
		String fileNameFormat = "files-02-08/02-08-03-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(0, 27, +1);
		main.loadAndSaveImages(29, 234, 0);
	}
	
	private static void loadIndexTrauung(String url) {
		String fileNameFormat = "files-02-08/02-08-03-Index-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(236, 256, -235);
	}

}
