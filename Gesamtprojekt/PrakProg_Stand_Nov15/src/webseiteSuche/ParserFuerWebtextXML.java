package webseiteSuche;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParserFuerWebtextXML {

	private static ArrayList<String> webtextListe = new ArrayList<String>();

	private String webtext = "";

	// ./Gesamtprojekt/PrakProg_Stand_Nov15/WebText.xml

	// public static void main(String[] args) throws
	// ParserConfigurationException {
	// ParserFuerCrawler t = new ParserFuerCrawler();
	// t.readXml();
	// }

	public void readXml(String path) throws ParserConfigurationException {

		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("Datensatz");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				webtextListe.add(eElement.getElementsByTagName("name").item(0).getTextContent());

				webtext += webtextListe.get(temp).toString();
			}
		}
		System.out.println(webtext);
	}

	public String getWebtext() {
		return webtext;
	}

	public void setWebtext(String webtext) {
		this.webtext = webtext;
	}

	public static void main(String args[]) {

		ParserFuerWebtextXML parser = new ParserFuerWebtextXML();

		try {
			parser.readXml("./WebText.xml");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ParserFuerWebtextXML() {

	}
}