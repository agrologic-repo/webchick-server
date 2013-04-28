
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.graph.history;



import com.agrologic.app.model.DataFormat;
import com.agrologic.app.graph.daily.AbstractGraph;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.utils.DateLocal;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.date.SerialDate;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;

/**
 * Title: Graph24WaterConsumpTemp <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class HistoryGraph24FWT extends AbstractGraph {
    private int resetTime;

    public HistoryGraph24FWT(GraphType type, String values, Long currentTime) {
        super(type, values);
        this.currentTime = currentTime;
        chart            = createChart();
    }

    public HistoryGraph24FWT(GraphType type, String values, Long currentTime, Locale locale) {
        super(type, values);
        this.locale      = locale;
        this.currentTime = currentTime;
        initLaguage();

        // chart = createChart();
    }

    @Override
    public final JFreeChart createChart() {
        if (!isEmpty()) {
            DateAxis dateaxis = new DateAxis(dictinary.get("graph.fw.axis.time"));

            dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
            dateaxis.setLabelPaint(Color.BLACK);
            dateaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            dateaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            dateaxis.setVerticalTickLabels(false);

            NumberAxis feedwoterAxis = new NumberAxis(dictinary.get("graph.fw.axis.feedwater"));

            feedwoterAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            feedwoterAxis.setAutoRangeIncludesZero(true);
            feedwoterAxis.setLabelPaint(Color.RED);
            feedwoterAxis.setTickLabelPaint(Color.RED);
            feedwoterAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            feedwoterAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));

//          feedwoterAxis.setTickUnit(new NumberTickUnit(5.0D));
            // Create tooltip and URL generators
            SimpleDateFormat           dateFormat = new SimpleDateFormat();
            StandardXYToolTipGenerator ttg        =
                new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, dateFormat,
                                               NumberFormat.getInstance());
            TimeSeriesURLGenerator urlg     = new TimeSeriesURLGenerator(dateFormat, "", "series", "values");
            StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg,
                                                  urlg);

            renderer.setShapesFilled(true);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesPaint(1, Color.RED);

            NumberAxis tempretureAxis = new NumberAxis(dictinary.get("graph.fw.axis.temperature"));

            tempretureAxis.setAutoRangeIncludesZero(true);
            tempretureAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            tempretureAxis.setAutoRangeIncludesZero(true);
            tempretureAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            tempretureAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            tempretureAxis.setLabelPaint(new Color(33, 94, 33));
            tempretureAxis.setTickLabelPaint(new Color(33, 94, 33));

//          tempretureAxis.setTickUnit(new NumberTickUnit(5.0D));

            XYPlot plot = new XYPlot(createWaterAndFeedConsumpDataset(), dateaxis, feedwoterAxis, renderer);

            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxis(1, tempretureAxis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            feedwoterAxis.setLowerBound(minY - 10);
            feedwoterAxis.setUpperBound(maxY + 10);

            XYDataset xydataset1 = createTempDataset();

            plot.setDataset(1, xydataset1);
            plot.mapDatasetToRangeAxis(1, 1);
            tempretureAxis.setLowerBound(minY - 1);
            tempretureAxis.setUpperBound(maxY + 1);

            StandardXYItemRenderer renderer2 = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg,
                                                   urlg);

            renderer2.setShapesFilled(true);
            renderer2.setBaseShapesVisible(true);
            renderer2.setBaseShapesFilled(true);
            renderer2.setSeriesPaint(0, new Color(33, 94, 33));
            plot.setRenderer(1, renderer2);
            plot.setBackgroundPaint(Color.WHITE);

            // set tooltip
            chart = new JFreeChart(dictinary.get("graph.fw.title"), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBorderPaint(Color.BLACK);
            chart.setBackgroundPaint(java.awt.Color.LIGHT_GRAY);

            LegendTitle legendTitle = (LegendTitle) chart.getSubtitle(0);

            legendTitle.setItemFont(new Font("Dialog", Font.PLAIN, 16));
            legendTitle.setPosition(RectangleEdge.TOP);
            legendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
        } else {
            final XYPlot xyplot = new XYPlot();

            xyplot.setNoDataMessage("No data available!");
            xyplot.setNoDataMessageFont(new Font("Serif", 2, 15));
            xyplot.setNoDataMessagePaint(Color.red);
            xyplot.setDomainCrosshairVisible(true);
            xyplot.setRangeCrosshairVisible(true);
            xyplot.setDomainPannable(true);
            xyplot.setRangePannable(true);
            chart = new JFreeChart(dictinary.get("graph.fw.title"), JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        }

        return chart;
    }

    private void setUpperAndLowerBound(String label) {
        if (label.equals("Temperature [C]")) {
            final XYPlot xyplot = chart.getXYPlot();
            int axisCount = xyplot.getRangeAxisCount();
            for (int i = 0; i < axisCount; i++) {
                NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
                if (numberaxis.getLabel().equals(label)) {
                    numberaxis.setLowerBound(0);
                    numberaxis.setUpperBound(50);
                }
            }
        }
    }

    private XYDataset createTempDataset() {
        resetMinMaxY();

        DateLocal                  now                  = DateLocal.now();
        DateLocal                  yday                 = now.addDays(-1);
        int                        day                  = now.getDate();
        int                        month                = now.getMonth();
        int                        year                 = now.getYear();
        int                        hour                 = now.getHours() - 1;
        Day                        today                = new Day(SerialDate.createInstance(day, month, year));
        Day                        yesterday            = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        final TimeSeries           timeseries           = new TimeSeries(dictinary.get("graph.fw.series.inside"));

        // testing
        int hr = (int) (currentTime / 100) - 1;

        for (int i = IN_TEMP_INDEX + DAY_HOURS - 1; i >= IN_TEMP_INDEX; i--, hr--) {
            String value      = DataFormat.formatToStringValue(DataFormat.DEC_1, Long.valueOf(datasetString[i]));
            float  floatValue = Float.valueOf(value);
            timeseries.add(new Hour(hr, today), floatValue);
            if (hr == 0) {
                today = yesterday;
                hr    = DAY_HOURS;
            }
            if (maxY < floatValue) {
                maxY = (int) floatValue;
            }
            if (minY > floatValue) {
                minY = (int) floatValue;
            }
        }

        timeSeriesCollection.addSeries(timeseries);

        return timeSeriesCollection;
    }

    private XYDataset createWaterAndFeedConsumpDataset() {
        DateLocal                  now                  = DateLocal.now();
        DateLocal                  yday                 = now.addDays(-1);
        int                        day                  = now.getDate();
        int                        month                = now.getMonth();
        int                        year                 = now.getYear();
        int                        hour                 = now.getHours() - 1;
        Day                        today                = new Day(SerialDate.createInstance(day, month, year));
        Day                        yesterday            = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        final TimeSeries           timeseries1          = new TimeSeries(dictinary.get("graph.fw.series.water"));

        resetMinMaxY();
        int hr = (int) (currentTime / 100) - 1;

        for (int i = WATER_INDEX + DAY_HOURS - 1; i >= WATER_INDEX; i--, hr--) {
            String value    = DataFormat.formatToStringValue(DataFormat.DEC_0, Long.valueOf(datasetString[i]));
            int    intValue = Integer.valueOf(value);
            timeseries1.add(new Hour(hr, today), intValue);
            if (hr == 0) {
                today = yesterday;
                hr    = DAY_HOURS;
            }
            if (maxY < intValue) {
                maxY = intValue;
            }
            if (minY > intValue) {
                minY = intValue;
            }
        }

        hour  = now.getHours() - 1;                                      // set hour
        today = new Day(SerialDate.createInstance(day, month, year));    // set day
        hr    = (int) (currentTime / 100) - 1;
        final TimeSeries timeseries2 = new TimeSeries(dictinary.get("graph.fw.series.feed"));
        for (int i = FEED_INDEX + DAY_HOURS - 1; i >= FEED_INDEX; i--, hr--) {
            String value    = DataFormat.formatToStringValue(DataFormat.DEC_0, Long.valueOf(datasetString[i]));
            int    intValue = Integer.valueOf(value);
            timeseries2.add(new Hour(hr, today), intValue);
            if (hr == 0) {
                today = yesterday;
                hr    = DAY_HOURS;
            }

            if (maxY < intValue) {
                maxY = intValue;
            }
            if (minY > intValue) {
                minY = intValue;
            }
        }
        timeSeriesCollection.addSeries(timeseries1);
        timeSeriesCollection.addSeries(timeseries2);
        return timeSeriesCollection;
    }
}
