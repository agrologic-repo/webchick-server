package com.agrologic.app.gui.rxtx.graph;

import com.agrologic.app.model.DataFormat;
import com.agrologic.app.util.DateLocal;
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
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kristina on 12/12/2016.
 */
public class Graph24WS extends AbstractGraph {
    public Graph24WS(GraphType type, String values, Long currentTime, Locale locale) {
        super(type, values);
        this.currentTime = currentTime;
        this.locale = locale;
        initLanguage();
    }

    @Override
    public final JFreeChart createChart() {
        DateAxis dateaxis;
        NumberAxis numberaxis;
        SimpleDateFormat dateFormat;
        StandardXYToolTipGenerator ttg;
        TimeSeriesURLGenerator urlg;
        StandardXYItemRenderer renderer;
        XYPlot plot;
        LegendTitle legendTitle;
        final XYPlot xyplot;
        if (!isEmpty()) {
            dateaxis = new DateAxis(dictionary.get("graph.ws.axis.time"));
            dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
            dateaxis.setLabelPaint(Color.BLACK);
            dateaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            dateaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            dateaxis.setVerticalTickLabels(false);
            numberaxis = new NumberAxis(dictionary.get("graph.ws.series.water.sum"));
            numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            numberaxis.setAutoRangeIncludesZero(true);
            numberaxis.setLabelPaint(Color.RED);
            numberaxis.setTickLabelPaint(Color.RED);
            numberaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            numberaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            // Create tooltip and URL generators
            dateFormat = new SimpleDateFormat();
            ttg = new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, dateFormat, NumberFormat.getInstance());
            urlg = new TimeSeriesURLGenerator(dateFormat, "", "series", "values");
            renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);
            renderer.setShapesFilled(true);
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            // renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesPaint(0, Color.RED);
            plot = new XYPlot(createWSDataset(), dateaxis, numberaxis, renderer);
            // XYPlot plot = new XYPlot(createWaterAndFeedConsumpDataset(), dateaxis, numberaxis, renderer);
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            numberaxis.setLowerBound(minY - 10);
            numberaxis.setUpperBound(maxY + 10);
            StandardXYItemRenderer renderer2 = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);
            renderer2.setShapesFilled(true);
            renderer2.setBaseShapesVisible(true);
            renderer2.setBaseShapesFilled(true);
            renderer2.setSeriesPaint(0, Color.BLUE);
            plot.setRenderer(1, renderer2);
            plot.setBackgroundPaint(Color.WHITE);
            // set tooltip
            chart = new JFreeChart(dictionary.get("graph.ws.title"), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBorderPaint(Color.BLACK);
            chart.setBackgroundPaint(java.awt.Color.LIGHT_GRAY);
            legendTitle = (LegendTitle) chart.getSubtitle(0);
            legendTitle.setItemFont(new Font("Dialog", Font.PLAIN, 16));
            legendTitle.setPosition(RectangleEdge.TOP);
            legendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
        } else {
            xyplot = new XYPlot();
            xyplot.setNoDataMessage("No data available!");
            xyplot.setNoDataMessageFont(new Font("Serif", 2, 15));
            xyplot.setNoDataMessagePaint(Color.red);
            xyplot.setDomainCrosshairVisible(true);
            xyplot.setRangeCrosshairVisible(true);
            xyplot.setDomainPannable(true);
            xyplot.setRangePannable(true);
            chart = new JFreeChart(dictionary.get("graph.ws.title"), JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        }
        return chart;
    }

    private XYDataset createWSDataset() {
        DateLocal now;
        DateLocal yday;
        int day;
        int month;
        int year;
        int hour;
        Day today;
        Day yesterday;
        final TimeSeriesCollection timeSeriesCollection;
        final TimeSeries feedSeries;
        int hr;
        resetMinMaxY();
        now = DateLocal.now();
        yday = now.addDays(-1);
        day = now.getDate();
        month = now.getMonth();
        year = now.getYear();
        hour = now.getHours() - 1;
        today = new Day(SerialDate.createInstance(day, month, year));
        yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
        timeSeriesCollection = new TimeSeriesCollection();
        feedSeries = new TimeSeries(dictionary.get("graph.ws.series.water.sum"));
        hr = (int) (currentTime / 100);
//        int hr = (int) (currentTime / 100);
        for (int i = FEED_CONS_PER_BIRD + DAY_HOURS - 1; i >= FEED_CONS_PER_BIRD; i--, hr--) {
            String value = DataFormat.formatToStringValue(DataFormat.DEC_0, Long.valueOf(datasetString[i]));
            int intValue = Integer.valueOf(value);
            feedSeries.add(new Hour(hr, today), intValue);
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
        timeSeriesCollection.addSeries(feedSeries);
        return timeSeriesCollection;
    }
}
