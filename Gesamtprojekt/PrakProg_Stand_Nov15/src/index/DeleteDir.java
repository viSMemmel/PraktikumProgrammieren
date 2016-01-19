package index;

import java.io.File;

import org.apache.lucene.index.DirectoryReader;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.io.File;

/*
 * Quelle: http://www.rgagnon.com/javadetails/java-0483.html
 * 
 * Diese Klasse ist unnötigt
 * 
 */

public class DeleteDir {
	String dir; // ordner-Pfad ist als String zu übergeben;

	public DeleteDir(String dr) {
		super();
		this.dir = dr;
	}

	public void delete() { // Aufruf der rekrusiven Methode
		deleteDirectory(new File(dir)); // auch wenn hier ein neues File erzeugt
										// wird, wird der alte Ordner gelöscht
	}

	static private boolean deleteDirectory(File path) { // arbeitet rekrusiv
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		System.out.println("gelöscht du nob");
		return (path.delete());
	}
}