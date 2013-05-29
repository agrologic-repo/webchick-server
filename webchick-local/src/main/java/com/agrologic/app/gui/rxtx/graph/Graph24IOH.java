
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx.graph;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.DataFormat;
import com.agrologic.app.util.DateLocal;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
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

public class Graph24IOH extends AbstractGraph {

    int rendi, seri;

    public Graph24IOH(GraphType type, String values, Long currnetTime) {
        super(type, values);
        this.currentTime = currnetTime;
        chart = createChart();
    }

    public Graph24IOH(GraphType type, String values, Long currnetTime, Locale locale) {
        super(type, values);
        this.currentTime = currnetTime;
        this.locale = locale;
        initLanguage();

        // chart = createChart();
    }

    @Override
    public final JFreeChart createChart() {
        if (!isEmpty()) {
            DateAxis dateaxis = new DateAxis(dictionary.get("graph.ioh.axis.time"));
            dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
            dateaxis.setLabelPaint(Color.BLACK);
            dateaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            dateaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            dateaxis.setVerticalTickLabels(false);

            NumberAxis tempAxis = new NumberAxis(dictionary.get("graph.ioh.axis.temperature"));
            tempAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            tempAxis.setAutoRangeIncludesZero(true);
            tempAxis.setLabelPaint(Color.RED);
            tempAxis.setTickLabelPaint(Color.RED);
            tempAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            tempAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));

