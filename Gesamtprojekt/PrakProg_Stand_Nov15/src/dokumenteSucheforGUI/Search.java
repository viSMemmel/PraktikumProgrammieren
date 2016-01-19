package dokumenteSucheforGUI;

//auskommentierte zeile zur Verbesserung der Ausgabe: 102, 117, 107
// FileReaderApp: ausgabe  auskommentiert
//IndexCreator: Zeile 86 + 94
//nicht sicher ob ich auskommentiert ode rberiets vorher: 98
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import index.DeleteDir;
import index.IndexCreator;

public class Search {
	private String[] SearchSubjectAr;
	private String[] Deutsch;
	private String FilesToIndex;
	private String Feld = "Inhalt";
	private String Title = "NUR_FUER_INTERNE_ZWECKE_NAME_VOELLIG_BUMBS";
	private String titleSave;
	private int fundstelleZahl;
	private String fundstelle;
	private List<Integer> FundListe;

	public Search(String[] searchSubjectAr, String filesToIndex, String [] deutsch) {
		super();
		Deutsch = deutsch; 
		SearchSubjectAr = searchSubjectAr;
		FilesToIndex = filesToIndex;
	}

	@SuppressWarnings("finally")
	public String find() throws ParseException, IOException {

		String Ausgabe_in_Textarea_alt = " "; // zur DokuZwecken
		String Ausgabe_in_Textarea = " ";
		try {
			boolean keineEintraege = true;

			NIOFSDirectory indexDir;
			String NameInternerOrdner = FilesToIndex + "_Index";
			IndexCreator index = new IndexCreator(FilesToIndex, NameInternerOrdner, Feld, Title);
			indexDir = index.createIndex();

			DirectoryReader dr = DirectoryReader.open(indexDir); // Öffnen des
																	// Verzeichnises
																	// textIndexDir
																	// über die
																	// Variable
																	// indexDir

			System.out.println(indexDir.getDirectory());
			// Ausgabe_in_Textarea_alt+=indexDir.getDirectory()+"\n";

			// NIOFSDirectory indexDir = new NIOFSDirectory(new File(IndexDir));
			// // wird vom index übergeben
			Analyzer analyzer2 = new StandardAnalyzer(Version.LUCENE_45); // Soll
																			// Query
																			// analysieren

			QueryParser qp = new QueryParser(Version.LUCENE_45, Feld, analyzer2);

			IndexSearcher searcher = new IndexSearcher(dr); // Indexsuche
			for (int n = 0; n < SearchSubjectAr.length; n++) { // Problem
																// Dokumente
																// werden
																// mehrfach
																// genannt, wenn
																// mehrer Wörter
																// der Liste
																// daran vor
																// kommen

				String SearchSubject = SearchSubjectAr[n];
				String deutschObject = Deutsch[n];

				Query query = qp.parse(SearchSubject); //

				TopDocs td = searcher.search(query, 1000); // Die ersten 1000
															// Dokumente mit
															// höchstem Ranking
				ScoreDoc[] sd = td.scoreDocs; //

				if (sd.length != 0) {
					keineEintraege = false;

					// Ausgabe_in_Textarea += "Es wurde folgendes Wort gefunden:
					// " +SearchSubject ;
					// Document doc = searcher.doc(0); Völliger Blödsinn!!!!!!!

					for (int i = 0; i < sd.length; i++) {
						Document doc = searcher.doc(i);
						searcher.doc(sd[i].doc); // Dokument mit
													// dem höchsten
													// "Ranking"
													// wird hier
													// "gefunden"

						// Ausgabe_in_Textarea += "Dokument gefunden: " + doc.getField(Title).stringValue() + "\n"; // Achtung
						// Ausgabe_in_Textarea_alt += "\n \n Dokument gefunden "
						// + doc.getField(Title).name() + "\n" +
						// Ausgabe_in_Textarea_alt; // Achtung
						// String // müssen

						// List<IndexableField> iField = doc.getFields(); böse
						// System.out.println("Ifield " + iField.size());//im
						// Moment immer 2
						List<IndexableField> iField = new LinkedList<IndexableField>();
						iField = doc.getFields();

						/*
						 * for(int i1=0; i1<iField.size(); i1++){
						 * System.out.println("IFELD"
						 * +iField.get(i1).toString()); }
						 */

						SearchInDoc searchInDoc = new SearchInDoc(iField, Feld, SearchSubject); // bei
						// überlauf

						List<Integer> FundListe = searchInDoc.getFundListe();
						if(FundListe.size()>0){

						Ausgabe_in_Textarea += "Dokument gefunden: " + doc.getField(Title).stringValue() + "\n";	
							
						Ausgabe_in_Textarea += "\nDas Dokument hat " + searchInDoc.Anzahl() + " Wörter."; 

						Ausgabe_in_Textarea += "\nEs wurde folgendes Wort gefunden: " + SearchSubject;

						Ausgabe_in_Textarea += " ("+ FundListe.size()+ "  Treffer) "   + " (deutsche Entsprechung: " + deutschObject+ ")\n";
						
						for (int x = 0; x < FundListe.size(); x++) {
							// fundstelleZahl=FundListe.get(x);
							Ausgabe_in_Textarea += " \n \t Das gesuchte Wort " + SearchSubject + " befinden sich  an "
									+ FundListe.get(x) + ". Stelle.";
							Ausgabe_in_Textarea += "\n";
						} // searchInDoc.close();
						Ausgabe_in_Textarea += "\n";
					}

					Ausgabe_in_Textarea_alt += "\n";

					//Ausgabe_in_Textarea += "\n"; // Bitte nicht aus kommentieren

					// System.out.println("\n ReadertoString() " +
					// searchInDoc.toString());
					}
				}

			}
			if (keineEintraege) {
				Ausgabe_in_Textarea += "Keine Einträge enthalten";
			}

			dr.close(); // muss geschlossen werden, damit indexDir geschlossen
			// werden kann

			indexDir.close(); // muss geschlossen werden damit Inhalt de
			@Deprecated
			DeleteDir deleteDir = new DeleteDir(NameInternerOrdner);
			deleteDir.delete(); // Der Ordner in denen die Indexdateien
								// geschrieben
								// wurden, wird nach Beendigung des Suchprozeß
								// gelöscht
			// internOrdner gelöscht werden kann

		} catch (Exception e) {
			System.out.println("Fehler!!!!");
			e.printStackTrace();
			System.out.println(e.getMessage() + "  " + e.getLocalizedMessage());
		} finally {
			System.out.println("--------------------");
			System.out.println(Ausgabe_in_Textarea_alt);
			System.out.println("--------------------");
			return Ausgabe_in_Textarea;
		}

	}

}
