package com.agrologic.app.graph;


import com.agrologic.app.graph.history.Coordinate;
import com.agrologic.app.model.Data;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;

import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class CombinedXYGraph {
    /**  */
    public final static int HUM_GRAPH = 1;
    /**  */
    public final static int TEMP_GRAPH = 0;
    private final static int FIRST = 0;
    private final static int FITH = 4;
    private final static int FOURTH = 3;
    private final static int SECOND = 1;
    private final static int SIXTH = 5;
    private final static int THIRD = 2;
    private final static Map<Integer, Color> COLOR_MAP = new HashMap<Integer, Color>();
    private final static Map<Integer, Color> ANOTBGCOLOR = new HashMap<Integer, Color>();

    static {
        COLOR_MAP.put(FIRST, Color.red);
        COLOR_MAP.put(SECOND, Color.blue);
        COLOR_MAP.put(THIRD, Color.green);
        COLOR_MAP.put(FOURTH, Color.darkGray);
        COLOR_MAP.put(FITH, Color.orange);
        COLOR_MAP.put(SIXTH, Color.magenta);
        ANOTBGCOLOR.put(FIRST, Color.yellow);
        ANOTBGCOLOR.put(SECOND, new Color(0, 255, 255, 255));
        ANOTBGCOLOR.put(THIRD, new Color(0, 0, 0));
        ANOTBGCOLOR.put(FOURTH, new Color(255, 255, 255));
        ANOTBGCOLOR.put(FITH, new Color(0, 0, 0));
        ANOTBGCOLOR.put(SIXTH, new Color(255, 255, 255));
    }

    public int INDEX = 0;

    /**  */
    private Coordinate<Long> bottomCoord;
    private JFreeChart chart;

    /**  */
    private int plotCounter;

    /**  */
    private XYPlot[] plots;

    /**  */
    private Coordinate<Long> topCoord;

    public CombinedXYGraph() {
        super();
        plots = new XYPlot[5];
        plotCounter = 0;
        initTopAndBottomCoords();
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void createFirstNextPlot(final String chartTitle, final String xAxisTitle, final String yAxisTitle,
                                    final Data data, final int graphType,
                                    final Map<Integer, Data>... coordinates) {

        StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                NumberFormat.getInstance(), NumberFormat.getInstance());
        TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(new SimpleDateFormat("DD"), "", "series",
                "values");
        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES,
                ttg,
                urlg);

        XYSeriesCollection dataset = new XYSeriesCollection();

        initTopAndBottomCoords();

        for (Map<Integer, Data> coordinate : coordinates) {
            String datalabel = getDataLabel(coordinate);
            XYSeries xyseries = createSeries(coordinate, datalabel, graphType);

            dataset.addSeries(xyseries);
            INDEX++;
        }

        NumberAxis growDayAxis = new NumberAxis(yAxisTitle);
        growDayAxis.setAutoRangeIncludesZero(false);
        growDayAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plots[plotCounter] = new XYPlot(dataset, null, growDayAxis, renderer);

        NumberAxis numberAxis = new NumberAxis(yAxisTitle);
        // numberAxis.setUpperBound(topCoord.getX() + (topCoord.getY()*0.1));
        numberAxis.setAutoRangeIncludesZero(true);
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberAxis.setLabelFont(new Font("Dialog", Font.BOLD, 12));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
        plots[plotCounter].setRangeAxis(numberAxis);

        ValueAxis va = plots[plotCounter].getRangeAxis();
        double upper = va.getUpperBound();

        upper = upper + upper * 0.2;
        va.setUpperBound(upper);
        plots[plotCounter].setRangeAxis(va);
        plotCounter++;


    }

    public void createNextPlot(final String chartTitle, final String xAxisTitle, final String yAxisTitle,
                               final Data data, final int graphType, final Map<Integer, Data>... coordinates) {
//        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                NumberFormat.getInstance(), NumberFormat.getInstance());
        TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(new SimpleDateFormat("DD"), "", "series",
                "values");
        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES,
                ttg,
                urlg);

        XYSeriesCollection dataset = new XYSeriesCollection();

        initTopAndBottomCoords();

        for (Map<Integer, Data> coordinate : coordinates) {
            String datalabel = getDataLabel(coordinate);
            XYSeries xyseries = createSeries(coordinate, datalabel, graphType);

            dataset.addSeries(xyseries);
            INDEX++;
        }

        NumberAxis growDayAxis = new NumberAxis(yAxisTitle);

        growDayAxis.setAutoRangeIncludesZero(false);
        growDayAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plots[plotCounter] = new XYPlot(dataset, null, growDayAxis, renderer);

        NumberAxis numberAxis = new NumberAxis(yAxisTitle);

