/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import javax.swing.JDialog;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author szhc
 */
public class ErrorDispPanel extends JDialog{
    public ErrorDispPanel(String appTitle,String chartTitle) {
        xyLineChart = ChartFactory.createXYLineChart(chartTitle, "点位", "偏差(mm)", 
                createDataset(), PlotOrientation.VERTICAL, true, true, false);
        
        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        chartPanel.setPreferredSize(new Dimension(1300,650));
        plot = xyLineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0,Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(0,new BasicStroke(0.4f));
        renderer.setSeriesStroke(0,new BasicStroke(0.4f));
        plot.setRenderer(renderer);
        setContentPane(chartPanel);
        
        Font titleFont = new Font("宋体",Font.PLAIN,16);
        Font plotFont = new Font("宋体",Font.PLAIN,16);
        Font legendFont = new Font("宋体",Font.PLAIN,16);
        xyLineChart.getTitle().setFont(titleFont);
        xyLineChart.getLegend().setItemFont(legendFont);
        plot.getDomainAxis().setLabelFont(plotFont);
        plot.getDomainAxis().setTickLabelFont(plotFont);
        plot.getRangeAxis().setLabelFont(plotFont);
        
        this.setTitle(appTitle);
        Toolkit toolKit = Toolkit.getDefaultToolkit();
        Image img = toolKit.getImage("Res/Tracker.png");
        this.setIconImage(img);
        pack();
    }
    private XYDataset createDataset() {
        diff_prev = new XYSeries("校准前");
        diff_post = new XYSeries("校准后");
//        for(int i = 0;i < 50;++i) {
//            diff_prev.add(i+1,0);
//            diff_post.add(i+1,0);
//        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(diff_prev);
        dataset.addSeries(diff_post);
        return dataset;
    }
    
    public void RefreshChartData(double[] prev,double[] post) {
        diff_prev.clear();
        diff_post.clear();
        for(int i = 0;i < prev.length;++i) {
//            diff_prev.addOrUpdate(i+1, prev[i]);
//            diff_post.addOrUpdate(i+1, post[i]);
            diff_prev.add(i+1, prev[i]);
            diff_post.add(i+1, post[i]);
        }
    }
    
    private final JFreeChart xyLineChart;
    private final XYPlot plot;
    private XYSeries diff_prev;
    private XYSeries diff_post;
}
