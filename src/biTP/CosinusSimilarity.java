package biTP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class CosinusSimilarity {

	public CosinusSimilarity(){}
	
	public HashMap<Integer,Double> calculateCS(int EpisodeNumber, HashMap<Integer,HashMap<String,Integer>> LWordSimplifySummary){
		if(LWordSimplifySummary.containsKey(EpisodeNumber)){
			HashMap<Integer,Double> vectorNorm = calculateVectorNorm(LWordSimplifySummary);
			HashMap<Integer,Double> similarityCosinusTable = new HashMap<>();
			
			Set<Integer> keysNumEpisode = LWordSimplifySummary.keySet();
			Iterator<Integer> it = keysNumEpisode.iterator();
			
			int numEpisode;
			int value;
			String keysStringTemp;
			HashMap<String,Integer> baseEpisode = LWordSimplifySummary.get(EpisodeNumber);
			Set<String> keysStringEpisode = baseEpisode.keySet();
			Iterator<String> itString;
			
			while(it.hasNext()){
				value=0;
				itString= keysStringEpisode.iterator();
				numEpisode = it.next();
				HashMap<String,Integer> currentEpisode = LWordSimplifySummary.get(numEpisode);
				while(itString.hasNext()){
					keysStringTemp = itString.next();
					if(currentEpisode.containsKey(keysStringTemp))
						value += baseEpisode.get(keysStringTemp)*currentEpisode.get(keysStringTemp);
				}
				similarityCosinusTable.put(numEpisode, value/(vectorNorm.get(EpisodeNumber)*vectorNorm.get(numEpisode)));
			}
			
			return similarityCosinusTable;
		}else
			return new HashMap<Integer,Double>();
			
	}
	
	// Return HashMap<Integer NumEpisode, Double normVectorEpisode>, use it in calculateCS to avoid some repetitive calculus
	private HashMap<Integer,Double> calculateVectorNorm(HashMap<Integer,HashMap<String,Integer>> LWordSimplifySummary){
		HashMap<Integer,Double> normVector = new HashMap<>();

		Set<Integer> keysNumEpisode = LWordSimplifySummary.keySet();
		Iterator<Integer> it = keysNumEpisode.iterator();
		
		Set<String> keysStringEpisode;
		Iterator<String> it2;
		int numEpisode;
		int tempNormVector;
		
		while (it.hasNext()){
			
			tempNormVector=0;
			numEpisode = it.next();
			keysStringEpisode = LWordSimplifySummary.get(numEpisode).keySet();
			it2 = keysStringEpisode.iterator();
			
			while (it2.hasNext())
				tempNormVector+=Math.pow(LWordSimplifySummary.get(numEpisode).get(it2.next()),2);
			
			normVector.put(numEpisode, Math.sqrt(tempNormVector));
		}
		
		return normVector;
	}
	
	public HashMap<Integer,Integer> getMostSimilarEpisode(int numEpisodeToCompare, int numberOfEpisode, HashMap<Integer,Double> LCosinusSimilarity){
		HashMap<Integer,Integer> max = new HashMap<>();

		LCosinusSimilarity.remove(numEpisodeToCompare);
		LCosinusSimilarity.put(0,0.0);
		// we add artificially this value to initialize the algorithm
		
		Set<Integer> keysNumEpisode = LCosinusSimilarity.keySet();
		Iterator<Integer> it;
		int numCurrentEpisode;
		int numEpisodeMax = 0;
		int rank=1;
		
		for(int i=0;i<numberOfEpisode;i++){
			it = keysNumEpisode.iterator();
			while(it.hasNext()){
				numCurrentEpisode = it.next();
				if(LCosinusSimilarity.get(numCurrentEpisode)>LCosinusSimilarity.get(numEpisodeMax))
					numEpisodeMax=numCurrentEpisode;
			}
			max.put(rank++,numEpisodeMax);
			LCosinusSimilarity.remove(numEpisodeMax);
			numEpisodeMax=0;
		}		
		return max;
	}
}
