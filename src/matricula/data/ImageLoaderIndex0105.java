package matricula.data;

public class ImageLoaderIndex0105 {

	/**
	 * Lädt alle Seite des Taufindex aus Buch 01-05.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {
		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761658&count=<<<>>>&key=665d27145f2a227357bb96f29e0ed900";
		String fileNameFormat = "files-0123-05/01-05-Index-Taufe_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages(180, 207, -179);

		System.out.println("Done.");
	}

}
