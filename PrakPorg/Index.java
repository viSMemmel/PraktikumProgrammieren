package luceneTest;


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

public class Index {

/*
 * Mehrer Dokumente werden eingelesen
 */
	private String FILES_TO_INDEX_DIRECTORY ;  // Verzeichnis von dem Eingelesen werden soll
	private  String IndexDir;					// Indexverzeichnis
	//private  String FIELD_PATH;
	//private String FIELD_CONTENTS ;

	public Index(String fILES_TO_INDEX_DIRECTORY, String indexDir) {
		super();
		FILES_TO_INDEX_DIRECTORY = fILES_TO_INDEX_DIRECTORY;
		IndexDir = indexDir;
		/*FIELD_PATH = fIELD_PATH;
		FIELD_CONTENTS = fIELD_CONTENTS;*/
	}




	@SuppressWarnings("deprecation")
	public  void createIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);  // brauchen wir den?? Englische Stoppwörter
		boolean recreateIndexIfExists = true;
		NIOFSDirectory indexDir = new NIOFSDirectory(new File(IndexDir));
		IndexWriter indexWriter = new IndexWriter(indexDir,  new org.apache.lucene.index.IndexWriterConfig(Version.LUCENE_45, analyzer));
		File dir = new File(FILES_TO_INDEX_DIRECTORY);
		File[] files = dir.listFiles();
		for (File file : files) {
			Document document = new Document();

			String path = file.getCanonicalPath();
		//	document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.NOT_ANALYZED));
			document.add(new Field("PFAD", path, Field.Store.YES, Field.Index.NOT_ANALYZED));


			Reader reader = new FileReader(file);
			document.add(new Field("INHALT", reader));

			indexWriter.addDocument(document);
		}
	//	indexWriter.optimize(); nötig
		indexWriter.close();
		System.out.println("Done - Index created");
	}
}
