package biTP;

import java.awt.Dimension;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class BarChart  extends ApplicationFrame {
	
	public BarChart(final String title) {
		super(title);
	}
	
	public BarChart(final String title, HashMap<Integer,Double> LCosinusSimilarity, int numEpisodeToCompare) {
		super(title);
		
		final CategoryDataset dataset = createDataset(LCosinusSimilarity, numEpisodeToCompare);
	    final JFreeChart chart = createChart(dataset);
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(800, 600));
	    setContentPane(chartPanel);
	}

	private CategoryDataset createDataset(HashMap<Integer,Double> LCosinusSimilarity, int numEpisodeToCompare) {
    
	    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	
	    String similarity = "Similarité (en %)";
	    for(int i=1;i<=LCosinusSimilarity.size();i++){
	    	if(i!=numEpisodeToCompare)
	    		dataset.addValue(LCosinusSimilarity.get(i), similarity, String.valueOf(i));
	    }
	    return dataset;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {
	    
	    final JFreeChart chart = ChartFactory.createBarChart(
	        this.getTitle(),         // chart title
	        "Numéro épisode",               // domain axis label
	        "Similarité en %",                  // range axis label
	        dataset,                  // data
	        PlotOrientation.VERTICAL, // orientation
	        false,                     // include legend
	        false,                     // tooltips?
	        false                     // URLs?
	    );
	    
		return chart;
	}
}
