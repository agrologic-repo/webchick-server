package com.agrologic.app.graph.daily;

import com.agrologic.app.model.DataFormat;
import com.agrologic.app.util.DateLocal;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.date.SerialDate;

import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FeedWaterConsumpTemp extends PerHourReportGraph {

    public FeedWaterConsumpTemp(String values, Long currentTime, Locale locale) {
        super(values);
        this.currentTime = currentTime;
        this.locale = locale;
        initLanguage();
    }

    @Override
    public final JFreeChart createChart() {
        if (!isEmpty()) {
            DateAxis dateaxis = new DateAxis(dictinary.get("graph.feed.water.axis.time"));
            dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
            dateaxis.setLabelPaint(Color.BLACK);
            dateaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            dateaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            dateaxis.setVerticalTickLabels(false);

            NumberAxis numberaxis = new NumberAxis(dictinary.get("graph.feed.water.axis.feed"));
            numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            numberaxis.setAutoRangeIncludesZero(true);
            numberaxis.setLabelPaint(Color.RED);
            numberaxis.setTickLabelPaint(Color.RED);
            numberaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            numberaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));

            // Create tooltip and URL generators
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, dateFormat, NumberFormat.getInstance());
            TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(dateFormat, "", "series", "values");
            StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);
            renderer.setShapesFilled(true);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Color.RED);

            NumberAxis waterAxis = new NumberAxis(dictinary.get("graph.feed.water.axis.water"));
            numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            waterAxis.setAutoRangeIncludesZero(true);
            waterAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            waterAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            waterAxis.setLabelPaint(Color.BLUE);
            waterAxis.setTickLabelPaint(Color.BLUE);

            XYPlot plot = new XYPlot(createFeedDataset(), dateaxis, numberaxis, renderer);
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxis(1, waterAxis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            numberaxis.setLowerBound(minY - 10);
            numberaxis.setUpperBound(maxY + 10);

            XYDataset xywaterdataset = createWaterDataset();
            plot.setDataset(1, xywaterdataset);
            plot.mapDatasetToRangeAxis(1, 1);
            waterAxis.setLowerBound(minY - 1);
            waterAxis.setUpperBound(maxY + 1);

            StandardXYItemRenderer renderer2 = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES,
                    ttg, urlg);

            renderer2.setShapesFilled(true);
            renderer2.setBaseShapesVisible(true);
            renderer2.setBaseShapesFilled(true);
            renderer2.setSeriesPaint(0, Color.BLUE);
            plot.setRenderer(1, renderer2);
            plot.setBackgroundPaint(Color.WHITE);

            // set tooltip
            chart = new JFreeChart(dictinary.get("graph.feed.water.title"), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBorderPaint(Color.BLACK);
            chart.setBackgroundPaint(java.awt.Color.LIGHT_GRAY);
            changeLegendFont();

        } else {
            final XYPlot xyplot = new XYPlot();
            xyplot.setNoDataMessage(dictinary.get("graph.no.data.available"));
            xyplot.setNoDataMessageFont(new Font("Serif", 2, 15));
            xyplot.setNoDataMessagePaint(Color.red);
            xyplot.setDomainCrosshairVisible(true);
            xyplot.setRangeCrosshairVisible(true);
            xyplot.setDomainPannable(true);
            xyplot.setRangePannable(true);
            chart = new JFreeChart(dictinary.get("graph.feed.water.title"), JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        }
        return chart;
    }

    private XYDataset createFeedDataset() {
        resetMinMaxY();
        DateLocal now = DateLocal.now();
        DateLocal yday = now.addDays(-1);
        int day = now.getDate();
        int month = now.getMonth();
        int year = now.getYear();
        Day today = new Day(SerialDate.createInstance(day, month, year));
        Day yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        final TimeSeries feedSeries = new TimeSeries(dictinary.get("graph.feed.water.series.feed"));
        int hour = (int) (currentTime / 100) - 1;

        for (int i = FEED_INDEX + DAY_HOURS - 1; i >= FEED_INDEX; i--, hour--) {
            String value = DataFormat.formatToStringValue(DataFormat.DEC_0, Long.valueOf(datasetString[i]));
            int intValue = Integer.valueOf(value);

            feedSeries.add(new Hour(hour, today), intValue);
            if (hour == 0) {
                today = yesterday;
                hour = DAY_HOURS;
            }

            if (maxY < intValue) {
                maxY = intValue;
            }

            if (minY > intValue) {
                minY = intValue;
            }
        }
        timeSeriesCollection.addSeries(feedSeries);
        return timeSeriesCollection;
    }

    private XYDataset createWaterDataset() {
        DateLocal now = DateLocal.now();
        DateLocal yday = now.addDays(-1);
        int day = now.getDate();
        int month = now.getMonth();
        int year = now.getYear();
        int hour = now.getHours() - 1;
        Day today = new Day(SerialDate.createInstance(day, month, year));
        Day yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        final TimeSeries timeseries1 = new TimeSeries(dictinary.get("graph.feed.water.series.water"));

        resetMinMaxY();

        int hr = (int) (currentTime / 100) - 1;
        for (int i = WATER_INDEX + DAY_HOURS - 1; i >= WATER_INDEX; i--, hr--) {
            String value = DataFormat.formatToStringValue(DataFormat.DEC_0, Long.valueOf(datasetString[i]));
            int intValue = Integer.valueOf(value);

            timeseries1.add(new Hour(hr, today), intValue);

            if (hr == 0) {
                today = yesterday;
                hr = DAY_HOURS;
            }

            if (maxY < intValue) {
                maxY = intValue;
            }

            if (minY > intValue) {
                minY = intValue;
            }
        }

        timeSeriesCollection.addSeries(timeseries1);

        return timeSeriesCollection;
    }
}



