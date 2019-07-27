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
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.axis.IntegerTickSelector;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.renderer.xyz.LineXYZRenderer;
import com.orsoncharts.style.StandardChartStyle;

/**
 *
 * @author szhc
 */
public class TrajectoryCurve extends JPanel{
    public TrajectoryCurve(String title,String subTitle) {
        initComponents(title,subTitle);
    }
    
    private void initComponents(String title,String subTitle) {
        this.setPreferredSize(new Dimension(300,300));
        this.setLayout(new BorderLayout());
        xyzDataset = createDataset();
        chart = Chart3DFactory.createXYZLineChart(title, subTitle, xyzDataset, "X", "Y", "Z");
        chart.setChartBoxColor(new Color(255,255,255,128));
        xyzPlot = (XYZPlot)chart.getPlot();
        xyzPlot.setDimensions(new Dimension3D(4.0,4.0,4.0));
        ViewPoint3D view3D = new ViewPoint3D(120.0,300.0,40.0,0.0);
        chart.setViewPoint(view3D);
        
        Font titleFont = new Font("宋体",Font.PLAIN,16);
        Font labelFont = new Font("宋体",Font.PLAIN,16);
        Font legendFont = new Font("宋体",Font.PLAIN,16);
        StandardChartStyle style = new StandardChartStyle();
        style.setTitleFont(titleFont);
        style.setAxisLabelFont(labelFont);
        chart.setStyle(style);
        
        
        chartPanel = new Chart3DPanel(chart);
        chartPanel.zoomToFit(new Dimension(300,300));
//        chartPanel.removeMouseListener(chartPanel);
//        chartPanel.removeMouseMotionListener(chartPanel);
        this.add(new DisplayPanel3D(chartPanel));
    }
    
    private XYZSeriesCollection<String> createDataset() {
        XYZSeriesCollection dataset = new XYZSeriesCollection();
//        for(int i = 1;i <= 10;++i) {
//            XYZSeries<String> s = new XYZSeries("Line"+i);
//            double y = 1.0;
//            for(int j = 0;j < 3000;++j) {
//                y = y * (1.0 + (Math.random() - 0.499) / 10.0);
//                s.add(j,y,i);
//            }
//            dataset.add(s);
//        }
        
        return dataset;
    }
    
    private XYZSeries<String> createSeries(int line,double[] values) {
        XYZSeries<String> s = new XYZSeries("Line"+line);
        for(int i = 0;i < values.length/3;i++) {
//            if(line == 0) {
                s.add(values[i*3],values[i*3+1],values[i*3+2]);
//            }
//            else {
//                s.add(values[i*3] + Math.random(),values[i*3] + Math.random(),values[i*3] + Math.random());
//            }
        }
        return s;
    }
    
    public void AddLineTrajectory(double[] values) {
        XYZSeries<String> s = createSeries(index,values);
        index++;
        xyzDataset.add(s);
    }
    
    public void ClearLineTrajectory() {
//        xyzDataset = createDataset();
        index = 0;
        xyzDataset.removeAll();
    }
    
    public void SetChart3DSize(Dimension d) {
        chartPanel.zoomToFit(d);
        this.setPreferredSize(d);
    }
    
    private Chart3D chart;
    private Chart3DPanel chartPanel;
    private XYZSeriesCollection xyzDataset;
    private XYZPlot xyzPlot;
    int index = 0;
}
