package DokumenteSucheforGUI;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;


import parser.Parser;

public class XMLListSearch {
	private String filePathWortliste; 
	private String filePathVerzeichnis;
	private int SelectedElement;
	private String [] sAs;


	public XMLListSearch(String filePathWortliste, String filePathVerzeichnis, int selectedElement) {
		super();
		this.filePathWortliste = filePathWortliste;
		this.filePathVerzeichnis = filePathVerzeichnis;
		SelectedElement = selectedElement;
	}
	public String find() throws ParseException, IOException {
		String suchwort=null;
		String Ergebnis = null;
		List <String> SuchWoerter = null;
		Parser p = new Parser(filePathWortliste );
		SuchWoerter = p.getRetAr(SelectedElement);
		for (String temp : SuchWoerter) {
			suchwort += " " + temp;
		}
		
//		sAs =  SuchWoerter.toArray(new String[SuchWoerter.size()]);//- geht so nicht!
		sAs = suchwort.split(" ");
	/*	for(int i=0; i<sAs.length; i++){ //test
			System.out.println(sAs[i]); nur kontrolle
		}*/ 
		Search suche = new Search(sAs, filePathVerzeichnis);
		Ergebnis = suche.find();
		System.out.println(Ergebnis);
		
		
		return Ergebnis;
		
		
	}
}
