package matricula.data;

public class ImageLoader0209 {

	/**
	 * Lädt alle Seite aus Trauungsbuch 02-09.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {
		String url0209 = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761670&count=<<<>>>&key=046bee602dce9f5c014e32f10bfe69f6";
		String fileNameFormat0209 = "files-02-09/02-09-03-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url0209, fileNameFormat0209);
		main.loadAndSaveImages(4, 20, -2);
		main.loadAndSaveImages(21, 30, -2);
		main.loadAndSaveImages(31, 49, -2);
		main.loadAndSaveImages(51, 53, -3);
		main.loadAndSaveImages(54, 89, -4);
		main.loadAndSaveImages(91, 150, -5);
		main.loadAndSaveImages(152, 167, -6);
		main.loadAndSaveImages(170, 191, -8);
		main.loadAndSaveImages(193, 201, -9);
		main.loadAndSaveImages(203, 217, -10);
		main.loadAndSaveImages(219, 228, -11);

		System.out.println("Done.");
	}

}
