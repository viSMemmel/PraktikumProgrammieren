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
		String Ergebnis = null;
		List <String> SuchWoerter = null;
		Parser p = new Parser(filePathWortliste );
		SuchWoerter = p.getRetAr(SelectedElement);
		sAs =  SuchWoerter.toArray(new String[SuchWoerter.size()]);
		
		Search suche = new Search(sAs, filePathVerzeichnis);
		Ergebnis = suche.find();
		
		
		
		return Ergebnis;
		
		
	}
}
