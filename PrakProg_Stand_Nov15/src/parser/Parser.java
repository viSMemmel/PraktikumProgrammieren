package parser;

import java.io.FileWriter;

import java.io.IOException;

import java.net.URISyntaxException;

import java.util.*; // Achtung die Liste von .util funktioniert nicht 

import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Content;

import org.jdom2.Element;

import org.jdom2.Text;

import org.jdom2.filter.Filters;

import org.jdom2.input.DOMBuilder;

import org.jdom2.util.IteratorIterable;

//&import org.xml.sax.SAXException; 

/* 

 * Funktioniert liest sämtliche Spieler aller Vereine aus 

 * 

 */

public class Parser {

	protected List<String> retAr = new ArrayList<String>();

	public List<String> getRetAr() {
		retAr.add("Start der Liste ");
		read();
		return retAr;

	}

	String XmlPfadString;

	String ElementName;

	int n = 0;

	public Parser(String xmlPfadString, String elementName) {

		super();

		XmlPfadString = xmlPfadString;

		ElementName = elementName;

	}

	private void read() {

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder;

			builder = dbFactory.newDocumentBuilder();

			org.w3c.dom.Document doc = builder.parse(XmlPfadString); // hier xml

			DOMBuilder domB = new DOMBuilder();

			org.jdom2.Document jDoc = domB.build(doc);// = new DOMBuilder();

			System.out.println(doc.toString());

			org.jdom2.Element r = jDoc.getRootElement();

			/*
			 * for (Iterator it = r.getDescendants(Filters.element());
			 * 
			 * it.hasNext();) {
			 */
			Iterator it = r.getDescendants(Filters.element());
			while (it.hasNext()) {

				try {

					Element e = (org.jdom2.Element) it.next();

					if (e.getName() == ElementName) {

						String S = (String) e.getValue().toString();

						// System.out.println(e.getValue().toString());

						retAr.add(S);// Achtung nur ArrayList

						// n++; WTF, does it?

					}

				}

				catch (Exception e) {

					retAr.add("geht net" + e.getMessage());

				}

			}

		}

		catch (Exception ee) {

			retAr.add("io excption " + ee.getMessage());

		}

	}

}
