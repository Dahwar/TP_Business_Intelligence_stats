package biTP;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;


public class Filter {
	
	private Set<String> stopWords;
	
	Filter(){
		String[] wordList = { "when", "while", "who", "thi", "up", "have",
				"don", "ha", "hi", "him", "so", "out", "an", "that", "is",
				"in", "the", "he", "she", "her", "s", "i", "m", "t",
				"after", "from", "all", "can", "do", "which", "doesn", "go" };
		stopWords = new HashSet<String>(Arrays.asList(wordList));
	}
	
	public String simplifySummary(String summary){
		
		// Filter
		Reader reader = new StringReader(summary);
		TokenStream ts = new LowerCaseTokenizer(Version.LUCENE_36, reader);
		ts = new StandardFilter(Version.LUCENE_36, ts);
		ts = new PorterStemFilter(ts);
		ts = new StopFilter(Version.LUCENE_36, ts, stopWords);
		
		// Transform TokenStream to String.
		boolean hasnext = false;
		try {
			hasnext = ts.incrementToken();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		StringBuffer newSummary = new StringBuffer();
		while (hasnext) {
			CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);
			newSummary.append(ta.toString()+" ");
			try {
				hasnext = ts.incrementToken();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return newSummary.toString();
	}
}
