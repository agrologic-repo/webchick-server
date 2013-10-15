

package com.agrologic.app.graph.example;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;


public class MultiAxisDemo1 extends ApplicationFrame {
    public MultiAxisDemo1(String paramString) {
        super(paramString);

        ChartPanel localChartPanel = (ChartPanel) createDemoPanel();

        localChartPanel.setPreferredSize(new Dimension(600, 270));
        localChartPanel.setDomainZoomable(true);
        localChartPanel.setRangeZoomable(true);
        setContentPane(localChartPanel);
    }

    private static JFreeChart createChart() {
        XYDataset localXYDataset1 = createDataset("Series 1", 100.0D, new Minute(), 200);
        JFreeChart localJFreeChart = ChartFactory.createTimeSeriesChart("Multiple Axis Demo 1", "Time of Day",
                "Primary Range Axis", localXYDataset1, true, true, false);

        localJFreeChart.addSubtitle(new TextTitle("Four datasets and four range axes."));

        XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();

        localXYPlot.setOrientation(PlotOrientation.VERTICAL);
        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(true);
        localXYPlot.getRangeAxis().setFixedDimension(15.0D);

//      NumberAxis localNumberAxis1 = new NumberAxis("Range Axis 2");
//      localNumberAxis1.setFixedDimension(10.0D);
//      localNumberAxis1.setAutoRangeIncludesZero(false);
//      localXYPlot.setRangeAxis(1, localNumberAxis1);
//      localXYPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
//      XYDataset localXYDataset2 = createDataset("Series 2", 1000.0D, new Minute(), 170);
//      localXYPlot.setDataset(1, localXYDataset2);
//      localXYPlot.mapDatasetToRangeAxis(1, 1);
//      StandardXYItemRenderer localStandardXYItemRenderer1 = new StandardXYItemRenderer();
//      localXYPlot.setRenderer(1, localStandardXYItemRenderer1);
//      NumberAxis localNumberAxis2 = new NumberAxis("Range Axis 3");
//      localXYPlot.setRangeAxis(2, localNumberAxis2);
//      XYDataset localXYDataset3 = createDataset("Series 3", 10000.0D, new Minute(), 170);
//      localXYPlot.setDataset(2, localXYDataset3);
//      localXYPlot.mapDatasetToRangeAxis(2, 2);
//      StandardXYItemRenderer localStandardXYItemRenderer2 = new StandardXYItemRenderer();
//      localXYPlot.setRenderer(2, localStandardXYItemRenderer2);
//      NumberAxis localNumberAxis3 = new NumberAxis("Range Axis 4");
//      localXYPlot.setRangeAxis(3, localNumberAxis3);
//      XYDataset localXYDataset4 = createDataset("Series 4", 25.0D, new Minute(), 200);
//      localXYPlot.setDataset(3, localXYDataset4);
//      localXYPlot.mapDatasetToRangeAxis(3, 3);
//      StandardXYItemRenderer localStandardXYItemRenderer3 = new StandardXYItemRenderer();
//      localXYPlot.setRenderer(3, localStandardXYItemRenderer3);
//      ChartUtilities.applyCurrentTheme(localJFreeChart);
//      localXYPlot.getRenderer().setSeriesPaint(0, Color.black);
//      localStandardXYItemRenderer1.setSeriesPaint(0, Color.red);
//      localNumberAxis1.setLabelPaint(Color.red);
//      localNumberAxis1.setTickLabelPaint(Color.red);
//      localStandardXYItemRenderer2.setSeriesPaint(0, Color.blue);
//      localNumberAxis2.setLabelPaint(Color.blue);
//      localNumberAxis2.setTickLabelPaint(Color.blue);
//      localStandardXYItemRenderer3.setSeriesPaint(0, Color.green);
//      localNumberAxis3.setLabelPaint(Color.green);
//      localNumberAxis3.setTickLabelPaint(Color.green);
        return localJFreeChart;
    }

    private static XYDataset createDataset(String paramString, double paramDouble,
                                           RegularTimePeriod paramRegularTimePeriod, int paramInt) {
        TimeSeries localTimeSeries = new TimeSeries(paramString);
        RegularTimePeriod localRegularTimePeriod = paramRegularTimePeriod;
        double d = paramDouble;

        for (int i = 0; i < paramInt; ++i) {
            localTimeSeries.add(localRegularTimePeriod, d);
            localRegularTimePeriod = localRegularTimePeriod.next();
            d *= (1.0D + (Math.random() - 0.495D) / 10.0D);
        }

        TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();

        localTimeSeriesCollection.addSeries(localTimeSeries);

        return localTimeSeriesCollection;
    }

    public static JPanel createDemoPanel() {
        JFreeChart localJFreeChart = createChart();
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);

        localChartPanel.setMouseWheelEnabled(true);

        return localChartPanel;
    }

    public static void main(String[] paramArrayOfString) {
        MultiAxisDemo1 localMultipleAxisDemo1 = new MultiAxisDemo1("JFreeChart: MultipleAxisDemo1.java");

        localMultipleAxisDemo1.pack();
        RefineryUtilities.centerFrameOnScreen(localMultipleAxisDemo1);
        localMultipleAxisDemo1.setVisible(true);
    }
}



