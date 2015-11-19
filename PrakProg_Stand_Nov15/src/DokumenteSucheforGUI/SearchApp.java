package DokumenteSucheforGUI;

import DokumenteSuche.SearchAlt;

public class SearchApp {
	/*1.12 Zwischenpräseentation
	 * wird eigentlich nicht mehr benötigt, da die Index App automatisch eine Suche instanziertt
	 * 
	 * */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Search s  =  new Search("C:/test/","du");
		Search s1  =  new Search("C:/test/","wie");
		Search s2  =  new Search("C:/test/","hallo");
		try{
		s.find();
		s1.find();
		System.out.println("/n test2 /n");
		s2.find();
		System.out.println("Funtst");
	}catch(Exception e){
		System.out.println("fehler !!!");
		e.printStackTrace();
	
	}
	}
}
