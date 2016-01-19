package ausgabeDeutschesWort;

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

public class AusgabeDeutsch {

	private static ArrayList<String> deutsch = new ArrayList<String>();
	private static ArrayList<String> englisch = new ArrayList<String>();
	
	public void readXml() throws ParserConfigurationException {

		File fXmlFile = new File("Anglizismen_ENDE!!!.xml");
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

				deutsch.add(eElement.getElementsByTagName("Deutsch").item(0).getTextContent());
				englisch.add(eElement.getElementsByTagName("Englisch").item(0).getTextContent());
				//System.out.println("deutsch: " + deutsch.get(temp).toString());	
			}
		}

	}
	public static void main(String[] args) throws ParserConfigurationException {

		AusgabeDeutsch t = new AusgabeDeutsch();
		t.readXml();
		for (int i = 0; i < englisch.size(); i++) {
			System.out.println("englisch: "+ englisch.get(i));
			System.out.println("deutsch: "+ deutsch.get(i));
		}
		
		System.out.println(englisch.size());
		System.out.println(deutsch.size());
	}
	public static ArrayList<String> getEnglisch() {
		return englisch;
	}
	public static void setEnglisch(ArrayList<String> englisch) {
		AusgabeDeutsch.englisch = englisch;
	}
	public static ArrayList<String> getDeutsch() {
		return deutsch;
	}
	public static void setDeutsch(ArrayList<String> deutsch) {
		AusgabeDeutsch.deutsch = deutsch;
	}
}