import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Output {
	String start = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
	boolean first = false;
	String last = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

	public Output() throws FileNotFoundException {
		LinkedList<String> keywords = new LinkedList<>();
		Scanner sc = new Scanner(new File("sam_keywords.txt"));
		while (sc.hasNext())
			keywords.add(sc.nextLine());

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if ((!last.equals(new SimpleDateFormat("MM-dd-yyyy").format(new Date())) || !first)
						&& LocalTime.now(ZoneId.systemDefault()).toSecondOfDay() > 51720) {
					if (!first)
						first = true;
					last = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
					try {
						scrape(keywords);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0, 5000);
	}

	public void scrape(LinkedList<String> keywords) throws IOException, MessagingException {
		LinkedList<Entry> allEntries = new LinkedList<>();
		for (int j = 0; j < keywords.size(); j++) {
			int size = 100000000;
			Document d = new Document(
					"https://sam.gov/api/prod/sgs/v1/search/?api_key=null&random=1622736871046&index=_all&page=0&mode=search&sort=-relevance&size="
							+ size + "&mfe=true&is_active=true&q=" + convertToHTMLQuery(keywords.get(j)));
			Scraper s = new Scraper(d.content, size);
			for (int i = 0; i < size; i++) {
				try {
					s.getEntry(i);
				} catch (Exception e) {
					break;
				}
			}
			allEntries.addAll(s.entries);
		}
		Collections.sort(allEntries);
		Collections.reverse(allEntries);
		String[][] returns = new String[allEntries.size()][15];
		for (int i = 0; i < allEntries.size(); i++)
			returns[i] = (allEntries.get(i).toArray());
		sendMail(returns);
	}

	public void sendMail(String[][] returns) throws MessagingException {
		Properties properties = new Properties();

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		String email = "benyonas03@gmail.com";
		String password = "~~Fgfgfg12**";

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});

		Message message = prepareMessage(session, email, "benyoans03@gmail.com", returns);
		Transport.send(message);
	}

	private Message prepareMessage(Session session, String email, String recipient, String[][] returns) {
		try {
			Multipart emailContent = new MimeMultipart();

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("SAM.gov Data Scrape Report");

			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart
					.setText("SAM.gov Data Scrape Report for " + new SimpleDateFormat("MM-dd-yyyy").format(new Date()));

			MimeBodyPart attatchment = new MimeBodyPart();
			attatchment.attachFile(exportAsXls(returns));

			emailContent.addBodyPart(textBodyPart);
			emailContent.addBodyPart(attatchment);
			message.setContent(emailContent);

			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public File exportAsXls(String[][] returns) {
		String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
		String att = "samreturns_" + date + ".xls";
		File archive = null;
		try {
			archive = new File(att);
			FileWriter excel = new FileWriter(archive);
			excel.write("Title \t");
			excel.write("Updated Date \t");
			excel.write("Published Date \t");
			excel.write("Response Date \t");
			excel.write("Activation Date \t");
			excel.write("Termination Date \t");
			excel.write("Notice ID \t");
			excel.write("Notice Type \t");
			excel.write("Department \t");
			excel.write("Subtier \t");
			excel.write("Office \t");
			excel.write("Classification \t");
			excel.write("SAM Number \t");
			excel.write("DUNS Number \t");
			excel.write("CAGE Code \t");
			excel.write("\n");
			for (int i = 0; i < returns.length; i++) {
				if (returns[i][0] == null)
					break;
				for (int j = 0; j < returns[i].length; j++) {
					String data = returns[i][j];
					if (data == "null") {
						data = "";
					}
					excel.write(data + "\t");
				}
				excel.write("\n");
			}
			excel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return archive;
	}

	public String convertToHTMLQuery(String in) {
		String[] splits = in.split(" ");
		String out = "%22";
		for (int i = 0; i < splits.length; i++) {
			if (i != splits.length - 1)
				out += splits[i] + "%20";
			else
				out += splits[i] + "%22";
		}
		return out;
	}

	public static void main(String args[]) throws IOException, MessagingException {
		new Output();
	}
}