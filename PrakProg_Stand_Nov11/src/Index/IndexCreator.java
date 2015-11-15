package Index;
/*
 * Quelle: http://www.avajava.com/tutorials/lessons/how-do-i-use-lucene-to-index-and-search-text-files.html
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryParser.QueryParser; Pfadänderung in Lucene 4.5
import org.apache.lucene.queryparser.classic.QueryParser.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class IndexCreator {

/*
 * Mehrer Dokumente werden eingelesen
 */
	private String FILES_TO_INDEX_DIRECTORY ;  // Verzeichnis von dem Eingelesen werden soll
	private  String IndexDir;					// Indexverzeichnis
	private String IndexFeld;
	
	
	
	
	public IndexCreator(String fILES_TO_INDEX_DIRECTORY, String indexDir,
			String indexFeld) {
		super();
		FILES_TO_INDEX_DIRECTORY = fILES_TO_INDEX_DIRECTORY;
		IndexDir = indexDir;
		IndexFeld = indexFeld;
	}
	//private  String FIELD_PATH;
	//private String FIELD_CONTENTS ;

	/*private static IndexCreator Singelton;
	111111111111111111111
	public static IndexCreator getInstance(String fILES_TO_INDEX_DIRECTORY, String indexDir){
		if(IndexCreator.Singelton==null){
			IndexCreator.Singelton= new IndexCreator( fILES_TO_INDEX_DIRECTORY,  indexDir);
			
			try{
				Singelton.createIndex();
				}
				catch(Exception e){
				System.out.println("Fehler");
				e.printStackTrace();
				}
		}else{
			System.out.println("Es wurde bereits ein Index erzeugt ");
			return null;
		}
		
		
		return IndexCreator.Singelton;
	}*/
	



	@SuppressWarnings("deprecation")
	public  NIOFSDirectory createIndex() throws CorruptIndexException, LockObtainFailedException, NegativeArraySizeException, IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);  // brauchen wir den?? Englische Stoppwörter
		boolean recreateIndexIfExists = true;
		
		
		
		NIOFSDirectory indexDir = new NIOFSDirectory(new File(IndexDir));
		IndexWriter indexWriter = new IndexWriter(indexDir,  new org.apache.lucene.index.IndexWriterConfig(Version.LUCENE_45, analyzer));
		File dir = new File(FILES_TO_INDEX_DIRECTORY);
		File[] files = dir.listFiles();System.out.println("index start");
		for (File file : files) {
			Document document = new Document();

			String path = file.getCanonicalPath();
		//	document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.NOT_ANALYZED));
			document.add(new Field("PFAD", path, Field.Store.YES, Field.Index.NOT_ANALYZED));


		/*	Reader reader = new FileReader(file);
			
			char testArray[] = new char [reader.read()];
		*/
			FReader fr = new FReader(file.getAbsolutePath());
	//		document.add(new TextField(IndexFeld, fr.getText(), Field.Store.NO));
			document.add(new Field(IndexFeld, fr.getText(), Field.Store.YES, Field.Index.NOT_ANALYZED));

System.out.println(document.toString());
			indexWriter.addDocument(document);
		}
	//	indexWriter.optimize(); nötig
		indexWriter.close();
		System.out.println("Done - Index created");
		return indexDir;
	}
	
}