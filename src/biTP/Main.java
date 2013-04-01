package biTP;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.ui.RefineryUtilities;

public class Main {

	/* LACROIX Florent 2A IR
	 * TP BI Mining
	 * 
	 * Le but est, pour un série donnée, de recupérer le nombre de saison.
	 * Ensuite, on choisi une saison, puis un épisode pour lancer les comparaisons.
	 * On demande ensuite deux autres infos : le nombre d'épisode que l'on souhaite
	 * dans la liste des épisodes les plus similaires à l'épisode sélectionné, puis
	 * le nombre de tag voulu dans le nuage de tag.
	 * 
	 * Les informations s'affichent alors dans la console, et une fenêtre s'ouvre
	 * avec un histogramme.
	 */
    
    // We can verify if the summary isn't empty or throw an exception
    // We can add some words to the filter (to, and, ...)
	
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		
		/* USER INTERFACE */
		/* ----------------------------------------------------------------------------------- */
		// Demand to the code series
		System.out.print("Veuillez saisir le code de la série (ex : How I Met Your Mother --> tt0460649) : ");
		String serie = sc.nextLine();
		/* ----------------------------------------------------------------------------------- */
		
		HtmlSourceGetter hsg = new HtmlSourceGetter();
		
		// Get back of HTML Source for a URL : here, for the number of seasons in the series
		String htmlSourceForSeries = null;
		try {
			htmlSourceForSeries = hsg.getHtmlSource(new URL("http://www.imdb.com/title/"+serie+"/episodes?season=1"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		// Regex : get back of all seasons and all years
		Pattern regexSeason = Pattern.compile("<option  value=\"(.*?)\">");
		Matcher mNumSeason = regexSeason.matcher(htmlSourceForSeries);
		Pattern regexNameSerie = Pattern.compile("itemprop='url'>(.*?)</a>");
		Matcher mNameSerie = regexNameSerie.matcher(htmlSourceForSeries);
		
		int seasomTemp;
		int numberOfSeason=1;
		
		// Get back of number of season
		while(mNumSeason.find()){
			seasomTemp = Integer.parseInt(mNumSeason.group(1));
			if(seasomTemp>numberOfSeason && seasomTemp<500)
				numberOfSeason=seasomTemp;				
		}
		
		String serieName=null;
		
		// Get back name of the series
		while(mNameSerie.find())
				serieName=mNameSerie.group(1);

		/* USER INTERFACE */
		/* ----------------------------------------------------------------------------------- */
		// Demand for the numerous of the seasons to analyze
		int season=0;
		try{
			while(season>numberOfSeason || season <1){
				System.out.print("Cette série comprend " + numberOfSeason + " saison(s). Sur laquelle souhaitez-vous lancer une analyse ? ");
				season=sc.nextInt();
			}
		} catch(InputMismatchException e){
			e.printStackTrace();
		}
		/* ----------------------------------------------------------------------------------- */
		
		// Get back of HTML Source for a URL
		String htmlSourceForSeason=null;
		try {
			htmlSourceForSeason = hsg.getHtmlSource(new URL("http://www.imdb.com/title/"+serie+"/episodes?season="+season));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		
		// Regex
		Pattern regexTitle = Pattern.compile("\"itemprop=\"name\">(.*?)</a>");
		Pattern regexSummary = Pattern.compile("<div class=\"item_description\" itemprop=\"description\">(.*?)</div>");
		Matcher mTitle = regexTitle.matcher(htmlSourceForSeason);
		Matcher mSummary = regexSummary.matcher(htmlSourceForSeason);		
		
		Filter filter = new Filter();
		
		// Create List LEpisode
		List<Episode> LEpisode = new LinkedList<Episode>();
		int number=1;
		
		while(mTitle.find() && mSummary.find()){
			String title = mTitle.group(1);
			String summary = mSummary.group(1);
			
			LEpisode.add(new Episode(number++,title,summary,filter.simplifySummary(summary)));
		}
		
		/* Occurrence of each words in all summary
		(resume in a HashMap<Integer,HashMap<String,Integer>> LWordSimplifySummary */
		OccurencesCounter oc = new OccurencesCounter();
		ListIterator<Episode> li = LEpisode.listIterator();
		HashMap<Integer,HashMap<String,Integer>> LWordSimplifySummary = new HashMap<>();
		StringBuffer allSimplifySummary = new StringBuffer();
		Episode episode;
		
		while(li.hasNext()){
			episode = li.next();
			LWordSimplifySummary.put(episode.getNumber(),oc.count(episode.getSimplifySummary()));
			allSimplifySummary.append(episode.getSimplifySummary());
			// last line to put all simplified summaries in one StringBuffer, for the tag cloud
		}
		
		/* CosinusSimilarity */
		/* calculateCS calculate the similarity of a summary to each other
		(return a HashMap<Integer NumEpisode,Double CosinusSimilarity>) */
		
		/* USER INTERFACE */
		/* ----------------------------------------------------------------------------------- */
		// Demand numerous of the episode to compare
		int numEpisodeToCompare = 0;
		try{
			while(numEpisodeToCompare>LEpisode.size() || numEpisodeToCompare <1){
				System.out.print("Cette saison comprend " + LEpisode.size() + " épisode(s). Lequel souhaitez-vous comparer aux autres ? ");
				numEpisodeToCompare=sc.nextInt();
			}
		} catch(InputMismatchException e){
			e.printStackTrace();
		}
		
		// Demand number of the episode to max similarity
		int numEpisodeMaxSimilarity = 0;
		try{
			while(numEpisodeMaxSimilarity>LEpisode.size() || numEpisodeMaxSimilarity<1){
				System.out.print("Combien d'épisode(s) souhaitez-vous avoir dans votre classement des épisodes les plus similaires à l'épisode " + numEpisodeToCompare + " ? ");
				numEpisodeMaxSimilarity=sc.nextInt();
			}
		} catch(InputMismatchException e){
			e.printStackTrace();
		}

		// Demand numerous of tag in cloud tag
		int numberWordInTagCloud=0;
		int sizeTagCloudMax = 50;
		try{
			while(numberWordInTagCloud>sizeTagCloudMax || numberWordInTagCloud <1){
				System.out.print("Combien de mot(s) souhaitez-vous dans votre nuage de tag(s) ? (entre 1 et 50) ");
				numberWordInTagCloud=sc.nextInt();
			}
		} catch(InputMismatchException e){
			e.printStackTrace();
		}
		/* ----------------------------------------------------------------------------------- */
		
		HashMap<String,Integer> tagCloud = oc.maxOccurences(oc.count(allSimplifySummary.toString()), numberWordInTagCloud); // tag cloud calculation
		
		CosinusSimilarity cs = new CosinusSimilarity();
		HashMap<Integer,Double> LCosinusSimilarity = cs.calculateCS(numEpisodeToCompare, LWordSimplifySummary);
		
		/* getMostSimilarEpisode return the three summary who have the more similarity with the summary specified
		 (return a HashMap<Integer Rank, Integer numEpisode>) */
		HashMap<Integer,Integer> LMaxCosinusSimilarity = cs.getMostSimilarEpisode(numEpisodeToCompare, numEpisodeMaxSimilarity, new HashMap<Integer,Double>(LCosinusSimilarity));
		// we must create a new HashMap to duplicate LCosinusSimilarity

		/* RESULT OF THE ANALYZE FOR THE USER */
		/* ----------------------------------------------------------------------------------- */
		
		System.out.println(	"------------------------------------------\n");
		System.out.println(serieName + " : " + numberOfSeason + " saison(s)\nSaison choisi pour l'analyse : " + season + "\n");
		
		System.out.println(	"------------------------------------------\n\n"+
							"Voici la liste des " + numEpisodeMaxSimilarity + " épisodes les plus similaires avec l'épisode " + numEpisodeToCompare + " --> " + LEpisode.get(numEpisodeToCompare-1).getTitle() + "\n");
		for(int i=1;i<=numEpisodeMaxSimilarity;i++){
			System.out.println("Épisode " + numEpisodeToCompare + " avec épisode " + LMaxCosinusSimilarity.get(i) + " : \t" + LCosinusSimilarity.get(LMaxCosinusSimilarity.get(i))*100 + " %\t--> " + LEpisode.get(LMaxCosinusSimilarity.get(i)-1).getTitle());
		}
		
		System.out.println(	"\n------------------------------------------\n\n"+
							"Voici l'histogramme de l'épisode " + numEpisodeToCompare + " comparé aux autres épisodes de la saison " + season + " :\n");
		
		System.out.print("\t\t");
		
		for(int i=0;i<50;i++)
			System.out.print(".");
		
		System.out.println(" 100 %");
		
		for(int i=1;i<=LCosinusSimilarity.size();i++){
			if(numEpisodeToCompare!=i){
				int d = (int) (LCosinusSimilarity.get(i)/0.02);
				System.out.print(numEpisodeToCompare + "-" + i + " : " +(int)(LCosinusSimilarity.get(i)*100) + " %" + "\t");
				for(int j=0;j<d;j++){
					System.out.print("|");
				}
				System.out.println();
			}
		}
		
		System.out.println(	"\n------------------------------------------\n"+
							"\nNuage de tags avec le poids de chacun des mots (" + numberWordInTagCloud + " mot(s) dans le nuage) : \n");
		System.out.println(tagCloud + "\n\n------------------------------------------");
		System.out.println("\nVeuillez trouver ci-joint l'histogramme, dans une fenêtre annexe (!)\n\n------------------------------------------");
		
		final BarChart bc = new BarChart(serieName + " : saison " + season + ", \nHistogramme de l'épisode " + numEpisodeToCompare + " comparé aux \nautres épisodes de la saison " + season, LCosinusSimilarity, numEpisodeToCompare);
		bc.pack();
        RefineryUtilities.centerFrameOnScreen(bc);
        bc.setVisible(true);
		/* ----------------------------------------------------------------------------------- */
	}
}
