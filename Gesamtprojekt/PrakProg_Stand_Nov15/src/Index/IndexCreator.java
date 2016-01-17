package Index;
/*
 * Quelle: http://www.avajava.com/tutorials/lessons/how-do-i-use-lucene-to-index-and-search-text-files.html
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryParser.QueryParser; Pfadänderung in Lucene 4.5
import org.apache.lucene.queryparser.classic.QueryParser.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockObtainFailedException;

import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.apache.lucene.util.Version;

public class IndexCreator {

	/*
	 * Mehrer Dokumente werden eingelesen
	 */
	private String FILES_TO_INDEX_DIRECTORY; // Verzeichnis von dem Eingelesen
												// werden soll
	private String DirToIndexDir;// Indexverzeichnis
	private String FIELD_PATH; // Feld in das der Suchindex geschrieben werden
								// soll
	private String Title;
	// private String FIELD_CONTENTS ;

	@SuppressWarnings("deprecation")
	public NIOFSDirectory createIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45); // brauchen
																		// wir
																		// den??
																		// Englische
																		// Stoppwörter
	
		NoLockFactory noLockFactory = NoLockFactory.getNoLockFactory();
		NIOFSDirectory indexDir = new NIOFSDirectory(new File(DirToIndexDir), noLockFactory);// ,
																						// new
																						// LockFactory());
																						// //
																					// koorrekt

		org.apache.lucene.index.IndexWriterConfig config = new org.apache.lucene.index.IndexWriterConfig(
				Version.LUCENE_45, analyzer);

		// config.setWriteLockTimeout(1000);
		// config.setIndexDeletionPolicy( IndexDeletionPolicy.); geht irgendwie
		// nett
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); // vgl.
																			// http://stackoverflow.com/questions/17711347/avoid-indexing-documents-again-lucene

		IndexWriter indexWriter = new IndexWriter(indexDir, config);// ???

		
		File[] files = getFiles(FILES_TO_INDEX_DIRECTORY);//dir.listFiles();  // auslagern

			try{
				
				for(int findex=0; findex<files.length; findex++){
					File currentFile=files[findex];
					String path=currentFile.getAbsolutePath();
				Document document = new Document();
				
			System.out.println(path +"ist datei" );
			document.add(new Field(Title, path, Field.Store.YES, Field.Index.NOT_ANALYZED));

			FReader fr = new FReader(path);   // hier über Liste iterieren
			//System.out.println(fr.getText());
			// fieldTypee
			// storeTermVectorPossition
			// auslesen termPossitions Lucene
			document.add(new Field(FIELD_PATH, fr.getText(), Field.Store.YES, Field.Index.ANALYZED));
			// document.add(new Field("Words", (String) fr.getWords(),
			// Field.Store.YES, Field.Index.NOT_ANALYZED));

			//System.out.println(document.toString());
			indexWriter.addDocument(document);
				}
			}
			catch(Exception e){  // rekrusive Indexstruturen sollen hier behandelt werden
					System.out.println("Index konnte nicht erstellt werden - Pfad: ");
			}
		
		// indexWriter.optimize(); nötig
		indexWriter.close(); // hier Problem bei wiederholtem Aufruf
		// indexDir.clearLock(FIELD_PATH);
		// indexDir.close();
		System.out.println("Done - Index created");
		return indexDir;
	}

	
	
	private File[] getFiles(String fILES_TO_INDEX_DIRECTORY2) { 
		/*
		 * Soll auch Dateien in Ordnern erfassen, die ihrerseits weitere Unterordner haben,
		 * 
		 */
		// TODO Auto-generated method stub
		System.out.println(fILES_TO_INDEX_DIRECTORY2);
		ArrayList<File> FileList = new ArrayList<File>();
		File dir = new File(fILES_TO_INDEX_DIRECTORY2);
		
		
		File[] files = dir.listFiles();
		
		for (File file : files) {
			if(file.isDirectory()){
				
				File[] RekArray=getFiles(file.getAbsolutePath());
				for(int z=0; z<RekArray.length; z++){
					FileList.add(RekArray[z]);
				
				}
				
			}else{
				FileList.add(file);
				
			}
		}
		
		File [] FileReturn = new File[FileList.size()];
		return (File[]) FileList.toArray(FileReturn);
		
	}



	public IndexCreator(String fILES_TO_INDEX_DIRECTORY, String indexDir, String fIELD_PATH, String title) {
		super();
		FILES_TO_INDEX_DIRECTORY = fILES_TO_INDEX_DIRECTORY;
		DirToIndexDir = indexDir;
		FIELD_PATH = fIELD_PATH;
		Title =title;
	}
}
