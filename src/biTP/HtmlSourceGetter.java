package biTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HtmlSourceGetter {
	
	public String getHtmlSource(URL url){
		StringBuffer htmlSource = new StringBuffer();
		
		try{
			URLConnection urlC = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(urlC.getInputStream()));

			String inputLine=null;
			
			while((inputLine = br.readLine()) != null){
				htmlSource.append(inputLine);
			}
			return htmlSource.toString();
		}catch (IOException e){
			System.out.println("Erreur 404 : Not Found");
			e.printStackTrace();
			return "";
		}
	}
}
