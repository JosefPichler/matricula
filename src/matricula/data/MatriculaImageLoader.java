package matricula.data;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

// Do not run this code.
public class MatriculaImageLoader {

	private final String outDirectory;

	public MatriculaImageLoader(String outDirectory) {
		this.outDirectory = outDirectory;
	}

	public static void main(String[] args) {
		MatriculaImageLoader loader = new MatriculaImageLoader(("files"));
		loader.load0210Trauung(2);
	}

	private void load0210Trauung(int pageNr) {
		final int count = pageNr + 3;
		final String fileName = String.format("02-10-03-Trauung_%04d.jpg", pageNr);
		final String url = RegisterURL.getUrlWithCount(count);
		System.out.println("fileName> " + fileName);
		System.out.println("url> " + url);

		try {
			saveUrl(fileName, url);
			load(outDirectory + "/" + fileName, url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void load(String fileName, String url) throws IOException {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(fileName);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	}

	public void saveUrl(final String filename, final String urlString) throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
	}
}