//      numberAxis.setUpperBound(100);
//      numberAxis.setLowerBound(0);
        numberAxis.setAutoRangeIncludesZero(false);
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberAxis.setLabelFont(new Font("Dialog", Font.BOLD, 12));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
        plots[plotCounter].setRangeAxis(numberAxis);
        plotCounter++;
    }

    public void createChart(String title, String subtitle, String xAxis) {
        NumberAxis growDayAxis = new NumberAxis(xAxis);
        growDayAxis.setAutoRangeIncludesZero(false);
        growDayAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        growDayAxis.setLabelFont(new Font("Dialog", Font.BOLD, 12));
        growDayAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));

        CombinedDomainXYPlot localCombinedDomainXYPlot = new CombinedDomainXYPlot(growDayAxis);
        localCombinedDomainXYPlot.setGap(10.0D);

        for (XYPlot p : plots) {
            if (p != null) {
                localCombinedDomainXYPlot.add(p, 1);
            }
        }

        localCombinedDomainXYPlot.setOrientation(PlotOrientation.VERTICAL);
        chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, localCombinedDomainXYPlot, true);

        changeLegendFont();
//        setSubtitle(subtitle);
    }

    private void setSubtitle(String subtitle) {
        TextTitle localTextTitle = new TextTitle(subtitle);
        localTextTitle.setFont(new Font("Dialog", Font.BOLD, 14));
        localTextTitle.setPosition(RectangleEdge.TOP);
        localTextTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        chart.addSubtitle(localTextTitle);
    }

    private XYSeries createSeries(final Map<Integer, Data> coordinates, String seriesLabel, int graphType) {

        // String seriesLabel = getDataLabel(coordinates);
        XYSeries series = new XYSeries(seriesLabel);
        Iterator<Entry<Integer, Data>> iter = coordinates.entrySet().iterator();

        while (iter.hasNext()) {
            Entry<Integer, Data> entry = (Entry<Integer, Data>) iter.next();
            Number x = entry.getKey();
            Number y = Double.valueOf(entry.getValue().getFormattedValue());
            Coordinate coord = new Coordinate(x.longValue(), y.longValue());

            if ((graphType == TEMP_GRAPH) && ((y.doubleValue() >= -50.0) && (y.doubleValue() <= 50.0))) {
                series.add(x, y);
                setTopCoords(coord);
                setBottomCoords(coord);
            } else if ((graphType == HUM_GRAPH) && ((y.doubleValue() >= 0.0) && (y.doubleValue() <= 100.0))) {
                series.add(x, y);
                setTopCoords(coord);
                setBottomCoords(coord);
            }
        }

        return series;
    }

    private void changeLegendFont() {
        LegendTitle legendTitle = (LegendTitle) getChart().getSubtitle(0);
        Font itemFont = new Font("Dialog", Font.PLAIN, 15);
        legendTitle.setItemFont(itemFont);
        legendTitle.setPosition(RectangleEdge.RIGHT);
        legendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
    }

    private String getDataLabel(final Map<Integer, Data> coordinates) {
        String label = "";
        Iterator<Data> dataIter = coordinates.values().iterator();

        if (dataIter.hasNext()) {
            label = dataIter.next().getUnicodeLabel();
        }

        return label;
    }

    /**
     * Initialize top coordinate and bottom
     * cordinate of each graph.
     */
    private void initTopAndBottomCoords() {
        topCoord = new Coordinate<Long>(Long.MIN_VALUE, Long.MIN_VALUE);
        bottomCoord = new Coordinate<Long>(Long.MAX_VALUE, Long.MAX_VALUE);
    }

    /**
     * Sets top coordinates .
     *
     * @param coord the coord to set topCoord.
     */
    private void setTopCoords(Coordinate coord) {
        if (coord.compareTo(topCoord) == 1) {
            topCoord = coord;
        } else {
            topCoord = coord;
        }
    }

    /**
     * Sets bottom coordinates .
     *
     * @param coord the coord to set bottomCoord.
     */
    private void setBottomCoords(Coordinate coord) {
        if (coord.compareTo(bottomCoord) == -1) {
            bottomCoord = coord;
        } else {
            bottomCoord = coord;
        }
    }
}



