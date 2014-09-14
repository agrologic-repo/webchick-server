package com.agrologic.app.graph.daily;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;

/**
 * Empty Graph used  in case of no data to create graph. Also when exception appeared during creation graph .
 */
public final class EmptyGraph {
    private JFreeChart chart;

    public EmptyGraph() {
        createChart();
    }

    /**
     * Create empty graph
     */
    protected void createChart() {
        final XYPlot xyplot = new XYPlot();
        xyplot.setNoDataMessage("No data available!");
        xyplot.setNoDataMessageFont(new Font("Serif", 2, 16));
        xyplot.setNoDataMessagePaint(Color.red);
        xyplot.setDomainCrosshairVisible(false);
        xyplot.setRangeCrosshairVisible(false);
        xyplot.setDomainPannable(false);
        xyplot.setRangePannable(false);
        chart = new JFreeChart("Empty Graph", xyplot);
    }

    /**
     * @return chart
     */
    public JFreeChart getChart() {
        return chart;
    }
}



