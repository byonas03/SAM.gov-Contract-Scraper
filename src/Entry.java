import java.util.Arrays;

public class Entry implements Comparable {
	String pos = "abcdefghijklmnopqrstuvwxyz1234567890";
	String ID = "";
	// "title"
	String title = "";
	String noticeID = "";
	String department = "";
	String subtier = "";
	String office = "";
	String updateddate = "";
	String publisheddate = "";
	String activationdate = "";
	String terminationdate = "";
	String responsedate = "";
	String noticetype = "";
	String classification = "";
	String dunsnumber = "";
	String samnumber = "";
	String cagecode = "";
	String physicaladdress = "";

	public Entry() {
		for (int i = 0; i < 6; i++) {
			int n = (int) (Math.random() * pos.length());
			ID += pos.substring(n, n + 1);
		}
	}

	public void setTitle(String t) {
		title = t;
	}

	public void setNoticeID(String t) {
		noticeID = t;
	}

	public void setDept(String t) {
		department = t;
	}

	public void setSubtier(String t) {
		subtier = t;
	}

	public void setOffice(String t) {
		office = t;
	}

	public void setUpdatedDate(String t) {
		if (t.contains("T"))
			updateddate = t.substring(0, t.indexOf("T"));
		else
			updateddate = t;
	}

	public void setPublishedDate(String t) {
		if (t.contains("T"))
			publisheddate = t.substring(0, t.indexOf("T"));
		else
			publisheddate = t;
	}

	public void setTerminationDate(String t) {
		if (("0123456789").contains(t.substring(0, 1))) {
			if (t.contains("T"))
				terminationdate = t.substring(0, t.indexOf("T"));
			else
				terminationdate = t;
		} else {
			terminationdate = "Indefinite/Null";
		}
	}

	public void setActivationDate(String t) {
		activationdate = t;
	}

	public void setResponseDate(String t) {
		if (t.length() > 0 && ("0123456789").contains(t.substring(0, 1))) {
			if (t.contains("T"))
				responsedate = t.substring(0, t.indexOf("T"));
			else
				responsedate = t;
		}
	}

	public void setNoticeType(String t) {
		noticetype = t;
	}

	public void setClassification(String t) {
		classification = t;
	}

	public void setSamNumber(String t) {
		if (!t.equals("pirValue"))
			samnumber = t;
	}

	public void setDunsNumber(String t) {
		if (!t.equals("pirKey"))
			dunsnumber = t;
	}

	public void setCageCode(String t) {
		if (!t.equals("isActive"))
			cagecode = t;
	}

	public void setPhysicalAddress(String t) {
		physicaladdress = t;
	}

	public String[] toArray() {
		return (new String[] { title, updateddate, publisheddate, responsedate, activationdate, terminationdate,
				noticeID, noticetype, department, subtier, office, classification, samnumber, dunsnumber, cagecode });
	}

	public String toString() {
		return (Arrays.toString(new String[] { title, updateddate, publisheddate, responsedate, activationdate,
				terminationdate, noticeID, noticetype, department, subtier, office, classification, samnumber,
				dunsnumber, cagecode }));
	}

	@Override
	public int compareTo(Object o) {
		String[] t_parse = updateddate.split("-");
		String[] o_parse = ((Entry) o).updateddate.split("-");
		if (t_parse.length != 3 && o_parse.length != 3)
			return 0;
		else if (t_parse.length != 3)
			return -1;
		else if (o_parse.length != 3)
			return 1;
		if (Integer.parseInt(t_parse[0]) > Integer.parseInt(o_parse[0])) {
			return 1;
		} else if (Integer.parseInt(t_parse[0]) < Integer.parseInt(o_parse[0])) {
			return -1;
		} else {
			if (Integer.parseInt(t_parse[1]) > Integer.parseInt(o_parse[1])) {
				return 1;
			} else if (Integer.parseInt(t_parse[1]) < Integer.parseInt(o_parse[1])) {
				return -1;
			} else {
				if (Integer.parseInt(t_parse[2]) > Integer.parseInt(o_parse[2])) {
					return 1;
				} else if (Integer.parseInt(t_parse[2]) < Integer.parseInt(o_parse[2])) {
					return -1;
				}
			}
		}
		return 0;
	}
}