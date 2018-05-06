package matricula.data;

public class ImageLoader0109 {

	/**
	 * Lädt alle Seite aus Taufbuch 01-09.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {
		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761662&count=<<<>>>&sess_id=696662d31c3bc9e8a61c747d434e72ce&key=4176827135d86bb1867d4ba04cdec7c9";
		String fileNameFormat = "files-01-09/01-09-Taufe_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);
		main.loadAndSaveImages2(0, 181, 2, 2);
		main.loadAndSaveImages2(182, 367, 2, 4);
		System.out.println("Done.");
	}

}
