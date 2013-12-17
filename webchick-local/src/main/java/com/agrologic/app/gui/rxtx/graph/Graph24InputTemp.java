package com.agrologic.app.gui.rxtx.graph;

import com.agrologic.app.model.DataFormat;
import com.agrologic.app.util.DateLocal;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
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


/**
 * Title: Graph24HumidTemp <br> Description: <br> Copyright: Copyright (c) 2009
 * <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class Graph24InputTemp extends AbstractGraph {

    public Graph24InputTemp(GraphType type, String values, Long currnetTime, Locale locale) {
        super(type, values);
        this.currentTime = currnetTime;
        this.locale = locale;
        initLanguage();
    }

    @Override
    public final JFreeChart createChart() {
        if (!isEmpty()) {
            DateAxis dateaxis = new DateAxis(dictionary.get("graph.in.temp.axis.time"));
            dateaxis.setDateFormatOverride(new SimpleDateFormat("HH"));
            dateaxis.setLabelPaint(Color.BLACK);
            dateaxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
            dateaxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
            dateaxis.setVerticalTickLabels(false);

            NumberAxis tempAxis = new NumberAxis(dictionary.get("graph.in.temp.axis.temperature"));
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

            // ////////////////////////////////////////////////////

            XYPlot plot = new XYPlot(createTempDataset(), dateaxis, tempAxis, renderer);
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            plot.setBackgroundPaint(Color.WHITE);

            // set tooltip
            chart = new JFreeChart(dictionary.get("graph.in.temp.title"), JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBorderPaint(Color.BLACK);
            chart.setBackgroundPaint(Color.LIGHT_GRAY);

            LegendTitle legendTitle = (LegendTitle) chart.getSubtitle(0);
            legendTitle.setItemFont(new Font("Dialog", Font.PLAIN, 16));
            legendTitle.setPosition(RectangleEdge.TOP);
            legendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
            tempAxis.setLowerBound(minY - 10);
            tempAxis.setUpperBound(maxY + 10);
        } else {
            final XYPlot xyplot = new XYPlot();
            xyplot.setNoDataMessage("No data available!");
            xyplot.setNoDataMessageFont(new Font("Serif", 2, 15));
            xyplot.setNoDataMessagePaint(Color.red);
            xyplot.setDomainCrosshairVisible(true);
            xyplot.setRangeCrosshairVisible(true);
            xyplot.setDomainPannable(true);
            xyplot.setRangePannable(true);
            chart = new JFreeChart(dictionary.get("graph.in.temp.title"), JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
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

                for (int i = AbstractGraph.IN_TEMP_INDEX + AbstractGraph.DAY_HOURS - 1; i >= AbstractGraph.IN_TEMP_INDEX; i--, hour--) {
                    String value = DataFormat.formatToStringValue(DataFormat.DEC_1, Long.valueOf(datasetString[i]));
                    float floatValue = Float.valueOf(value);

                    insideseries.add(new Hour( /*
                             * hour
                             */hour, today), floatValue);

                    if (hour == 0) {
                        today = yesterday;
                        hour = AbstractGraph.DAY_HOURS;    // testing
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
                timeSeriesCollection.addSeries(insideseries);
                return timeSeriesCollection;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private int getHour() {
        return (int) (currentTime / 100) - 1;
    }
}
