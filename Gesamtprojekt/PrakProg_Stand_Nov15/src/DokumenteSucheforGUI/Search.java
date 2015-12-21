package DokumenteSucheforGUI;

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
import java.util.List;
import java.util.ListIterator;

import Index.DeleteDir;
import Index.IndexCreator;

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

public class Search {
	private String[] SearchSubjectAr;
	private String FilesToIndex;
	private String Feld = "Inhalt";
	private String Title = "NUR_FUER_INTERNE_ZWECKE_NAME_VOELLIG_BUMBS";
	private String titleSave;
	private int fundstelleZahl;
	private String fundstelle;
	private List<Integer> FundListe;
	

	public Search(String[] searchSubjectAr, String filesToIndex) {
		super();
		SearchSubjectAr = searchSubjectAr;
		FilesToIndex = filesToIndex;
	}

	@SuppressWarnings("finally")
	public String find() throws ParseException, IOException {

		String Ausgabe_in_Textarea = null; // Bitte nicht aus kommentieren doch!!!
		try {

			/*
			 * if(Files.exists(FileSystems.getDefault().getPath(
			 * NameInternerOrdner) , LinkOption.NOFOLLOW_LINKS)){ // indexDir=
			 * (NIOFSDirectory) Paths.get(NameInternerOrdner);
			 * //sun.nio.fs.WindowsPath cannot be cast to
			 * org.apache.lucene.store.NIOFSDirectory sun.nio.fs.WindowsPath
			 * cannot be cast to org.apache.lucene.store.NIOFSDirectory
			 * indexDir= (NIOFSDirectory)
			 * FileSystems.getDefault().getPath(NameInternerOrdner);
			 * 
			 * //(NIOFSDirectory) Paths.get(NameInternerOrdner);
			 * System.out.println("Existiert"); }else{ System.out.println(
			 * "NICHT exisitent ");
			 * 
			 * }
			 */

			NIOFSDirectory indexDir;
			String NameInternerOrdner = FilesToIndex + "_Index";
			IndexCreator index = new IndexCreator(FilesToIndex, NameInternerOrdner, Feld, Title);
			indexDir = index.createIndex();
			// }

			DirectoryReader dr = DirectoryReader.open(indexDir); // �ffnen des
																	// Verzeichnises
																	// textIndexDir
																	// �ber die
																	// Variable
																	// indexDir
			
			System.out.println(indexDir.getDirectory());
			Ausgabe_in_Textarea+=indexDir.getDirectory()+"\n";

			// NIOFSDirectory indexDir = new NIOFSDirectory(new File(IndexDir));
			// // wird vom index �bergeben
			Analyzer analyzer2 = new StandardAnalyzer(Version.LUCENE_45); // Soll
																			// Query
																			// analysieren

			QueryParser qp = new QueryParser(Version.LUCENE_45, Feld, analyzer2);

			IndexSearcher searcher = new IndexSearcher(dr); // Indexsuche
			for (int n = 0; n < SearchSubjectAr.length; n++) {
				String SearchSubject = SearchSubjectAr[n];
				Query query = qp.parse(SearchSubject); //
  
				 TopDocs td = searcher.search(query, 10); // Die ersten 10
															// Dokumente mit
															// h�chstem Ranking
				ScoreDoc[] sd = td.scoreDocs; //

				if (sd.length == 0) {
				//Ausgabe_in_Textarea = "Keine Eintr�ge gefunden";
				} else {

					//System.out.println(" \nGefundenes Wort: " + SearchSubject);

					Document doc = searcher.doc(0);
					// Position des W. kann in Index gespeichert werden
					// Klasse IndexCreator �ndernn ev
					// IndexableField iField[] =null;
					for (int i = 0; i < sd.length; i++) {
						// System.out.println("l�nge " + sd.length);
						// System.out.println("shared index " +
						// sd[i].shardIndex);

						searcher.doc(sd[i].doc); // Dokument mit
													// dem h�chsten
													// "Ranking"
													// wird hier
													// "gefunden"
						
						//Ausgabe_in_Textarea += "Dokument gefunden " + doc.getField(Title) + "\n" + Ausgabe_in_Textarea; // Achtung
																														// String
																														// immer
																														// konkatinieren,
																														// da
																														// vorherige
																														// Ergebnisse
																														// mitber�cksichtig
																														// werden
																														// m�ssen

						// System.out.println("Dokument gefunden " +
						// doc.toString());
						// hier muss dann eine Suche innerhalb des do
						// stattfinden

						List<IndexableField> iField = doc.getFields();
						// System.out.println("Ifield " + iField.size());//WAS
						// SOLL DAS SEIN??
						SearchInDoc searchInDoc = new SearchInDoc(iField, Feld, SearchSubject); // bei
																								// Gro�er
																								// Anzahl
																								// von
																								// Dokumenten
																								// evtl.
																								// Speicher
																								// �berlauf

						//Ausgabe_in_Textarea += "\n Das Dokument hat " + searchInDoc.Anzahl() + " W�rter. \n";
						
						List<Integer> FundListe = searchInDoc.getFundListe();
						//Ausgabe_in_Textarea += "Das gesuchte Wort " + SearchSubject + " befinden sich  "; 
						
						
							
						//Ausgabe in textarea so wie ausgabe bei sysout, da dies �bersichtlich und ausreichend ist!
						if(searchInDoc.getCounter()!=0 ){
							
						//Ausgabe_in_Textarea+=doc.get(Title);  
						Ausgabe_in_Textarea+= "\nGefundenes Wort: " +SearchSubject+""; 
						Ausgabe_in_Textarea+= "\nAnzahl Treffer: "+searchInDoc.getCounter(); 
						System.out.println(doc.get(Title));	
						System.out.println("\nGefundenes Wort: " +SearchSubject);
						System.out.println("Anzahl Treffer: "+searchInDoc.getCounter());
						for (int x = 0; x < FundListe.size(); x++) {
							
							
							System.out.println("Fundstelle: " + FundListe.get(x));
							Ausgabe_in_Textarea+="\nFundstelle: " + FundListe.get(x) ; 
							fundstelleZahl=FundListe.get(x);

						}
						Ausgabe_in_Textarea+="\n";
						}
						Ausgabe_in_Textarea+="\n"; // Bitte nicht aus kommentieren

						// System.out.println("\n ReadertoString() " +
						// searchInDoc.toString());
						searchInDoc.close();

						 

					}
					
					//searchInDoc.getCounter();
					
					//System.out.println(" Fundstelle: " + fundstelleZahl);
					//System.out.println(doc.get(Title));
				}
			}


			dr.close(); // muss geschlossen werden, damit indexDir geschlossen
			// werden kann

			indexDir.close(); // muss geschlossen werden damit Inhalt de
			@Deprecated
			DeleteDir deleteDir = new DeleteDir(NameInternerOrdner);
			deleteDir.delete(); // Der Ordner in denen die Indexdateien
								// geschrieben
								// wurden, wird nach Beendigung des Suchproze�
								// gel�scht
			// internOrdner gel�scht werden kann
			
		} catch (Exception e) {
			System.out.println("Fehler!!!!");
			e.printStackTrace();
			System.out.println(e.getMessage() + "  " + e.getLocalizedMessage());
		} finally {
			return Ausgabe_in_Textarea;
		}
		
	}

}
