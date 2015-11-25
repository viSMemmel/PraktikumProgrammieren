package DokumenteSuche;

public class SearchApp {
	/*1.12 Zwischenpräseentation
	 * wird eigentlich nicht mehr benötigt, da die Index App automatisch eine Suche instanziertt
	 * 
	 * */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Search s  =  new Search("hallo","C:/Users/Schule/Documents/Semester4/GIS/Uebungsblaetter", "ersterTest");
		try{
		s.find();
		System.out.println("Funtst");
	}catch(Exception e){
		System.out.println("fehler !!!");
		e.printStackTrace();
	}
	}
}
