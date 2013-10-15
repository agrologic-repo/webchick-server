
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.graph.history;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;

import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HistoryGraph {

    /**
     * Line style: dashed
     */
    public static final String STYLE_DASH = "dash";
    /**
     * Line style: dotted
     */
    public static final String STYLE_DOT = "dot";
    /**
     * Line style: line
     */
    public static final String STYLE_LINE = "line";
    public Number max = Double.MIN_VALUE;
    public Number min = Double.MAX_VALUE;
    /**
     * The bottom coordinate of series
     */
    private Coordinate<Long> bottomCoord;
    /**
     *
     */
    protected JFreeChart chart;
    /**
     *
     */
    private List<Map<Integer, Data>> dataHistoryList;
    /**
     * The top coordinate of series
     */
    private Coordinate<Long> topCoord;
    /**
     *
     */
    private boolean unitTickX;
    /**
     *
     */
    private boolean unitTickY;

    public void createChart(final String title, final String xAxisTitle, final String yAxisTitle) {
        // create series collection
        XYSeriesCollection seriesCollection = createSeriesCollection(dataHistoryList);
        //      create the chart ...
        chart = ChartFactory.createXYLineChart(title, // chart title
                xAxisTitle, // x axis label
                yAxisTitle, // y axis label
                seriesCollection, // data
                PlotOrientation.VERTICAL,//
                true, // include legend
                true, // tooltips
                false);
        // now do some optional customisation of the chart...
        // set chart background color
        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        LegendTitle legendTitle = (LegendTitle) chart.getSubtitle(0);
        legendTitle.setItemFont(new Font("Dialog", Font.PLAIN, 16));
        legendTitle.setPosition(RectangleEdge.TOP);
        legendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        setPlotParameters(plot);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        setAxisParameters(xAxis, xAxisTitle, Color.black);
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerBound(getMinValue(plot).doubleValue());
        xAxis.setUpperBound((getMaxValue(plot).doubleValue() + 1));

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        setAxisParameters(yAxis, yAxisTitle, Color.red);
    }

    /**
     * Creates series collection of data management by grow day and add to plot.
     *
     * @param dataHsitoryList the List of data management by grow day map.
     * @param axisLabel       the label of series collection.
     */
    public void createAndAddSeriesCollection(List<Map<Integer, Data>> dhl, String axisLabel) {
        initTopAndBottomCoords();
        XYSeriesCollection seriesCollect = createSeriesCollection(dhl);
        final XYPlot plot = chart.getXYPlot();
        int count = plot.getRangeAxisCount();
        plot.setDataset(count, seriesCollect);
        plot.mapDatasetToRangeAxis(count, count);
        plot.setRangeAxis(count, createNumberAxis(axisLabel, Color.BLUE));
        StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("DD"), NumberFormat.getInstance());

        TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(
                new SimpleDateFormat("DD"),
                "",
                "series",
                "hitDate");
        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES,
                ttg,
                urlg);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setSeriesPaint(count - 1, Color.BLUE);
        plot.setRangeAxisLocation(count, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setRenderer(count, renderer);
    }

    /**
     * Creates series collection of data management by grow day.
     *
     * @param dataHsitoryList the List of data management by grow day map.
     * @return seriesCollect the series collection object.
     */
    protected XYSeriesCollection createSeriesCollection(List<Map<Integer, Data>> dhl) {
        initTopAndBottomCoords();
        XYSeriesCollection seriesCollect = new XYSeriesCollection();
        for (Map<Integer, Data> coordinate : dhl) {
            XYSeries xyseries = createSeries(coordinate);
            seriesCollect.addSeries(xyseries);
        }
        return seriesCollect;
    }

    /**
     * Return created series and set minimum maximum coordinates.
     *
     * @param coordinates the map with values by grow day.
     * @param seriesLabel the label of series.
     * @return series the series
     */
    protected XYSeries createSeries(final Map<Integer, Data> coordinates) {
        XYSeries series = new XYSeries(getSeriesLabel(coordinates));
        Set<Entry<Integer, Data>> entries = coordinates.entrySet();
        for (Entry<Integer, Data> entry : entries) {
            Number x = entry.getKey();
            Number y = valueByType(entry.getValue());
            series.add(x, y);
            setTopAndBottomCoords(x, y);
        }
        return series;
    }

    private Number valueByType(Data data) {
        if (DataFormat.TIME == data.getFormat()) {
            Long value = data.getValue();
            long h = value / 100;
            long m = value % 100;
            long t = h * 60 + m;
            return (double) t;
        }
        return Double.valueOf(data.getFormattedValue());
    }

    /**
     * Return label of series.
     *
     * @param coordinates
     * @return data label
     */
    protected String getSeriesLabel(final Map<Integer, Data> coordinates) {
        Iterator<Data> dataIter = coordinates.values().iterator();
        if (dataIter.hasNext()) {
            return dataIter.next().getUnicodeLabel();
        }
        return "";
    }

    /**
     * Set background color, domain grid line , range grind line and also set
     * renderer of plot to XYLineAndShapeRenderer.
     *
     * @param plot the given plot
     */
    protected void setPlotParameters(XYPlot plot) {
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        StandardXYToolTipGenerator ttg =
                new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                        new SimpleDateFormat("DD"), NumberFormat.getInstance());

        TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(new SimpleDateFormat("DD"), "", "series", "hitDate");
        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES, ttg, urlg);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        plot.setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);
        plot.setRenderer(renderer);
    }

    /**
     * Creates axis .
     *
     * @param axisLabel the label of axis.
     * @param color     the color of axis.
     * @return numberAxis the created axis object.
     */
    protected NumberAxis createNumberAxis(final String axisLabel, Color color) {
        NumberAxis numberAxis = new NumberAxis(axisLabel);
        setAxisParameters(numberAxis, axisLabel, color);
        return numberAxis;
    }

    /**
     * Set parameters of axis.
     *
     * @param numberAxis the axis to set.
     * @param axisLabel  the label of axis to set.
     * @param color      the color of axis to set.
     * @return numberAxis the seted axis object.
     */
    protected NumberAxis setAxisParameters(NumberAxis numberAxis, final String axisLabel, Color color) {
        numberAxis.setLabel(axisLabel);
        numberAxis.setAutoRangeIncludesZero(false);
        numberAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        numberAxis.setLabelPaint(color);
        numberAxis.setTickLabelPaint(color);
        numberAxis.setLabelFont(new Font("Dialog", Font.BOLD, 16));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
        return numberAxis;
    }

    /**
     * Return chart.
     *
     * @return chart
     */
    public JFreeChart getChart() {
        return chart;
    }

    /**
     * Initialize top coordinate and bottom coordinate of each graph.
     */
    protected void initTopAndBottomCoords() {
        topCoord = new Coordinate<Long>(Long.MIN_VALUE, Long.MIN_VALUE);
        bottomCoord = new Coordinate<Long>(Long.MAX_VALUE, Long.MAX_VALUE);
    }

    /**
     * Initialize top coordinate and bottom coordinate of each graph.
     */
    protected void setTopAndBottomCoords(Number x, Number y) {
        Coordinate coord = new Coordinate(x.longValue(), y.longValue());
        setTopCoords(coord);
        setBottomCoords(coord);
    }

    /**
     * Sets top coordinates .
     *
     * @param coord the coord to set topCoord.
     */
    private void setTopCoords(Coordinate coord) {
        if (topCoord == null) {
            topCoord = new Coordinate<Long>(Long.MIN_VALUE, Long.MIN_VALUE);
        }

        if (coord.compareTo(topCoord) == 1) {
            topCoord = coord;
        }
    }

    /**
     * Sets bottom coordinates .
     *
     * @param coord the coord to set bottomCoord.
     */
    private void setBottomCoords(Coordinate coord) {
        if (bottomCoord == null) {
            bottomCoord = new Coordinate<Long>(Long.MAX_VALUE, Long.MAX_VALUE);
        }

        if (coord.compareTo(bottomCoord) == -1) {
            bottomCoord = coord;
        }
    }

    /**
     * Convert style string to stroke object.
     *
     * @param style One of STYLE_xxx.
     * @return Stroke for <i>style</i> or null if style not supported.
     */
    private BasicStroke toStroke(String style) {    //
        BasicStroke result = null;

        if (style != null) {
            float lineWidth = 0.2f;
            float dash[] = {5.0f};
            float dot[] = {lineWidth};

            if (style.equalsIgnoreCase(STYLE_LINE)) {
                result = new BasicStroke(lineWidth);
            } else if (style.equalsIgnoreCase(STYLE_DASH)) {
                result = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
            } else if (style.equalsIgnoreCase(STYLE_DOT)) {
                result = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
            }
        }    // else: input unavailable

        return result;
    }    // toStroke()

    /**
     * Set style of series.
     *
     * @param chart       JFreeChart.
     * @param seriesIndex Index of series to set color of (0 = first series)
     * @param style       One of STYLE_xxx.
     */
    public void setSeriesStyle(JFreeChart chart, int seriesIndex, String style, int plotIndex) {
        if ((chart != null) && (style != null)) {
            BasicStroke stroke = toStroke(style);
            CombinedDomainXYPlot plots = (CombinedDomainXYPlot) chart.getPlot();
            List subplots = plots.getSubplots();
            Plot plot = (Plot) subplots.get(plotIndex);

            if (plot instanceof CategoryPlot) {

                // CategoryPlot categoryPlot = chart.getCategoryPlot();
                CategoryItemRenderer cir = ((CategoryPlot) plot).getRenderer();

                try {
                    cir.setSeriesStroke(seriesIndex, stroke);    // series line style
                } catch (Exception e) {
                    System.err.println("Error setting style '" + style + "' for series '" + seriesIndex
                            + "' of chart '" + chart + "': " + e);
                }
            } else if (plot instanceof XYPlot) {

                // XYPlot xyPlot = chart.getXYPlot();
                XYItemRenderer xyir = ((XYPlot) plot).getRenderer();

                try {
                    xyir.setSeriesStroke(seriesIndex, stroke);    // series line style
                } catch (Exception e) {
                    System.err.println("Error setting style '" + style + "' for series '" + seriesIndex
                            + "' of chart '" + chart + "': " + e);
                }
            } else {
                System.out.println("setSeriesColor() unsupported plot: " + plot);
            }
        }
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
     * Set data management list.
     *
     * @param dataHistoryList the data management list
     */
    public void setDataHistoryList(List<Map<Integer, Data>> dataHistoryList) {
        this.dataHistoryList = dataHistoryList;
    }

    /**
     * Return min value
     *
     * @return
     */
    public Number getMinValue(XYPlot plot) {
        XYSeriesCollection seriesCollection = (XYSeriesCollection) plot.getDataset();
        List<XYSeries> xyseriesList = seriesCollection.getSeries();
        for (XYSeries xyseries : xyseriesList) {
            Number minNumberX = xyseries.getMinX();
            if (min.doubleValue() > minNumberX.doubleValue()) {
                min = xyseries.getMinX();
            }
        }
        return min;
    }

    /**
     * Return max value
     *
     * @return
     */
    public Number getMaxValue(XYPlot plot) {
        XYSeriesCollection seriesCollection = (XYSeriesCollection) plot.getDataset();
        List<XYSeries> xyseriesList = seriesCollection.getSeries();
        for (XYSeries xyseries : xyseriesList) {
            Number maxNumberX = xyseries.getMaxX();
            if (max.doubleValue() < maxNumberX.doubleValue()) {
                max = xyseries.getMaxX();
            }
        }
        return max;
    }
}
