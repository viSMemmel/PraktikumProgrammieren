package parser;

import java.util.List;
import java.util.ListIterator;

public class ParserApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parser p = new Parser("src/Anglizismenliste.xml");

		/*
		 * in das erste Feld ist der relative Pfade zu dem zu parsendem
		 * xml-Dokument anzugeben in das zweite Feld ist der auszulesende
		 * XML-TAG Anzugeb
		 */
		List<String> test;
		test = p.getErgebnis();

		ListIterator<String> ListenIterator = test.listIterator();

		while (ListenIterator.hasNext()) {
			System.out.println(ListenIterator.next());
		}
System.out.println("\n ende \n");
test = p.getRetAr(1);

ListIterator<String> ListenIterator2 = test.listIterator();

while (ListenIterator2.hasNext()) {
	System.out.println(ListenIterator2.next());
}
	}

}
