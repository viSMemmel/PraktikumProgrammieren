package dateiSuche;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class TextDurchsucher {
	
	private String Ausgabe_in_Textarea = " ";
	
	public TextDurchsucher(List<String> list, String path, List <String> deutschList) {

		
		List<Integer> fundListe = null;
		List<Integer> fundStelle;
		List <String> woerterListe = null;
		List<String> liste =  list;
		List <String> deutschListe = deutschList;
		String text = "";
		
		
		ParserFuerCrawler p = new ParserFuerCrawler();
		try {
			p.readXml(path);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text = p.getGesamterText();
		String Sucharray[] = null;
		Sucharray = text.split(" ");

		int counter = 0;
		ArrayList<Integer> fundliste = new ArrayList<Integer>();

		Ausgabe_in_Textarea +="Folgende Datei wurde durchsucht: " +path;
		Ausgabe_in_Textarea+= "\n";
		Ausgabe_in_Textarea+= "\n";
		
		for (int i = 0; i < liste.size(); i++) {
			
			for (int j = 0; j < Sucharray.length; j++) {
				if (Sucharray[j].equals(liste.get(i))) {
					
					counter++;
					//System.out.println(counter);
					
					
						Ausgabe_in_Textarea+="Es wurde das Wort: " + liste.get(i);
						String fund= liste.get(i);
						int iwas = liste.indexOf(fund);
						
						Ausgabe_in_Textarea+=" gefunden (deutsche Entsprechung: " + deutschListe.get(iwas)+")" ;
						//fundListe.add(j);
						//woerterListe.add(liste.get(i));
						
						Ausgabe_in_Textarea+= "\n an "+j+". Stelle gefunden \n";
						Ausgabe_in_Textarea+= "\n";
					
					
					System.out.println("Das Wort: " + liste.get(i) + " wurde an " +j+". Stelle gefunden \n");
					System.out.println();
					
				}
			}
			
			
			counter =0;
		}
//		for (int i = 0; i < liste.size(); i++) {
//			System.out.println("englisch= "+ liste.get(i));
//			System.out.println("deutsch = " + deutschListe.get(i));
//			
//		}

	}

	public String getAusgabe_in_Textarea() {
		return Ausgabe_in_Textarea;
	}

	public void setAusgabe_in_Textarea(String ausgabe_in_Textarea) {
		Ausgabe_in_Textarea = ausgabe_in_Textarea;
	}

	public static void main(String[] args) throws ParserConfigurationException {
//		ArrayList<String> liste = new ArrayList<String>();
//
//		liste.add("Chefin");
//		liste.add("Volkswagen");
//		liste.add("handy");
//
//		ParserFuerCrawler p = new ParserFuerCrawler();
//		p.readXml();
//		String text = p.getGesamterText();
//		String Sucharray[] = null;
//		Sucharray = text.split(" ");
//
//		int counter = 0;
//		ArrayList<Integer> fundliste = new ArrayList<>();
//
//		for (int i = 0; i < liste.size(); i++) {
//			
//			for (int j = 0; j < Sucharray.length; j++) {
//				if (Sucharray[j].equals(liste.get(i))) {
//					
//					counter++;
//					//System.out.println(counter);
//					
//					System.out.println("Es wurde das Wort: " + liste.get(i) + " gefunden an " +j+". Stelle gefunden \n");
//					
//				}
//			}
//			
//			counter =0;
//		}
//
//		

	}

}