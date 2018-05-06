package matricula.maps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GeoLocations {

	public static final GeoLocations Local = new GeoLocations();

	private Map<String, GeoLocation> cache;

	public GeoLocations() {
		this.cache = new HashMap<String, GeoLocation>();
		initFrom("locs");
		initFrom("locs/orte");
	}

	@Override
	public String toString() {
		return "cache size = " + cache.size();
	}

	private void initFrom(String directory) {

		File root = new File(directory);
		for (File e : root.listFiles()) {
			if (e.getName().endsWith(".xml")) {
				GeoLocation loc = loadGeoLocation(e);
				if (loc != null) {
					cache.put(loc.getStreet(), loc);
				}
			}
		}
	}

	private GeoLocation loadGeoLocation(File xmlFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		Document doc = null;
		try {
			doc = builder.parse(xmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		List<GeoLocation> resultCandidateList = new ArrayList<GeoLocation>();

		NodeList resultList = doc.getElementsByTagName("result");
		for (int i = 0; i < resultList.getLength(); i++) {
			GeoLocation loc = parseResult(resultList.item(i));
			resultCandidateList.add(loc);
		}

		Collections.sort(resultCandidateList, new Comparator<GeoLocation>() {

			@Override
			public int compare(GeoLocation o1, GeoLocation o2) {
				if (GeoLocation.TypeRoofTop.equals(o1.getLocationType())) {
					return -1;
				} else {
					return 1;
				}
			}

		});

		return resultCandidateList.get(0);

	}

	private GeoLocation parseResult(Node item) {
		NodeList children = item.getChildNodes();
		String streetNumber = null;
		String streetName = null;
		String political = null;
		String locationType = null;
		double lat = Double.NaN;
		double lng = Double.NaN;
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if ("address_component".equals(n.getNodeName())) {
				String type = null;
				String longName = null;
				NodeList nChildren = n.getChildNodes();
				for (int j = 0; j < nChildren.getLength(); j++) {
					// <type>street_number</type>
					Node m = nChildren.item(j);
					if ("type".equals(m.getNodeName())) {
						type = m.getTextContent();
					} else if ("long_name".equals(m.getNodeName())) {
						longName = m.getTextContent();
					}
				}

				if ("street_number".equals(type)) {
					streetNumber = longName;
				} else if ("route".equals(type)) {
					streetName = longName;
				} else if ("political".equals(type) && political == null) {
					political = longName;
				}

			} else if ("geometry".equals(n.getNodeName())) {
				NodeList nChildren = n.getChildNodes();
				for (int j = 0; j < nChildren.getLength(); j++) {
					Node m = nChildren.item(j);
					if ("location".equals(m.getNodeName())) {

						NodeList locationChildNodes = m.getChildNodes();
						for (int k = 0; k < locationChildNodes.getLength(); k++) {
							Node lNode = locationChildNodes.item(k);
							if ("lat".equals(lNode.getNodeName())) {
								lat = Double.parseDouble(lNode.getTextContent());
							} else if ("lng".equals(lNode.getNodeName())) {
								lng = Double.parseDouble(lNode.getTextContent());
							}
						}

					} else if ("location_type".equals(m.getNodeName())) {
						locationType = m.getTextContent();
					}
				}
			}
		}

		// Nur Ortsname wie "Gaflenz".
		if (streetName == null && streetNumber == null && political != null) {
			return new GeoLocation(political, "", lat, lng, locationType);
		}
		return new GeoLocation(streetName, streetNumber, lat, lng, locationType);
	}

	public List<GeoLocation> getAllLocations() {
		List<GeoLocation> result = new ArrayList<GeoLocation>();
		result.addAll(cache.values());
		return result;
	}

	public GeoLocation find(String address) {
		if (cache.containsKey(address)) {
			return cache.get(address);
		}
		return null;
	}
}
