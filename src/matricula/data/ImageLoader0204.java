package matricula.data;

public class ImageLoader0204 {

	/**
	 * Lädt alle Seiten 1- 86 aus Trauungsbuch 02-04.
	 * 
	 * @param args
	 *            keine Verwendung
	 */
	public static void main(String[] args) {

		// Seite 1
		// Trauungsbuch:http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761684&count=114&key=d1b7ea5f85df8e7a171fde343e6056f8

		String url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761684&count=<<<>>>&key=b1cd6cc61057b5ccd9d5d5a4d729e660";
		String fileNameFormat = "files-0123-04/0123-04-Trauung_%04d.jpg";

		ImageLoader main = new ImageLoader(url, fileNameFormat);

		// Einband
		main.loadAndSaveImages(0, 0, 0);

		// Seiten 1 - 86
		main.loadAndSaveImages(114, 115, -113);
		for (int i = 0; i < 42; i++) {
			main.loadAndSaveImages(116 + i, 116 + i, -112 + i);
		}

		System.out.println("Done.");
	}

}
