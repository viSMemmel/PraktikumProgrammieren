package luceneTest;

public class IndexApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * 	public Index(String fILES_TO_INDEX_DIRECTORY, String indexDir,
			String fIELD_PATH, String fIELD_CONTENTS) 
		 */
		Index i = new Index("C:/Users/Administrator/Documents/testdateien", "ersterTest");
		/*Index(Ordner_mit_zu_indizierenden_Dateien, Dateipfad_Indey);*/
		
		try{
		i.createIndex();
		}
		catch(Exception e){
		System.out.println("Fehler");
		e.printStackTrace();
		}
	}

}
