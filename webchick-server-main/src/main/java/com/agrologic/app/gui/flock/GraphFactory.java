/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.flock;

import com.agrologic.app.model.DataFormat;
import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class GraphFactory {

    private static XYDataset createSeriesCollection() {
        XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection();
        return localXYSeriesCollection;
    }

    public static XYSeries createXYDataset(String seriesTitle, String values) {
        XYSeries series = new XYSeries(seriesTitle);
        if(values == null) {
            return series;
        }
        String[] vals = values.split(" ");
        for (int i = 0; i < vals.length; i++) {
            String vl = vals[i];
            Long value = Long.parseLong(vl);
            vl = DataFormat.formatToStringValue(DataFormat.DEC_1, value);
            series.add((double) (i + 1), Double.parseDouble(vl));
        }
        return series;
    }

    public static XYSeries createXYDataset(String seriesTitle, Map<Integer, Double> table) {
        XYSeries series = new XYSeries(seriesTitle);
        Iterator<Entry<Integer, Double>> iter = table.entrySet().iterator();

        while (iter.hasNext()) {
            Entry<Integer, Double> entry = iter.next();
            series.add(entry.getKey(), entry.getValue());
        }

        return series;
    }

    public static XYSeriesCollection createSeriesCollection(XYPlot plot, String seriesTitle, Map<Integer, Double> table) {
        int count = plot.getSeriesCount();
        XYSeriesCollection xydataset = (XYSeriesCollection) createSeriesCollection();
        xydataset.addSeries(createXYDataset(seriesTitle, table));
        return xydataset;
    }

    private static JFreeChart createXYChart(XYDataset xyDataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("History Graph",
                "Grow Day",
                " ",
                xyDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);

        XYPlot localXYPlot = (XYPlot) chart.getPlot();
        localXYPlot.setForegroundAlpha(0.65F);
        localXYPlot.setOrientation(PlotOrientation.VERTICAL);
        localXYPlot.setDomainCrosshairVisible(true);
        localXYPlot.setRangeCrosshairVisible(true);
        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(true);

        ValueAxis localValueAxis1 = localXYPlot.getDomainAxis();
        localValueAxis1.setTickMarkPaint(Color.black);

        LegendTitle legendTitle = (LegendTitle) chart.getSubtitle(0);
        legendTitle.setItemFont(new Font("Dialog", Font.PLAIN, 16));
        legendTitle.setPosition(RectangleEdge.BOTTOM);
        legendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
        return chart;
    }

    public static ChartPanel createXYChartPanel() {
        return new ChartPanel(createXYChart(createSeriesCollection()));
    }

    public static void setNumberAxis(String title, XYPlot plot) {
        NumberAxis numberAxis = (NumberAxis) plot.getDomainAxis();
        numberAxis.setLabel(title);
        numberAxis.setAutoRangeIncludesZero(false);
        numberAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        numberAxis.setLabelPaint(Color.BLACK);
        numberAxis.setTickLabelPaint(Color.BLACK);
        numberAxis.setLabelFont(new Font("Dialog", Font.BOLD, 16));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    }

    public static void setRangeAxis(String title, XYPlot plot) {
        ValueAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setLabel(title);
        valueAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        valueAxis.setLabelFont(new Font("Dialog", Font.BOLD, 16));
        valueAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
        valueAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
    }

    public static void setTopAndBottom(XYPlot plot) {
    }

    public static void setRenderer(XYPlot plot) {
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseSeriesVisible(true);
        if (renderer instanceof XYLineAndShapeRenderer) {
            Object localObject = (XYLineAndShapeRenderer) renderer;
            ((XYLineAndShapeRenderer) localObject).setBaseShapesVisible(true);
            ((XYLineAndShapeRenderer) localObject).setBaseShapesFilled(true);
        }
    }
}