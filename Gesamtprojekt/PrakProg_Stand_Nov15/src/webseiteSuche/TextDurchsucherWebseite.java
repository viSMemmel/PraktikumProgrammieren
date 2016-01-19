package webseiteSuche;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class TextDurchsucherWebseite {

	private String Ausgabe_in_Textarea = " ";

	public TextDurchsucherWebseite(List<String> list, String path, List <String> deutsch, String link) {

		List<String> liste = list;
		List <String> deutsche = deutsch;
		String text = "";

		ParserFuerWebtextXML p = new ParserFuerWebtextXML();
		try {
			p.readXml(path);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text = p.getWebtext();
		String Sucharray[] = null;
		Sucharray = text.split(" ");

		int counter = 0;
		ArrayList<Integer> fundliste = new ArrayList<Integer>();

		Ausgabe_in_Textarea += "Folgende Webseite wurde durchsucht: " + link;
		Ausgabe_in_Textarea += "\n";
		Ausgabe_in_Textarea += "\n";

		for (int i = 0; i < liste.size(); i++) {

			for (int j = 0; j < Sucharray.length; j++) {
				if (Sucharray[j].equals(liste.get(i))) {

					counter++;
					// System.out.println(counter);

					Ausgabe_in_Textarea += "Es wurde das Wort: " ;
					
					Ausgabe_in_Textarea += liste.get(i) + " gefunden (deutsche Entsprechung: " + deutsche.get(i)+")";
							
					Ausgabe_in_Textarea += "\nAn " +j+ ". Stelle gefunden \n ";
					Ausgabe_in_Textarea += "\n";
					System.out.println("Das Wort: " + liste.get(i) + " wurde an " + j + ". Stelle gefunden \n");

				}
			}
			counter = 0;
		}

	}

	public String getAusgabe_in_Textarea() {
		return Ausgabe_in_Textarea;
	}

	public void setAusgabe_in_Textarea(String ausgabe_in_Textarea) {
		Ausgabe_in_Textarea = ausgabe_in_Textarea;
	}

	public static void main(String[] args) throws ParserConfigurationException {
		// ArrayList<String> liste = new ArrayList<String>();
		//
		// liste.add("Chefin");
		// liste.add("Volkswagen");
		// liste.add("handy");
		//
		// ParserFuerCrawler p = new ParserFuerCrawler();
		// p.readXml();
		// String text = p.getGesamterText();
		// String Sucharray[] = null;
		// Sucharray = text.split(" ");
		//
		// int counter = 0;
		// ArrayList<Integer> fundliste = new ArrayList<>();
		//
		// for (int i = 0; i < liste.size(); i++) {
		//
		// for (int j = 0; j < Sucharray.length; j++) {
		// if (Sucharray[j].equals(liste.get(i))) {
		//
		// counter++;
		// //System.out.println(counter);
		//
		// System.out.println("Es wurde das Wort: " + liste.get(i) + " gefunden
		// an " +j+". Stelle gefunden \n");
		//
		// }
		// }
		//
		// counter =0;
		// }
		//
		//

	}

}