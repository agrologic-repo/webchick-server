package com.agrologic.app.graph.daily;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.util.DateLocal;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
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
//import org.jfree.ui.RectangleEdge;
//import org.jfree.ui.RectangleInsets;
//import org.jfree.util.UnitType;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HourHistoryGraph extends PerHourReportGraph {

    private static final int HOURS = 24;
    private static final int INDEX = 0;

    public HourHistoryGraph(String values, String label, Integer format, Long currentTime, Locale locale) {
        super(values, label, format);
        this.currentTime = currentTime;
        this.locale = locale;
        initLanguage();
    }

    @Override
    public final JFreeChart createChart() {// format
        if (!isEmpty()) {
            DateAxis dateaxis = new DateAxis(dictinary.get("graph.time.hour"));
            dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
            dateaxis.setLabelPaint(Color.BLACK);
            dateaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            dateaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            dateaxis.setVerticalTickLabels(false);

            NumberAxis number_axis = new NumberAxis(label);

            number_axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            number_axis.setAutoRangeIncludesZero(true);
            number_axis.setLabelPaint(Color.RED);
            number_axis.setTickLabelPaint(Color.RED);
            number_axis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            number_axis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));

            // Create tooltip and URL generators
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, dateFormat, NumberFormat.getInstance());
            TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(dateFormat, "", "series", "values");
            StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);

            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.BLUE);

//            NumberAxis humidityAxis = new NumberAxis(dictinary.get("graph.inside.outside.humidity.axis.humidity"));
//            humidityAxis.setAutoRangeIncludesZero(true);
//            humidityAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
//            humidityAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
//            humidityAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
//            humidityAxis.setLabelPaint(new Color(33, 94, 33));
//            humidityAxis.setTickLabelPaint(new Color(33, 94, 33));
//            humidityAxis.setLowerBound(-1);
//            humidityAxis.setUpperBound(101);
//            humidityAxis.setTickUnit(new NumberTickUnit(5.0D));

            XYPlot plot = new XYPlot(createHourDataSet(format), dateaxis, number_axis, renderer);/////////////////////////////

            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxis(1, number_axis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);


//            XYDataset data_set = createHumidityDataSet();////////////////////////////////////

//            plot.setDataset(1, data_set);
//            plot.mapDatasetToRangeAxis(1, 1);

//            StandardXYItemRenderer renderer2 = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);

//            renderer2.setShapesFilled(true);
//            renderer2.setBaseShapesVisible(true);
//            renderer2.setBaseShapesFilled(true);
//            renderer2.setSeriesPaint(0, new Color(33, 94, 33));
//            plot.setRenderer(1, renderer2);
//            plot.setBackgroundPaint(Color.WHITE);

            // set tooltip
            chart = new JFreeChart(label, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBorderPaint(Color.BLACK);
            chart.setBackgroundPaint(java.awt.Color.LIGHT_GRAY);
            changeLegendFont();
            number_axis.setLowerBound(minY - 10);
            number_axis.setUpperBound(maxY + 10);
        } else {
            final XYPlot xyplot = new XYPlot();
            xyplot.setNoDataMessage(dictinary.get("graph.no.data.available"));
            xyplot.setNoDataMessageFont(new Font("Serif", 2, 15));
            xyplot.setNoDataMessagePaint(Color.red);
            xyplot.setDomainCrosshairVisible(true);
            xyplot.setRangeCrosshairVisible(true);
            xyplot.setDomainPannable(true);
            xyplot.setRangePannable(true);
            chart = new JFreeChart(label, JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        }

        // hideSeries(1, 0);
        return chart;
    }

    private XYDataset createHourDataSet(Integer format) {
        resetMinMaxY();
     TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
            if (isEmpty()) {
                return timeSeriesCollection;
            } else {
                DateLocal now = DateLocal.now();
                DateLocal yday = now.addDays(-1);
                Day today = new Day(SerialDate.createInstance(now.getDate(), now.getMonth(), now.getYear()));
                Day yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
                int hour = (int) (currentTime / 100) - 1;
//                final TimeSeries series = new TimeSeries(dictinary.get("graph.inside.outside.humidity.series.inside"));
                TimeSeries series = new TimeSeries(label);
                for (int i = INDEX + HOURS - 1; i >= INDEX; i--, hour--) {
                    String value = DataFormat.formatToStringValue(format, Long.valueOf(datasetString[i]));////////////////////
                    float floatValue = Float.valueOf(value);
//                    Number value = value_by_type();
//                    series.add(new Hour(hour, today), Float.valueOf(value));//Number y = valueByType(entry.getValue());//HistoryGraph <--- float value
                    series.add(new Hour(hour, today), Float.valueOf(value));
                    if (hour == 0) {
                        today = yesterday;
                        hour = HOURS;    // testing
                    }
                    if (maxY < floatValue) {
                        maxY = (int) floatValue;
                    }
                    if (minY > floatValue) {
                        minY = (int) floatValue;
                    }
                }
//                today = new Day(SerialDate.createInstance(now.getDate(), now.getMonth(), now.getYear()));

                // testing
//                hour = (int) (currentTime / 100) - 1;
                timeSeriesCollection.addSeries(series);
                return timeSeriesCollection;
            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        return null;
    }

    private XYDataset create_data_set() {
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

        if (isEmpty()) {
            return timeSeriesCollection;
        } else {
            DateLocal now = DateLocal.now();
            DateLocal yday = now.addDays(-1);
            int day = now.getDate();
            int month = now.getMonth();
            int year = now.getYear();
            Day today = new Day(SerialDate.createInstance(day, month, year));
            Day yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));

            // testing
            int hour = (int) (currentTime / 100) - 1;
            final TimeSeries humidityseries = new TimeSeries(dictinary.get("graph.inside.outside.humidity.series.humidity"));

            for (int i = HUMIDITY_INDEX + DAY_HOURS - 1; i >= HUMIDITY_INDEX; i--, hour--) {
                String value = DataFormat.formatToStringValue(DataFormat.HUMIDITY, Long.valueOf(datasetString[i]));
                int intValue = Integer.valueOf(value);

                humidityseries.add(new Hour(hour, today), intValue);

                if (hour == 0) {
                    today = yesterday;
                    hour = DAY_HOURS;
                }

                if (maxY < intValue) {
                    maxY = 100;
                }

                if (minY > intValue) {
                    minY = 0;
                }
            }

            timeSeriesCollection.addSeries(humidityseries);

            return timeSeriesCollection;
        }
    }

}
