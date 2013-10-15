

package com.agrologic.app.graph;


import com.agrologic.app.graph.history.Coordinate;
import com.agrologic.app.model.Data;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CombinedXYGraph {
    private final static int FIRST = 0;
    private final static int FITH = 4;
    private final static int FOURTH = 3;

    /**  */
    public final static int HUM_GRAPH = 1;
    private final static int SECOND = 1;
    private final static int SIXTH = 5;

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

    /**  */
    public final static int TEMP_GRAPH = 0;
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
    public int SER_INDEX = 0;
    private int maxX = 0;
    private int maxY = Integer.MIN_VALUE;
    private int minX = 0;
    private int minY = Integer.MAX_VALUE;

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

    protected void resetMinMax() {
        maxX = 0;
        maxY = Integer.MIN_VALUE;
        minX = 0;
        minY = Integer.MAX_VALUE;
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void createFirstNextPlot(final String chartTitle, final String xAxisTitle, final String yAxisTitle,
                                    final Data data, final int graphType,
                                    final Map<Integer, Data>... coordinates) {
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
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
        numberAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
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
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
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
        numberAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
        plots[plotCounter].setRangeAxis(numberAxis);
        plotCounter++;
    }

    public void createChart(String title, String subtitle) {
        NumberAxis growDayAxis = new NumberAxis("Grow Day");
        growDayAxis.setAutoRangeIncludesZero(false);
        growDayAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        growDayAxis.setLabelFont(new Font("Dialog", Font.PLAIN, 16));
        growDayAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));

        CombinedDomainXYPlot localCombinedDomainXYPlot = new CombinedDomainXYPlot(growDayAxis);

        localCombinedDomainXYPlot.setGap(10.0D);

        for (XYPlot p : plots) {
            if (p != null) {
                localCombinedDomainXYPlot.add(p, 1);
            }
        }

        localCombinedDomainXYPlot.setOrientation(PlotOrientation.VERTICAL);
        chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, localCombinedDomainXYPlot, true);

        TextTitle localTextTitle = new TextTitle(subtitle);

        localTextTitle.setFont(new Font("SansSerif", 0, 14));
        localTextTitle.setPosition(RectangleEdge.TOP);
        localTextTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        chart.addSubtitle(localTextTitle);
    }

    /**
     * @param coordinates
     * @param title
     */
    public XYSeriesCollection createAndAddDataset(final Map<Integer, Data> coordinates, final String title,
                                                  Data data, int graphType) {
        XYPlot plot = chart.getXYPlot();
        XYSeriesCollection dataset = (XYSeriesCollection) plot.getDataset(SER_INDEX);

        return dataset;
    }

    public void setMinMax(String unit, Data data) {
        StringBuilder sb = new StringBuilder();

        sb.append(data.getLabel()).append(" : Maximum = ").append(topCoord.getY()).append(unit).append(
                ", Minimum = ").append(bottomCoord.getY()).append(unit);
        chart.addSubtitle(INDEX, new TextTitle(sb.toString()));
    }

    /**
     * @param coordinates
     * @param title
     */
    public void createNewDataset(final Map<Integer, Data> coordinates, final String title, final Data data,
                                 final int graphType) {
        XYSeriesCollection dataset = new XYSeriesCollection();
    }

    public void addAxis(final String yAxisTitle) {
        final XYPlot plot = chart.getXYPlot();
        NumberAxis numberAxis = new NumberAxis(yAxisTitle);

        numberAxis.setUpperBound(topCoord.getX() + (topCoord.getY() * 0.1));
        numberAxis.setAutoRangeIncludesZero(false);
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberAxis.setLabelFont(new Font("Dialog", Font.BOLD, 12));
        numberAxis.setTickLabelFont(new Font("Dialog", Font.BOLD, 12));
        plot.setRangeAxis(SER_INDEX, numberAxis);
    }

    public final void fillColor() {
    }

    /**
     * Convert style string to stroke object.
     *
     * @param style One of STYLE_xxx.
     * @return Stroke for <i>style</i> or null if style not supported.
     */
    private BasicStroke toStroke(String style) {
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
     * Set color of series.
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
        }    // else: input unavailable
    }    // setSeriesStyle()

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
        }    // else: input unavailable
    }    // setSeriesColor()

    public AbstractXYAnnotation createAnnotation(int x, int y, String label, int count) {
        XYPointerAnnotation annotation = new XYPointerAnnotation(label + " ( " + x + " , " + y + " ) ", x, y,
                -0.7853981633974483D);

        annotation.setTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
        annotation.setPaint(COLOR_MAP.get(count));
        annotation.setArrowPaint(COLOR_MAP.get(count));

        return annotation;
    }

    public AbstractXYAnnotation createAnnotation(long x, long y, String label, int count) {
        XYPointerAnnotation annotation = new XYPointerAnnotation(label + " ( " + x + " , " + y + " ) ", x, y,
                -0.7853981633974483D);

        annotation.setTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
        annotation.setPaint(COLOR_MAP.get(count));
        annotation.setArrowPaint(COLOR_MAP.get(count));

        return annotation;
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



