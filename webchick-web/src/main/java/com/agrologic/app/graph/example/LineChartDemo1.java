
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.graph.example;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;

import java.text.AttributedString;

public class LineChartDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public LineChartDemo1(final String title) {
        super(title);

        final CategoryDataset dataset    = createDataset();
        final JFreeChart      chart      = createChart(dataset);
        final ChartPanel      chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset() {

        // row keys...
        final String series1 = "In Temp";
        final String series2 = "Out Temp";
        final String series3 = "Humidity";

        // column keys...
        String[]     types     = new String[] {
            " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8"
        };
        double[]     inValues  = new double[] {
            25.0, 24.0, 23.0, 25.0, 27.0, 26.0, 25.0, 24.0
        };
        double[]     outValues = new double[] {
            25.0, 24.0, 23.0, 25.0, 27.0, 26.0, 25.0, 24.0
        };
        double[]     hValues   = new double[] {
            25.0, 24.0, 25.0, 24.0, 27.0, 26.0, 23.0, 25.0
        };
        final String type1     = " 1";
        final String type2     = " 2";
        final String type3     = " 3";
        final String type4     = " 4";
        final String type5     = " 5";
        final String type6     = " 6";
        final String type7     = " 7";
        final String type8     = " 8";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(25.0, series1, type1);
        dataset.addValue(24.0, series1, type2);
        dataset.addValue(23.0, series1, type3);
        dataset.addValue(25.0, series1, type4);
        dataset.addValue(25.0, series1, type5);
        dataset.addValue(27.0, series1, type6);
        dataset.addValue(27.0, series1, type7);
        dataset.addValue(28.0, series1, type8);
        dataset.addValue(15.0, series2, type1);
        dataset.addValue(17.0, series2, type2);
        dataset.addValue(16.0, series2, type3);
        dataset.addValue(18.0, series2, type4);
        dataset.addValue(14.0, series2, type5);
        dataset.addValue(14.0, series2, type6);
        dataset.addValue(12.0, series2, type7);
        dataset.addValue(11.0, series2, type8);
        dataset.addValue(24.0, series3, type1);
        dataset.addValue(23.0, series3, type2);
        dataset.addValue(22.0, series3, type3);
        dataset.addValue(23.0, series3, type4);
        dataset.addValue(26.0, series3, type5);
        dataset.addValue(23.0, series3, type6);
        dataset.addValue(24.0, series3, type7);
        dataset.addValue(23.0, series3, type8);

        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  a dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart("Line Chart Demo 1",    // chart title
            "Type",                                                                   // domain axis label
            "Value",                                                                  // range axis label
            dataset,                                                                  // data
            PlotOrientation.VERTICAL,                                                 // orientation
            true,                                                                     // include legend
            true,                                                                     // tooltips
            false                                                                     // urls
                );

        ((CategoryPlot) chart.getPlot()).setFixedLegendItems(getPrincipalAndInterestLegendItemCollection());

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
//      final StandardLegend legend = (StandardLegend) chart.getLegend();
//      legend.setDisplaySeriesShapes(true);
//      legend.setShapeScaleX(1.5);
//      legend.setShapeScaleY(1.5);
//      legend.setDisplaySeriesLines(true);
        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.setBackgroundPaint(Color.lightGray);
        plot.setNoDataMessagePaint(Color.white);

        // get a reference to the plot for further customisation...
//      final XYPlot plot = chart.getXYPlot();
//      plot.setBackgroundPaint(Color.lightGray);
//          plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
//      plot.setDomainGridlinePaint(Color.white);
//      plot.setRangeGridlinePaint(Color.white);
        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        // ****************************************************************************
        // * JFREECHART DEVELOPER GUIDE                                               *
        // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
        // * to purchase from Object Refinery Limited:                                *
        // *                                                                          *
        // * http://www.object-refinery.com/jfreechart/guide.html                     *
        // *                                                                          *
        // * Sales are used to provide funding for the JFreeChart project - please    *
        // * support us so that we can continue developing free software.             *
        // ****************************************************************************
        // customise the renderer...
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
    }

    private LegendItemCollection getPrincipalAndInterestLegendItemCollection() {
        LegendItemCollection result             = new LegendItemCollection();
        Shape                shape              = new Rectangle(10, 10);
        AttributedString     label              = new AttributedString("Interest");
        LegendItem           interestLegendItem = new LegendItem(label, null, null, null, shape, Color.RED);

        label = new AttributedString("Principal");

        LegendItem principalLegendItem = new LegendItem(label, null, null, null, shape, Color.BLUE);

        result.add(interestLegendItem);
        result.add(principalLegendItem);

        return result;
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
        final LineChartDemo1 demo = new LineChartDemo1("Line Chart Demo");

        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}



