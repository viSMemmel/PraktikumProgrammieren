package Index;

import org.apache.lucene.store.NIOFSDirectory;

public class IndexApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * public Index(String fILES_TO_INDEX_DIRECTORY, String indexDir, String
		 * fIELD_PATH, String fIELD_CONTENTS)
		 */

		IndexCreator i = new IndexCreator("C:/test/", "ersterTest", "INHALT");

		/* Index(Ordner_mit_zu_indizierenden_Dateien, Dateipfad_Indey); */

		try {
			NIOFSDirectory d = i.createIndex();
			System.out.println(d.toString() + " - Erfolg ");
		} catch (Exception e) {
			System.out.println("Fehler");
			e.printStackTrace();
		}
	}

}
