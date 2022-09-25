import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Document {
	String content = "";

	public Document(String URL) throws IOException {
		URL url = new URL(URL);
		Scanner sc = new Scanner(url.openStream());
		StringBuffer sb = new StringBuffer();
		while (sc.hasNextLine()) {
			content += sc.nextLine();
		}
	}
}