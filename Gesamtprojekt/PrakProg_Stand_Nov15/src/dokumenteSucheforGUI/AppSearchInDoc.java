package dokumenteSucheforGUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;

public class AppSearchInDoc {
	/*
	 * Testprogramm für die Klasse SearchInDoc
	 * 
	 * 
SearchInDoc liefert leere FundListe an Search. Vielleicht findet jmd. von euch meinen Fehler
	 */



	//private static List<IndexableField> fListe;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	//	List<IndexableField> fListe = null; -- geht nicht; sondern
		List<IndexableField> fListe = new LinkedList();
		
		try {
			// TODO Auto-generated method stub
			IndexableField i1 = new Field("Title", "1. cool Titelpath das ist cool", Field.Store.YES,
					Field.Index.NOT_ANALYZED);
			IndexableField i2 = new Field("Title", "2. Titelpath das ist auch cool", Field.Store.YES,
					Field.Index.NOT_ANALYZED);
			IndexableField i3 = new Field("lalala", "cool 1. lalala", Field.Store.YES, Field.Index.NOT_ANALYZED);
			IndexableField i4 = new Field("lalala", "2. lalala ist doch cool", Field.Store.YES, Field.Index.NOT_ANALYZED);
			IndexableField i5 = new Field("lalala", "2. wie cool lalala ist doch cool", Field.Store.YES, Field.Index.NOT_ANALYZED);
			IndexableField i6 = new Field("lalala", "2. wie wotz cool lalala ist doch cool", Field.Store.YES, Field.Index.NOT_ANALYZED);

			fListe.add(i1);

			fListe.add(i2);
			fListe.add(i3);
			fListe.add(i4);
			fListe.add(i5);
			fListe.add(i6);
			SearchInDoc sd = new SearchInDoc(fListe, "lalala", "cool");
System.out.println("\n Das Dokument hat " + sd.Anzahl() + " Wörter. \n");
			List<Integer> Ergebnis = sd.getFundListe();

			for (int i = 0; i < Ergebnis.size(); i++) {
				System.out.println("Ergebnis : " + Ergebnis.get(i));
			}
			System.out.println("ENDE");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
