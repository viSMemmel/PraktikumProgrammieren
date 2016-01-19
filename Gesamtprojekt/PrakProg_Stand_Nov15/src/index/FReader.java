package index;

import java.io.*;

public class FReader { // Die Klasse hies er FileReader, was wegen eines
						// Namenskonflikt eine sehr schlechte Idee war
	private String path = null;

	private int words = 0;

	public int getWords() {
		return words;
	}

	public FReader(String path) {
		super();
		this.path = path;
	}

	public String getText() {
		String S = null;
		FileReader fr = null;
		BufferedReader br;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);

			// Textzeilen der Datei einlesen und auf Konsole ausgeben:
			String zeile;
			zeile = br.readLine();

			while (zeile != null) {
				S += zeile;
				zeile = br.readLine();

			}
			br.close();

		} catch (IOException e) {
			System.out.println("Fehler beim Lesen der Datei " + path);
			System.out.println(e.toString());
		}

		return S;
	}

}
