package biTP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class OccurencesCounter {

	public OccurencesCounter(){}
	
	public HashMap<String,Integer> count(String simplifySummary){
		
		StringTokenizer st;
		st = new StringTokenizer(simplifySummary, " ");
		
		HashMap<String,Integer> counter = new HashMap<>();
		
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			if(counter.containsKey(word))
				counter.put(word,counter.get(word)+1);
			else
				counter.put(word,1);
		}
		return counter;
	}
	
	public HashMap<String,Integer> maxOccurences(HashMap<String,Integer> tagCloud, int numberWordInTagCloud){
		HashMap<String,Integer> maxOcc = new HashMap<>();
		
		tagCloud.put(" ",0);
		/* we add artificially this value to initialize the algorithm
		   We take the key " " because she doesn't exist in the tag cloud */
		Set<String> keysWords = tagCloud.keySet();
		Iterator<String> it;
		String currentWord;
		String wordMaxOcc = " ";
		
		for(int i=0;i<numberWordInTagCloud;i++){
			it = keysWords.iterator();
			while(it.hasNext()){
				currentWord = it.next();
				if(tagCloud.get(currentWord)>tagCloud.get(wordMaxOcc))
					wordMaxOcc=currentWord;
			}
			maxOcc.put(wordMaxOcc,tagCloud.get(wordMaxOcc));
			tagCloud.remove(wordMaxOcc);
			wordMaxOcc = " ";
		}
		return maxOcc;
	}
}
