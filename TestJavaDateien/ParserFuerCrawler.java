package parser;

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

	private static String title;
	private static String description;
	private static String artikel;

	public static void main(String[] args) throws ParserConfigurationException {
		ParserFuerCrawler t = new ParserFuerCrawler();
		t.readXml();

	}

	public void readXml() throws ParserConfigurationException {

		File fXmlFile = new File(
				"C:/Users/Christoph/Studium/Semester/WS2015/eclipse_Workspace/ParserCrawlerProject/RSS-73389953.xml");
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

				title=eElement.getElementsByTagName("title").item(0).getTextContent();
				description=eElement.getElementsByTagName("description").item(0).getTextContent();
				artikel=eElement.getElementsByTagName("ExtractedText").item(0).getTextContent();
				//title.add(eElement.getElementsByTagName("title").item(0).getTextContent());
				//description.add(eElement.getElementsByTagName("description").item(0).getTextContent());
				//artikel.add(eElement.getElementsByTagName("ExtractedText").item(0).getTextContent());

				System.out.println("Titel: " + title);
				System.out.println();
				System.out.println("Beschreibung: " + description);
				System.out.println();
				System.out.println("Text des Artikels: " + artikel);

			}
		}
	}

	

}