package DokumenteSuche;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
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
 private String SearchSubject;
 private String IndexDir;
	
	
	public Search(String search, String indexDir) {
	super();
	this.SearchSubject = search;
	this.IndexDir = indexDir;
}


	public void find() throws ParseException, IOException{
		      NIOFSDirectory indexDir = new NIOFSDirectory(new File(IndexDir)); 
		      DirectoryReader dr = DirectoryReader.open(indexDir); // Öffnen des Verzeichnises textIndexDir über die Variable indexDir
		   Analyzer analyzer2 = new StandardAnalyzer(Version.LUCENE_45); // Soll Query analysieren

	       QueryParser qp = new QueryParser(Version.LUCENE_45,"content",analyzer2);
	  
	      
	       IndexSearcher searcher = new IndexSearcher(dr); // Indexsuche
	
	       Query query = qp.parse(SearchSubject); //

	      

	       TopDocs td = searcher.search(query,10); // Die ersten 10 Dokumente mit höchstem Ranking

	       ScoreDoc[] sd = td.scoreDocs; //

	      

	       for (int i=0; i < sd.length; i++) {

	         Document doc = searcher.doc(sd[i].doc); // Dokument mit dem höchsten "Ranking" wird hier "gefunden"

	         System.out.println(doc.get("title"));

	       }
	}
	
}
