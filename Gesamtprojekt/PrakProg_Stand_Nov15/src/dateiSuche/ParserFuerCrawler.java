package dateiSuche;

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

public class ParserFuerCrawler {

	private static ArrayList<String> title = new ArrayList<String>();
	private static ArrayList<String> artikel = new ArrayList<String>();
	private String titelString = "";
	private String artikelString = "";
	private String gesamterText = "";

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

		NodeList nList = doc.getElementsByTagName("item");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				title.add(eElement.getElementsByTagName("title").item(0).getTextContent());
				artikel.add(eElement.getElementsByTagName("ExtractedText").item(0).getTextContent());
				titelString = title.get(temp).toString();
				artikelString = artikel.get(temp).toString();

				// System.out.println("Titel: " + title.get(temp).toString());
				// System.out.println("Text des Artikels: " +
				// artikel.get(temp).toString());
				System.out.println(titelString);
				System.out.println(artikelString);

			}
		}
		gesamterText = titelString + "" + artikelString;
	}

	public String getGesamterText() {
		return gesamterText;
	}

	public void setGesamterText(String gesamterText) {
		this.gesamterText = gesamterText;
	}

}