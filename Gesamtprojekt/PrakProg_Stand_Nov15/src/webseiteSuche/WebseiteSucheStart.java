package webseiteSuche;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WebseiteSucheStart {

	private String links;
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	public void webseiteStart2(String link){
		try {
			int start = 0;
			int stop = 0;
			// String link = "http://www.html-seminar.de/erste-html-seite.htm";
			String urlText = link;
			System.out.println(link);
			// String pText = "";
			link = "http://www.welt.de/politik/ausland/article150940812/Hat-der-Attentaeter-gezielt-nach-Deutschen-gesucht.html";
			links = link;

			URL url = new URL(links);

			Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));
			while (scanner.hasNextLine()) {

				String speicher = urlText;
				urlText = speicher + scanner.nextLine() + "\n";

			}
			String pElement;

			String searchString = "<p ";
			String searchString3 = "<p>";
			String searchstring2 = "</p>";
			int occurencesStart = 0;
			int occurencesStop = 0;
			List<Integer> startStelle = new ArrayList<Integer>();
			List<Integer> stopStelle = new ArrayList<Integer>();

			if (0 != searchString.length()) {
				for (int index = urlText.indexOf(searchString, 0); index != -1; index = urlText.indexOf(searchString,
						index + 1)) {

					startStelle.add(index);
					occurencesStart++;
				}
			}
			if (0 != searchString3.length()) {
				for (int index = urlText.indexOf(searchString3, 0); index != -1; index = urlText.indexOf(searchString3,
						index + 1)) {

					startStelle.add(index);
					occurencesStart++;
				}
			}
			if (0 != searchstring2.length()) {
				for (int index = urlText.indexOf(searchstring2, 0); index != -1; index = urlText.indexOf(searchstring2,
						index + 1)) {

					stopStelle.add(index);
					occurencesStop++;
				}
			}

			System.out.println("Anzahl Starttags: " + occurencesStart);
			for (int i = 0; i < startStelle.size(); i++) {
				System.out.println("Stelle: " + startStelle.get(i));
			}

			System.out.println("Anzahl an Endtags: " + occurencesStop);
			for (int i = 0; i < stopStelle.size(); i++) {
				System.out.println("Stelle: " + stopStelle.get(i));
			}

			List<String> pTagText = new ArrayList<String>();

			for (int i = 0; i < startStelle.size() && i < stopStelle.size(); i++) {
				String save = "";
				String pText = "";
				while (startStelle.get(i) < stopStelle.get(i)) {

					pText += urlText.charAt(startStelle.get(i));
					save = pText;
					int speicher = startStelle.get(i);
					speicher++;
					startStelle.set(i, speicher);
				}
				pTagText.add(save);
				// System.out.println(pText);
			}

			for (int i = 0; i < pTagText.size(); i++) {
				System.out.println(pTagText.get(i));
			}

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Referenzdaten");
			doc.appendChild(rootElement);

			for (int i = 0; i < pTagText.size(); i++) {
				Element letter = doc.createElement("Datensatz");
				rootElement.appendChild(letter);
				Element name = doc.createElement("name");
				System.out.println("Tag1: " + i);
				System.out.println(pTagText.get(i));
				name.appendChild(doc.createTextNode((pTagText.get(i))));
				letter.appendChild(name);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./WebText.xml"));
			transformer.transform(source, result);

		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		} catch (TransformerException e) {
			
			e.printStackTrace();
		}
	}
}
