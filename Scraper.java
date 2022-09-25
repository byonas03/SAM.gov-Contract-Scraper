import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Scraper {
	int size;

	String[] splices;
	LinkedList<Entry> entries = new LinkedList<>();
	LinkedList<String> titles = new LinkedList<>();
	HashMap<Entry, Integer> entry_index_map = new HashMap<>();

	public Scraper(String content, int s) {
		splices = getSplices(content, false);
		titles = getTitles();
		size = s;
		sortByValue(entry_index_map);
	}

	public Entry getEntry(int index) {
		Entry e = entries.get(index);
		int end = splices.length;
		if (index + 1 < size)
			end = entry_index_map.get(entries.get(index + 1));
		boolean found_sn = false, found_dept = false, found_subtier = false, found_office = false,
				found_publisheddate = false, found_updateddate = false, found_activationdate = false,
				found_responsedate = false, found_noticetype = false, found_terminationdate = false,
				found_classification = false, found_samnumber = false, found_dunsnumber = false, found_cagecode = false,
				found_physicaladdress = false;
		for (int i = entry_index_map.get(e); i < end; i++) {
			if (!found_sn && splices[i].equals("solicitationNumber")) {
				e.setNoticeID(splices[i + 2]);
				found_sn = true;
			}
			if (!found_dept && splices[i].equals(":1,")) {
				e.setDept(splices[i + 3]);
				found_dept = true;
			}
			if (!found_subtier && splices[i].equals(":2,")) {
				e.setSubtier(splices[i + 3]);
				found_subtier = true;
			}
			if (!found_office && splices[i].equals(":3,")) {
				e.setOffice(splices[i + 3]);
				found_office = true;
			}
			if (!found_updateddate && splices[i].equals("modifiedDate")) {
				e.setUpdatedDate(splices[i + 2]);
				found_updateddate = true;
			}
			if (!found_activationdate && splices[i].equals("activationDate")) {
				e.setActivationDate(splices[i + 2]);
				found_activationdate = true;
			}
			if (!found_responsedate && splices[i].equals("responseDateActual")) {
				e.setResponseDate(splices[i + 2]);
				found_responsedate = true;
			}
			if (!found_noticetype && splices[i].equals("value")) {
				e.setNoticeType(splices[i + 2]);
				found_noticetype = true;
			}
			if (!found_terminationdate && splices[i].equals("terminationDate")) {
				e.setTerminationDate(splices[i + 2]);
				found_terminationdate = true;
			}
			if (!found_classification && splices[i].equals("classification")) {
				e.setClassification(splices[i + 4]);
				found_classification = true;
			}
			if (!found_samnumber && splices[i].equals("samNumber")) {
				e.setSamNumber(splices[i + 2]);
				found_samnumber = true;
			}
			if (!found_dunsnumber && splices[i].equals("dunsNumber")) {
				e.setDunsNumber(splices[i + 2]);
				found_dunsnumber = true;
			}
			if (!found_cagecode && splices[i].equals("cageCode")) {
				e.setCageCode(splices[i + 2]);
				found_cagecode = true;
			}
			/*
			 * if (!found_physicaladdress && splices[i].equals("physicalAddress")) {
			 * e.setPhysicalAddress(splices[i + 2]); found_physicaladdress = true; }
			 */
		}
		int start = 0;
		if (index > 0)
			start = entry_index_map.get(entries.get(index - 1));
		for (int i = start; i < entry_index_map.get(e); i++) {
			if (!found_publisheddate && splices[i].equals("publishDate")) {
				e.setPublishedDate(splices[i + 2]);
				found_publisheddate = true;
			}
		}
		return e;
	}

	public String[] getSplices(String content, boolean erase) {
		String[] ret = content.split("\"");
		return ret;
	}

	public LinkedList<String> getTitles() {
		LinkedList<String> titles = new LinkedList<>();
		for (int i = 0; i < splices.length; i++) {
			if (splices[i].equals("title") && (i == 0 || splices[i - 1].equals(":true,"))
					&& (i == splices.length - 1 || splices[i + 1].equals(":"))) {
				titles.add(splices[i + 2]);
				Entry e = new Entry();
				e.setTitle(splices[i + 2]);
				entry_index_map.put(e, i + 2);
				entries.add(e);
			}
		}
		return titles;
	}

	public static HashMap<Entry, Integer> sortByValue(HashMap<Entry, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<Entry, Integer>> list = new LinkedList<Map.Entry<Entry, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Entry, Integer>>() {
			public int compare(Map.Entry<Entry, Integer> o1, Map.Entry<Entry, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<Entry, Integer> temp = new LinkedHashMap<Entry, Integer>();
		for (Map.Entry<Entry, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}
}