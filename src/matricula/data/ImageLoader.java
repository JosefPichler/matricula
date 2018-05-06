package matricula.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ImageLoader {

	private final String urlWithPlacholder;
	private final String fileNameFormat;

	public ImageLoader(String urlWithPlacholder, String fileNameFormat) {
		this.urlWithPlacholder = urlWithPlacholder;
		this.fileNameFormat = fileNameFormat;
	}

	public void loadAndSaveImages(int fromCount, int toCount, int pageDelta) {
		for (int i = fromCount; i <= toCount; i++) {
			String imageUrl = urlWithPlacholder.replace("<<<>>>", "" + i);
			String file = String.format(fileNameFormat, i + pageDelta);
			File f = new File(file);
			if (f.exists()) {
				System.out.println(file + " ... already exists.");
			} else {
				System.out.print(file + " ...");
				loadAndSaveImage(imageUrl, file);
				System.out.println(" loaded.");
			}
		}
	}

	public void loadAndSaveImages2(int fromCount, int toCount, int increment, int pageDelta) {
		for (int i = fromCount; i <= toCount; i++) {
			String imageUrl = urlWithPlacholder.replace("<<<>>>", "" + i);
			int pageNr = i * increment + pageDelta;
			String file = String.format(fileNameFormat, pageNr);
			File f = new File(file);
			if (f.exists()) {
				System.out.println(file + " ... already exists.");
			} else {
				System.out.print(file + " ...");
				loadAndSaveImage(imageUrl, file);
				System.out.println(" loaded.");
			}
		}
	}

	/**
	 * Lädt doppelseitige Bilder.
	 * 
	 * @param fromCount Startindex in URL.
	 * @param toCount Endindex in URL
	 * @param multiplier Wert 2 für doppelseitig
	 * @param pageDelta Delta zwischen Index in URL und Nummer der Seite.
	 */
	public void loadAndSaveImagesDoublePage(int fromCount, int toCount, int multiplier, int pageDelta, int increment) {
		for (int i = fromCount; i <= toCount; i++) {
			String imageUrl = urlWithPlacholder.replace("<<<>>>", "" + i);
			int pageNr = (i +pageDelta) * multiplier + increment;
			String file = String.format(fileNameFormat, pageNr);
			File f = new File(file);
			if (f.exists()) {
				System.out.println(file + " ... already exists.");
			} else {
				System.out.print(file + " ...");
				loadAndSaveImage(imageUrl, file);
				System.out.println(" loaded.");
			}
		}
	}
	/**
	 * Lädt doppelseitige Bilder.
	 * 
	 * @param fromCount Startindex in URL.
	 * @param toCount Endindex in URL
	 * @param multiplier Wert 2 für doppelseitig
	 * @param pageDelta Delta zwischen Index in URL und Nummer der Seite.
	 */
	public void loadAndSaveImagesDoublePage(int fromCount, int toCount, int multiplier, int pageDelta) {
		for (int i = fromCount; i <= toCount; i++) {
			String imageUrl = urlWithPlacholder.replace("<<<>>>", "" + i);
			int pageNr = (i +pageDelta) * multiplier;
			String file = String.format(fileNameFormat, pageNr);
			File f = new File(file);
			if (f.exists()) {
				System.out.println(file + " ... already exists.");
			} else {
				System.out.print(file + " ...");
				loadAndSaveImage(imageUrl, file);
				System.out.println(" loaded.");
			}
		}
	}
	public void loadAndSaveImage(String imgPath, String imgFilePath) {
		URL imgUrl;
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		try {
			imgUrl = new URL(imgPath);
			rbc = Channels.newChannel(imgUrl.openStream());
			fos = new FileOutputStream(imgFilePath);
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (rbc != null) {
				try {
					rbc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
