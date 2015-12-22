package DokumenteSucheforGUI;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

public class SearchApp {
	private static final String[] Ar = {"hallo", "reisen"};

	/*
	 * Bitte Diese Klasse nicht löschen
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
XMLListSearch S = new XMLListSearch("C:/Users/schule/Documents/GitHub/PraktikumProgrammieren/Gesamtprojekt/PrakProg_Stand_Nov15/Anglizismen.xml","C:/test/T2",  1);
try {
	System.out.println(S.find());
} catch (ParseException | IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
