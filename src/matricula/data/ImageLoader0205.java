package matricula.data;

public class ImageLoader0205 {

	/**
	 * Lädt alle Seite aus Trauungsbuch 02-05.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {
		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761658&count=<<<>>>&key=8ef5ad4afc20f26f4ffa88bb9ff3072b";
		String fileNameFormat = "files-0123-05/0123-05-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);

		// Page 1
		main.loadAndSaveImages(121, 121, -120); // 1
		main.loadAndSaveImages(122, 122, -120); // 2
		main.loadAndSaveImages(123, 123, -119); // 4
		main.loadAndSaveImages(124, 124, -118); // 6

		for (int i = 0; i < 26; i++) {
			main.loadAndSaveImages(124 + i, 124 + i, -118 + i);
		}
		for (int i = 26; i < 45; i++) {
			main.loadAndSaveImages(124 + i, 124 + i, -119 + i);
		}

		main.loadAndSaveImages(168, 168, -75);
		main.loadAndSaveImages(169, 169, -74);
		main.loadAndSaveImages(170, 170, -73);
		main.loadAndSaveImages(171, 171, -72);
		main.loadAndSaveImages(172, 178, -71);

		System.out.println("Done.");
	}

}
