package biTP;

public class Episode {
	private int number;
	private String title;
	private String summary;
	private String simplifySummary;
	
	Episode(){
		this.number=0;
		this.title=null;
		this.summary=null;
		this.simplifySummary=null;
	}
	
	Episode(int number, String title, String summary, String simplifySummary){
		this.number=number;
		this.title=title;
		this.summary=summary;
		this.simplifySummary=simplifySummary;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getSummary(){
		return this.summary;
	}
	
	public String getSimplifySummary(){
		return this.simplifySummary;
	}
	
	public int getNumber(){
		return this.number;
	}
	
	public String toString(){
		return this.number + " : " + this.title + "\n" + this.summary + "\n" + this.simplifySummary;
	}
}
