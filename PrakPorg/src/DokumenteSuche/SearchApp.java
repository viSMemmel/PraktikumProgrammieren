package DokumenteSuche;

public class SearchApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Search s  =  new Search("du", "ersterTest");
		try{
		s.find();
		System.out.println("Funtst");
	}catch(Exception e){
		System.out.println("fehler !!!");
		e.printStackTrace();
	}
	}
}
