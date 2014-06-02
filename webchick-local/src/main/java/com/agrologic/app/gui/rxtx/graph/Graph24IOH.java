package com.agrologic.app.gui.rxtx.graph;

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
    private XYDataset inputOutputDataSet;
    private XYDataset humidityDataset;

    public Graph24IOH(GraphType type, String values, Long currentTime, Locale locale) {
        super(type, values);
        this.currentTime = currentTime;
        this.locale = locale;
        initLanguage();
        inputOutputDataSet = createTemperatureDataset();
        humidityDataset = createHumidityDataset();
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

            final XYPlot plot = new XYPlot(inputOutputDataSet, dateaxis, tempAxis, renderer);
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxis(1, humidityAxis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            tempAxis.setLowerBound(minY - 1);
            tempAxis.setUpperBound(maxY + 1);

            plot.setDataset(1, humidityDataset);
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
        return chart;
    }

    /**
     * Creates a sample dataset.
     *
     * @return the dataset.
     */
    private XYDataset createTemperatureDataset() {
        resetMinMaxY();

        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

        try {
            if (isEmpty()) {
                return timeSeriesCollection;
            } else {
                final TimeSeries insideTempSeries = new TimeSeriesCreator(dictionary.get("graph.ioh.series.inside"),
                        IN_TEMP_INDEX, -1, -1).invoke();
                timeSeriesCollection.addSeries(insideTempSeries);

                final TimeSeries outsideTempSeries = new TimeSeriesCreator(dictionary.get("graph.ioh.series.outside"),
                        OUT_TEMP_INDEX, -1, -1).invoke();

                timeSeriesCollection.addSeries(outsideTempSeries);
                return timeSeriesCollection;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return timeSeriesCollection;
        }
    }

    /**
     * Create #XYDataset with humidity per hour data series
     *
     * @return TimeSeriesCollection with humidity per hour history data series
     */
    private XYDataset createHumidityDataset() {
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        try {
            if (isEmpty()) {
                return timeSeriesCollection;
            } else {
                final TimeSeries humiditySeries = new TimeSeriesCreator(dictionary.get("graph.ioh.series.humidity"),
                        HUMIDITY_INDEX, 100, 0).invoke();

                timeSeriesCollection.addSeries(humiditySeries);

                return timeSeriesCollection;
            }
        } catch (Exception e) {
            return timeSeriesCollection;
        }
    }

    /**
     * Inner class that help to create TimeSeries object for chart .
     */
    private class TimeSeriesCreator {
        private String name;
        private int startIndex;
        private int maxYValue;
        private int minYValue;

        public TimeSeriesCreator(String name, int startIndex, int maxYValue, int minYValue) {
            this.name = name;
            this.startIndex = startIndex;
            this.maxYValue = maxYValue;
            this.minYValue = minYValue;
        }

        public TimeSeries invoke() {
            DateLocal now = DateLocal.now();
            DateLocal yday = now.addDays(-1);
            int day = now.getDate();
            int month = now.getMonth();
            int year = now.getYear();
            Day today = new Day(SerialDate.createInstance(day, month, year));
            Day yesterday = new Day(SerialDate.createInstance(yday.getDate(), yday.getMonth(), yday.getYear()));

            // testing
            int hour = getHour();

            final TimeSeries series = new TimeSeries(name);


            for (int i = startIndex + DAY_HOURS - 1; i >= startIndex; i--, hour--) {
                String value = DataFormat.formatToStringValue(DataFormat.DEC_1, Long.valueOf(datasetString[i]));
                float floatValue = Float.valueOf(value);
                series.add(new Hour(hour, today), floatValue);

                if (hour == 0) {
                    today = yesterday;
                    hour = DAY_HOURS;        // testing
                }

                if (maxY < floatValue) {
                    if (maxYValue == -1) {
                        maxY = (int) floatValue;
                    } else {
                        maxY = maxYValue;
                    }
                }

                if (minY > floatValue) {
                    if (minYValue == -1) {
                        minY = (int) floatValue;
                    } else {
                        minY = minYValue;
                    }
                }
            }
            return series;
        }

        private int getHour() {
            return (int) (currentTime / 100) - 1;
        }
    }

    /**
     * Hide series of given rend index and series index .
     *
     * @param rendIndex renderer index
     * @param serIndex  series index
     */
    public void hideSeries(int rendIndex, int serIndex) {
//      XYItemRenderer rend = chart.getXYPlot().getRenderer(rendIndex);
//      boolean bool = rend.getItemVisible(serIndex, 0);
//      rend.setSeriesVisible(serIndex, false);
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
                }
            } catch (Exception e) {    // e.g. invalid seriesIndex
                System.err.println("Error setting color '" + color + "' for series '" + seriesIndex + "' of chart '"
                        + chart + "': " + e);
            }
        }
    }
}



