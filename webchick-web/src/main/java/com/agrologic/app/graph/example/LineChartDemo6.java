
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.graph.example;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

//~--- JDK imports ------------------------------------------------------------

public class LineChartDemo6 extends ApplicationFrame {
    private JFreeChart chart;
    private XYSeriesCollection dataset;
    private List<XYSeriesCollection> datasetList;

    /**
     * Creates a new demo.
     *
     * @param title the frame title.
     */
    public LineChartDemo6(final String title) {
        super(title);
    }

    public void creatPanel() {
        final ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * @param coordinates
     * @param title
     */
    private void addSeries(final Map<Double, Double> coordinates, final String title) {
        if (dataset == null) {
            dataset = new XYSeriesCollection();
        }

        XYSeries series = new XYSeries(title);
        Iterator iter = coordinates.entrySet().iterator();

        while (iter.hasNext()) {
            Entry<Double, Double> entry = (Entry<Double, Double>) iter.next();
            double x = entry.getKey();
            double y = entry.getValue();

            series.add(x, y);
        }

        dataset.addSeries(series);
    }

    /**
     * @param coordinates
     * @param title
     */
    private XYSeries createSeries(final Map<Double, Double> coordinates, final String title) {
        XYSeries series = new XYSeries(title);
        Iterator iter = coordinates.entrySet().iterator();

        while (iter.hasNext()) {
            Entry<Double, Double> entry = (Entry<Double, Double>) iter.next();
            double x = entry.getKey();
            double y = entry.getValue();

            series.add(x, y);
        }

        return series;

        // dataset.addSeries(series);
    }

    private void addDataset(XYSeriesCollection dataset) {
        if (datasetList == null) {
            datasetList = new ArrayList<XYSeriesCollection>();
        }

        datasetList.add(dataset);
    }

    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @return a chart.
     */
    private void createChart() {

        // create the chart...
        chart = ChartFactory.createXYLineChart("Line Chart Demo 6",    // chart title
                "X",                                                   // x axis label
                "Y",                                                   // y axis label
                dataset,                                               // data
                PlotOrientation.VERTICAL, true,                        // include legend
                true,                                                  // tooltips
                false                                                  // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        // rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

//      renderer.setSeriesStroke(0, new BasicStroke(1.5F));
//      renderer.setSeriesStroke(1, new BasicStroke(1.5F));
//      renderer.setSeriesStroke(2, new BasicStroke(1.5F));
//      renderer.setSeriesStroke(3, new BasicStroke(1.5F));
//      renderer.setSeriesStroke(4, new BasicStroke(1.5F));
//      renderer.setSeriesStroke(5, new BasicStroke(1.5F));
//      XYPointerAnnotation localXYPointerAnnotation1 = new XYPointerAnnotation("First Max = 80.0 ", 8.0D, 80.0D, 1.57D);
//      localXYPointerAnnotation1.setLabelOffset(4.0D);
//      localXYPointerAnnotation1.setTextAnchor(TextAnchor.TOP_CENTER);
//      localXYPointerAnnotation1.setPaint(Color.red);
//      localXYPointerAnnotation1.setArrowPaint(Color.red);
//      localXYPointerAnnotation1.setBackgroundPaint(Color.yellow);
//      renderer.addAnnotation(localXYPointerAnnotation1);
//
//      XYPointerAnnotation localXYPointerAnnotation4 = new XYPointerAnnotation("First Min = 30.0 ", 3.0D, 30.0D, -1.57D);
//      localXYPointerAnnotation4.setLabelOffset(4.0D);
//      localXYPointerAnnotation4.setTextAnchor(TextAnchor.TOP_CENTER);
//      localXYPointerAnnotation4.setPaint(Color.red);
//      localXYPointerAnnotation4.setArrowPaint(Color.red);
//      localXYPointerAnnotation4.setBackgroundPaint(Color.yellow);
//      renderer.addAnnotation(localXYPointerAnnotation4);
//
//      XYPointerAnnotation localXYPointerAnnotation2 = new XYPointerAnnotation("Second Max = 85.0 ", 4.0D, 85.0D, 1.57D);
//      localXYPointerAnnotation2.setLabelOffset(4.0D);
//      localXYPointerAnnotation2.setTextAnchor(TextAnchor.TOP_CENTER);
//      localXYPointerAnnotation2.setPaint(Color.blue);
//      localXYPointerAnnotation2.setArrowPaint(Color.blue);
//      localXYPointerAnnotation2.setBackgroundPaint(new Color(0, 255, 255, 255));
//      renderer.addAnnotation(localXYPointerAnnotation2);
//
//      XYPointerAnnotation localXYPointerAnnotation5 = new XYPointerAnnotation("Second Min = 35.0 ", 8.0D, 35.0D, -1.57D);
//      localXYPointerAnnotation5.setLabelOffset(4.0D);
//      localXYPointerAnnotation5.setTextAnchor(TextAnchor.TOP_CENTER);
//      localXYPointerAnnotation5.setPaint(Color.blue);
//      localXYPointerAnnotation5.setArrowPaint(Color.blue);
//      localXYPointerAnnotation5.setBackgroundPaint(new Color(0, 255, 255, 255));
//      renderer.addAnnotation(localXYPointerAnnotation5);
//
//      XYPointerAnnotation localXYPointerAnnotation3 = new XYPointerAnnotation("Third Max = 65.0 ", 7.0D, 65.0D, 1.5D);
//      localXYPointerAnnotation3.setLabelOffset(4.0D);
//      localXYPointerAnnotation3.setTextAnchor(TextAnchor.TOP_CENTER );
//      localXYPointerAnnotation3.setPaint(Color.green);
//      localXYPointerAnnotation3.setArrowPaint(Color.green);
//      localXYPointerAnnotation3.setBackgroundPaint(new Color( 0, 0, 0));
//      renderer.addAnnotation(localXYPointerAnnotation3);        // OPTIONAL CUSTOMISATION COMPLETED.
//
//      XYPointerAnnotation localXYPointerAnnotation6 = new XYPointerAnnotation("Third Min = 25.0 ", 5.0D, 25.0D, -1.5D);
//      localXYPointerAnnotation6.setLabelOffset(4.0D);
//      localXYPointerAnnotation6.setTextAnchor(TextAnchor.TOP_CENTER );
//      localXYPointerAnnotation6.setPaint(Color.green);
//      localXYPointerAnnotation6.setArrowPaint(Color.green);
//      localXYPointerAnnotation6.setBackgroundPaint(new Color( 0, 0, 0));
//      renderer.addAnnotation(localXYPointerAnnotation6);        // OPTIONAL CUSTOMISATION COMPLETED.
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {
        Map<Double, Double> coordinates = new TreeMap<Double, Double>();
        final LineChartDemo6 demo = new LineChartDemo6("Line Chart Demo 6");

        coordinates.put(1.0, 60.0);
        coordinates.put(2.0, 40.5);
        coordinates.put(3.0, 30.0);
        coordinates.put(4.0, 50.0);
        coordinates.put(5.0, 50.5);
        coordinates.put(6.0, 70.0);
        coordinates.put(7.0, 70.0);
        coordinates.put(8.0, 80.0);
        demo.addSeries(coordinates, "first");
        coordinates.clear();
        coordinates.put(1.0, 55.0);
        coordinates.put(2.0, 75.0);
        coordinates.put(3.0, 65.5);
        coordinates.put(4.0, 85.0);
        coordinates.put(5.0, 45.0);
        coordinates.put(6.0, 45.5);
        coordinates.put(7.0, 45.0);
        coordinates.put(8.0, 35.5);
        demo.addSeries(coordinates, "second");
        coordinates.clear();
        coordinates.put(3.0, 45.5);
        coordinates.put(4.0, 35.0);
        coordinates.put(5.0, 25.0);
        coordinates.put(6.0, 35.0);
        coordinates.put(7.0, 65.0);
        coordinates.put(8.0, 35.0);
        coordinates.put(9.0, 45.5);
        coordinates.put(10.0, 53.0);
        demo.addSeries(coordinates, "third");
        demo.addDataset(null);
        demo.createChart();
        demo.creatPanel();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}