            // Create tooltip and URL generators
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            StandardXYToolTipGenerator ttg =
                    new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, dateFormat,
                            NumberFormat.getInstance());
            TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(dateFormat, "", "series", "values");
            StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg,
                    urlg);

            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.BLUE);

            // ////////////////////////////////////////////////////
            NumberAxis humidityAxis = new NumberAxis(dictionary.get("graph.ioh.axis.humidity"));

            humidityAxis.setAutoRangeIncludesZero(true);
            humidityAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            humidityAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            humidityAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
            humidityAxis.setLabelPaint(new Color(33, 94, 33));
            humidityAxis.setTickLabelPaint(new Color(33, 94, 33));
            humidityAxis.setLowerBound(-1);
            humidityAxis.setUpperBound(101);
            humidityAxis.setTickUnit(new NumberTickUnit(5.0D));

            XYPlot plot = new XYPlot(createTempDataset(), dateaxis, tempAxis, renderer);

            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxis(1, humidityAxis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            tempAxis.setLowerBound(minY - 1);
            tempAxis.setUpperBound(maxY + 1);

            XYDataset humDataset = createHumDataset();

            plot.setDataset(1, humDataset);
            plot.mapDatasetToRangeAxis(1, 1);

            StandardXYItemRenderer renderer2 =
                    new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);

            renderer2.setShapesFilled(true);
            renderer2.setBaseShapesVisible(true);
            renderer2.setBaseShapesFilled(true);
            renderer2.setSeriesPaint(0, new Color(33, 94, 33));
            plot.setRenderer(1, renderer2);
            plot.setBackgroundPaint(Color.WHITE);

            // set tooltip
            chart = new JFreeChart(dictionary.get("graph.ioh.title"), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
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
            chart = new JFreeChart(dictionary.get("graph.ioh.title"), JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        }

        // hideSeries(1, 0);
        return chart;
    }

    /**
     * Set color of series.
     *
     * @param chart       JFreeChart.
     * @param seriesIndex Index of series to set color of (0 = first series)
     * @param color       New color to set.
     */
    public void setSeriesColor(JFreeChart chart, int seriesIndex, Color color) {
        if (chart != null) {
            Plot plot = chart.getPlot();

            try {
                if (plot instanceof CategoryPlot) {
                    CategoryPlot categoryPlot = chart.getCategoryPlot();
                    CategoryItemRenderer cir = categoryPlot.getRenderer();
                    cir.setSeriesPaint(seriesIndex, color);
                } else if (plot instanceof PiePlot) {
                    PiePlot piePlot = (PiePlot) chart.getPlot();
                    piePlot.setSectionPaint(seriesIndex, color);
                } else if (plot instanceof XYPlot) {
                    XYPlot xyPlot = chart.getXYPlot();
                    XYItemRenderer xyir = xyPlot.getRenderer();
                    xyir.setSeriesPaint(seriesIndex, color);
                } else {
                    System.out.println("setSeriesColor() unsupported plot: " + plot);
                }
            } catch (Exception e) {    // e.g. invalid seriesIndex
                System.err.println("Error setting color '" + color + "' for series '" + seriesIndex + "' of chart '"
                        + chart + "': " + e);
            }
        }
    }

    /**
     * Creates a sample dataset.
     *
     * @return the dataset.
     */
    private XYDataset createTempDataset() {
        resetMinMaxY();

        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

        try {
            if (isEmpty()) {
                return timeSeriesCollection;
            } else {
                DateLocal now = DateLocal.now();
                DateLocal yday = now.addDays(-1);
                Day today = new Day(SerialDate.createInstance(now.getDate(), now.getMonth(), now.getYear()));
                Day yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));
                int hour = getHour();
                final TimeSeries insideseries = new TimeSeries(dictionary.get("graph.ioh.series.inside"));

                for (int i = IN_TEMP_INDEX + DAY_HOURS - 1; i >= IN_TEMP_INDEX; i--, hour--) {
                    String value = DataFormat.formatToStringValue(DataFormat.DEC_1, Long.valueOf(datasetString[i]));
                    float floatValue = Float.valueOf(value);

                    insideseries.add(new Hour( //hour
                            hour, today), floatValue);

                    if (hour == 0) {
                        today = yesterday;
                        hour = DAY_HOURS;        // testing
                    }

                    if (maxY < floatValue) {
                        maxY = (int) floatValue;
                    }

                    if (minY > floatValue) {
                        minY = (int) floatValue;
                    }
                }

                today = new Day(SerialDate.createInstance(now.getDate(), now.getMonth(), now.getYear()));

                // testing
                hour = getHour();

                final TimeSeries outsideseries = new TimeSeries(dictionary.get("graph.ioh.series.outside"));
                for (int i = OUT_TEMP_INDEX + DAY_HOURS - 1; i >= OUT_TEMP_INDEX; i--, hour--) {
                    String value = DataFormat.formatToStringValue(DataFormat.DEC_1, Long.valueOf(datasetString[i]));
                    float floatValue = Float.valueOf(value);

                    outsideseries.add(new Hour( //hour
                            hour, today), floatValue);
                    if (hour == 0) {
                        today = yesterday;
                        hour = DAY_HOURS;         // testing
                    }

                    if (maxY < floatValue) {
                        maxY = (int) floatValue;
                    }

                    if (minY > floatValue) {
                        minY = (int) floatValue;
                    }
                }

                timeSeriesCollection.addSeries(insideseries);
                timeSeriesCollection.addSeries(outsideseries);
                return timeSeriesCollection;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private XYDataset createHumDataset() {
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
            int hr = (int) (currentTime / 100) - 1;
            final TimeSeries humidityseries = new TimeSeries(dictionary.get("graph.ioh.series.humidity"));

            for (int i = HUMIDITY_INDEX + DAY_HOURS - 1; i >= HUMIDITY_INDEX; i--, hr--) {
                String value = DataFormat.formatToStringValue(DataFormat.HUMIDITY, Long.valueOf(datasetString[i]));
                int intValue = Integer.valueOf(value);

                humidityseries.add(new Hour( /*
                         * hour
                         */
                        hr, today), intValue);

                if (hr == 0) {
                    today = yesterday;
                    hr = DAY_HOURS;
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

    private int getHour() {
        return (int) (currentTime / 100) - 1;
    }

    public void hideSeries(int rendIndex, int serIndex) {
//      XYItemRenderer rend = chart.getXYPlot().getRenderer(rendIndex);
//      boolean bool = rend.getItemVisible(serIndex, 0);
//      rend.setSeriesVisible(serIndex, false);
    }
}



