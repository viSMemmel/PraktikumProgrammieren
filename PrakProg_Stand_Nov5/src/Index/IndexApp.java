package Index;

public class IndexApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * 	public Index(String fILES_TO_INDEX_DIRECTORY, String indexDir,
			String fIELD_PATH, String fIELD_CONTENTS) 
		 */
		IndexCreator i =  IndexCreator.getInstance("C:/Users/Schule/Documents/Semester4/GIS/Uebungsblaetter", "ersterTest");
		/*Index(Ordner_mit_zu_indizierenden_Dateien, Dateipfad_Indey);*/
		
	/*	try{		wird im Konstruktor ausgeführt
		i.createIndex();
		}
		catch(Exception e){
		System.out.println("Fehler");
		e.printStackTrace();
		}*/
	}

}
