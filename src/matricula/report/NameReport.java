package matricula.report;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import matricula.Community;
import matricula.model.Matricula;
import matricula.model.PersonTag;
import matricula.model.RegisterPage;
import matricula.model.TagRegion;

public class NameReport {

	private final List<PersonTag> personList;

	public NameReport(List<PersonTag> personList) {
		this.personList = personList;
	}

	public static void main(String[] args) {
		Matricula m = Matricula.loadRegisters(Community.Hollenstein.registerFileNames());
		List<PersonTag> personList = m.getPersonList();
		personList = unique(personList);
		NameReport report = new NameReport(personList);

		StringFrequency firstNameFreq = report.createFrequency(new PersonTagKeyBuilder() {
			@Override
			public String makeKey(PersonTag p) {
				return p.getFirstName();
			}
		});
		System.out.println(firstNameFreq);

		StringFrequency lastNameFreq = report.createFrequency(new PersonTagKeyBuilder() {
			@Override
			public String makeKey(PersonTag p) {
				return p.getFamilyName();
			}
		});
		System.out.println(lastNameFreq);

		List<PersonTag> josefList = filter(personList, new PersonTagFilter() {

			@Override
			public boolean accept(PersonTag tag) {
				return "Katharina".equals(tag.getFirstName());
			}

		});

		report.generateImages("c:/temp/Katharina/", josefList);

	}

	private void generateImages(String outDir, List<PersonTag> pList) {
		int count = 1;
		String prefix = "Katharina-";
		for (PersonTag e : pList) {
			TagRegion r = e.getRegion();
			RegisterPage page = e.getRegisterPage();
			BufferedImage pageImage = null;
			try {
				pageImage = ImageIO.read(new File(page.getImageURL()));
			} catch (IOException e1) {
				System.out.println("Cannot load image " + page.getImageURL());
			}
			if (pageImage != null) {
				String outFileName = prefix + count + ".jpg";
				BufferedImage outImage = pageImage.getSubimage(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				try {
					ImageIO.write(outImage, "png", new File(outDir + outFileName));
				} catch (IOException e1) {
					System.out.println("Cannot write image " + outDir + outFileName);
				}
				count++;
				System.out.println(outFileName);
			}
		}
	}

	private static List<PersonTag> filter(List<PersonTag> pList, PersonTagFilter pFilter) {
		List<PersonTag> result = new ArrayList<PersonTag>();
		for (PersonTag e : pList) {
			if (pFilter.accept(e)) {
				result.add(e);
			}

		}
		return result;
	}

	private static List<PersonTag> unique(List<PersonTag> list) {
		Map<String, PersonTag> table = new HashMap<String, PersonTag>();

		for (PersonTag e : list) {
			if (e.getFullName() != null && e.getDateOfBirth() != null && e.getPlaceOfBirth() != null) {
				String key = e.getFullName() + e.getDateOfBirth() + e.getPlaceOfBirth();
				if (!table.containsKey(key)) {
					table.put(key, e);
				}
			}
		}

		List<PersonTag> result = new ArrayList<PersonTag>();
		result.addAll(table.values());
		return result;
	}

	private StringFrequency createFrequency(PersonTagKeyBuilder keyBuilder) {
		StringFrequency result = new StringFrequency();

		for (PersonTag p : personList) {
			String key = keyBuilder.makeKey(p);
			result.add(key);
		}
		return result;
	}
}
