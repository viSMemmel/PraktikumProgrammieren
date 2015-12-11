package parser;

import java.util.List;
import java.util.ListIterator;

public class ParserApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parser p = new Parser("C:/Anglizismenliste.xml");

		/*
		 * in das erste Feld ist der relative Pfade zu dem zu parsendem
		 * xml-Dokument anzugeben in das zweite Feld ist der auszulesende
		 * XML-TAG Anzugeb
		 */
		List<String> test;
		test = p.getKids();

		ListIterator<String> ListenIterator = test.listIterator();

		while (ListenIterator.hasNext()) {
			System.out.println(ListenIterator.next());
		}
System.out.println("\n ende \n");
String [] test2 = p.getRetAr(1);

int n=0;
while(n<test2.length){
	System.out.println(test2[n++]);
}


	}

}
