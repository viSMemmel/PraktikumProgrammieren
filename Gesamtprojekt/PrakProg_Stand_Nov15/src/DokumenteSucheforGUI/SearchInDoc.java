package DokumenteSucheforGUI;
// Zeile 51!!!
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Version;

public class SearchInDoc {
	private int counter=0;
	private List<IndexableField> IField;
	private String SearchSubject;
	private String FieldName;
	private TermsEnum Terme;
	private TokenStream TStream; // verarbeitet einzelne Buchstaben
	private TokenFilter TFilter; // evtl. unnötig
	private List<String> tokenList = new ArrayList<String>();
	private List<Integer> FundListe = new ArrayList<Integer>();
	
	

	private Reader reader = null;
	// https://lucene.apache.org/core/4_0_0/core/org/apache/lucene/index/DocsEnum.html

	public SearchInDoc(List<IndexableField> iField, String fieldName, String searchSubject) {
		SearchSubject=searchSubject;
		
		// TODO Auto-generated constructor stub#
		IField = iField;
		FieldName = fieldName;
		Iterator iter = IField.iterator();
		
		while (iter.hasNext()) {
			IndexableField currentField = (IndexableField) iter.next();
			// System.out.println("?????????????"+ currentField.name() + " "+
			// currentField.stringValue());
			
			if (currentField.name().equals(FieldName)) {
				//System.out.println("\n Treffer bei der Suche!!!"); // WAS GENAU GEFUNDEN???
				
				
				// reader=currentField.readerValue();
				// Analyzer analyzer = new Analyzer();

				try {
					Analyzer analyzer2 = new StandardAnalyzer(Version.LUCENE_45); // Soll
																					// Query
																					// analysieren
					// tokenArray =
					// tokensFromAnalysis(analyzer2,currentField.toString() );

					// TStream= currentField.tokenStream(analyzer2 ); redundant

					TStream = analyzer2.tokenStream(FieldName, new StringReader(currentField.stringValue()));
	
					String text = currentField.stringValue();
					OffsetAttribute offsetAttribute = TStream.getAttribute(OffsetAttribute.class);
					CharTermAttribute termAttribute = TStream.getAttribute(CharTermAttribute.class);
					// siehe:
					// http://stackoverflow.com/questions/2638200/how-to-get-a-token-from-a-lucene-tokenstreamc
					// i.V.m.
					// http://stackoverflow.com/questions/2638200/how-to-get-a-token-from-a-lucene-tokenstream
					// TermToBytesRefAttribute termAttribute =
					// TStream.getAttribute(TermAttribute.class);
					TStream.reset();
					int Fundstelle = 0;
					while (TStream.incrementToken()) {
		
						String term = termAttribute.toString();
						
						Fundstelle++;
						if(term.toLowerCase().equals(searchSubject.toLowerCase())){
							
							counter ++;
							FundListe.add(Fundstelle);
							
						}
						
				//	System.out.println(term + "  "+ term.length() + "   SSUBject  "+ searchSubject.toLowerCase()+ searchSubject.length());
						try {
							tokenList.add(term);
						} catch (Exception e) {
						} // hier muss noch Fleisch rein

					}
					
					TStream.end();
					TStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
//		if(counter>0){
//			System.out.println(" \nGefundenes Wort: "+searchSubject);
//			System.out.println(String.valueOf("Anzahl der Treffer: "+counter));
//			for (int i = 0; i < FundListe.size(); i++) {
//				System.out.println("Fundstelle: "+FundListe.get(i));
//			}
//		}
	}

	public String getSearchSubject() {
		return SearchSubject;
	}



	public void setSearchSubject(String searchSubject) {
		SearchSubject = searchSubject;
	}



	public TokenStream getTStream() {
		return TStream;
	}



	public void setTStream(TokenStream tStream) {
		TStream = tStream;
	}



	public List<String> getTokenList() {
		return tokenList;
	}



	public void setTokenList(List<String> tokenList) {
		this.tokenList = tokenList;
	}



	public int getCounter() {
		return counter;
	}



	public void setCounter(int counter) {
		this.counter = counter;
	}



	public List<Integer> getFundListe() {
		return FundListe;
	}



	public void setFundListe(List<Integer> fundListe) {
		FundListe = fundListe;
	}



	public String toString() throws NullPointerException {
		int SucheFeld = 0;

		return null;

	}

	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int Anzahl() {

		int i = 0;
		try {
			i = tokenList.size();
		} catch (Exception e) {

		}
		return i;

	}

}
