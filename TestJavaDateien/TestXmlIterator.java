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

public class TestXmlIterator {

	private static ArrayList<String> englisch = new ArrayList<String>();
	private static ArrayList<String> deutsch = new ArrayList<String>();

	public static void main(String[] args) throws ParserConfigurationException {
		TestXmlIterator t = new TestXmlIterator();
		t.readXml();

	}

	public void readXml() throws ParserConfigurationException {

		File fXmlFile = new File(
				"C:/Users/Christoph/Studium/Semester/WS2015/eclipse_Workspace/FileChooseExample/Anglizismen.xml");
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

				englisch.add(eElement.getElementsByTagName("Englisch").item(0).getTextContent());
				deutsch.add(eElement.getElementsByTagName("Deutsch").item(0).getTextContent());

				System.out.println("Englisch: " + englisch.get(temp).toString());
				System.out.println("Deutsch: " + deutsch.get(temp).toString());

			}
		}
	}

}