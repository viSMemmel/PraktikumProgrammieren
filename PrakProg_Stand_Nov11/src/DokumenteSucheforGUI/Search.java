package DokumenteSucheforGUI;

import java.io.File;
import java.io.IOException;

import Index.IndexCreator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
public class Search {
	 private String SearchSubject;
	 private  String FilesToIndex;
	 private String IndexFeld="Inhalt";

	 
	 
	 

		public Search(String searchSubject, String filesToIndex) {
		super();
		SearchSubject = searchSubject;
		FilesToIndex = filesToIndex;
	
	}





		@SuppressWarnings("finally")
		public String find() throws ParseException, IOException{
			String Ausgabe_in_Textarea = null;
			try{
			IndexCreator index = new IndexCreator(FilesToIndex, "/internOrdner", IndexFeld);
			
			NIOFSDirectory indexDir = index.createIndex();
			    //  NIOFSDirectory indexDir = new NIOFSDirectory(new File(IndexDir));  // wird vom index übergeben
			      DirectoryReader dr = DirectoryReader.open(indexDir); // Öffnen des Verzeichnises textIndexDir über die Variable indexDir
			   Analyzer analyzer2 = new StandardAnalyzer(Version.LUCENE_45); // Soll Query analysieren

		 QueryParser qp = new QueryParser(Version.LUCENE_45,IndexFeld,analyzer2);
		  
			//   QueryParser qp = new QueryParser();
			   
		       IndexSearcher searcher = new IndexSearcher(dr); // Indexsuche
		
		       Query query = qp.parse(SearchSubject); //

		      

		       TopDocs td = searcher.search(query,10); // Die ersten 10 Dokumente mit höchstem Ranking

		       ScoreDoc[] sd = td.scoreDocs; //

		      if(sd.length==0){
		    	  System.out.println("Keine Einträge gefunden");
		      }else{
		    

		       for (int i=0; i < sd.length; i++) {

		         Document doc = searcher.doc(sd[i].doc); // Dokument mit dem höchsten "Ranking" wird hier "gefunden"
		         Ausgabe_in_Textarea ="Dokument gefunden "+ doc.toString() + "\n"+Ausgabe_in_Textarea;
		         System.out.println("Dokument gefunden "+ doc.toString() );
		         IndexableField  iField[] =doc.getFields(SearchSubject);
		         System.out.println("Ifield " +  iField.length);
		         System.out.println(doc.get("title"));

		       }}
		      }
			catch(Exception e){
				  Ausgabe_in_Textarea ="\n Fehler !!!!!! \n";
		         Ausgabe_in_Textarea =  e.getLocalizedMessage();
				//System.out.println(e.getMessage() + "  "+
				//e.getLocalizedMessage());
			}
		finally{
		System.out.println(Ausgabe_in_Textarea); // zu Test Zwecken
			return Ausgabe_in_Textarea;
		}
		}
		
	}
