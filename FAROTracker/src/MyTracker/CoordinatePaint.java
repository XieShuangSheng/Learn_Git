/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyTracker;

import java.awt.*;
import javax.swing.*;

import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.style.StandardChartStyle;
import com.orsoncharts.Colors;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.label.StandardXYZLabelGenerator;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;

/**
 *
 * @author szhc
 */
public class CoordinatePaint extends JPanel{
    public CoordinatePaint(String title,String chartTitle) {
        initComponents(title,chartTitle);
    }
    
    private void initComponents(String title,String chartTitle) {
        this.setPreferredSize(new Dimension(520,330));//520,555
        this.setLayout(new BorderLayout());
        xyzDataset = createXYZDataset();
        chart = Chart3DFactory.createScatterChart(title, chartTitle, xyzDataset, "X", "Y", "Z");
        xyzPlot = (XYZPlot)chart.getPlot();
        xyzPlot.setDimensions(new Dimension3D(5.0,5.0,5.0));
        xyzPlot.setLegendLabelGenerator(new StandardXYZLabelGenerator(StandardXYZLabelGenerator.COUNT_TEMPLATE));
        ScatterXYZRenderer renderer = (ScatterXYZRenderer)xyzPlot.getRenderer();
        renderer.setSize(0.08);
        renderer.setColors(Colors.createIntenseColors());
        ViewPoint3D view3D = new ViewPoint3D(40.0,90.0,45.0,0.0);
        chart.setViewPoint(view3D);
        
        Font titleFont = new Font("宋体",Font.PLAIN,16);
        Font subTitleFont = new Font("宋体",Font.PLAIN,14);
        Font labelFont = new Font("宋体",Font.PLAIN,14);
        StandardChartStyle style = new StandardChartStyle();
        style.setTitleFont(titleFont);
        style.setSubtitleFont(subTitleFont);
        style.setAxisLabelFont(labelFont);
        chart.setStyle(style);
        
        Chart3DPanel chartPanel = new Chart3DPanel(chart);
//        chartPanel.zoomToFit(new Dimension(505,555));
        chartPanel.removeMouseListener(chartPanel);
        chartPanel.removeMouseMotionListener(chartPanel);
//        chartPanel.removeMouseWheelListener(chartPanel);
        this.add(new DisplayPanel3D(chartPanel));
    }
    
    private XYZSeries<String> createXYZSeries(String name,int count) {
        xyzSeries = new XYZSeries<String>(name);
//        for(int i = 0;i < count;++i) {
//            xyzSeries.add(Math.random()*100,Math.random()*100,Math.random()*100);
//        }
        return xyzSeries;
    }
    
    private XYZDataset<String> createXYZDataset() {
        XYZSeries<String> s = createXYZSeries("Points",10);
        XYZSeriesCollection<String> d = new XYZSeriesCollection<String>();
        d.add(s);
        return d;
    }
    
    public void AddPoint(double x,double y,double z) {
        if(xyzSeries == null) {
            return;
        }
        xyzSeries.add(x,y,z);
    }
    public void UpdatePoint(int index,double x,double y,double z) {
        if(xyzSeries == null || index >= xyzSeries.getItemCount()) {
            return;
        }
        xyzSeries.remove(index);
        xyzSeries.add(x,y,z);
    }
    public void ClearPoint() {
        if(xyzSeries == null) {
            return;
        }
        int cnt = xyzSeries.getItemCount();
        for(int i = cnt - 1;i >= 0;--i) {
            xyzSeries.remove(i);
        }
    }
    
    private XYZSeries<String> xyzSeries;
    private XYZDataset<String> xyzDataset;
    private Chart3D chart;
    private XYZPlot xyzPlot;
}
