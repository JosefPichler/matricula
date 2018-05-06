package matricula.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringFrequency {

	private final Integer one = new Integer(1);

	private int total;
	private Map<String, Integer> freq;

	public StringFrequency() {
		this.freq = new HashMap<String, Integer>();
	}

	public void add(String key) {
		if (freq.containsKey(key)) {
			int oldValue = freq.get(key);
			Integer newValue = new Integer(1 + oldValue);
			freq.put(key, newValue);
		} else {
			freq.put(key, one);
		}
	}

	@Override
	public String toString() {
		freq.remove(null);
		makeTotal();
		StringBuilder result = new StringBuilder();
		List<String> keyList = new ArrayList<String>();
		keyList.addAll(freq.keySet());

		Collections.sort(keyList, new Comparator<String>() {

			@Override
			public int compare(String a, String b) {
				int fa = freq.get(a);
				int fb = freq.get(b);
				return fb - fa;
			}

		});

		for (String e : keyList) {
			int count = freq.get(e);
			double p = percentage(count);

			String line = String.format("%3dx  %.1f  %s", count, p, e);
			result.append(line);
			result.append("\n");
		}

		return result.toString();
	}

	private void makeTotal() {
		total = 0;
		for (Integer e : freq.values()) {
			total += e;
		}
	}

	private double percentage(int absoluteValue) {
		double result = 100.0 * (double) absoluteValue / (double) total;
		return result;
	}

	public int getCount(String key) {
		if (freq.containsKey(key)) {
			return freq.get(key);
		}
		return 0;
	}
}
