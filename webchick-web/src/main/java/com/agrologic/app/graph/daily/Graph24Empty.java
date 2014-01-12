package com.agrologic.app.graph.daily;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;

public final class Graph24Empty {
    JFreeChart chart;

    public Graph24Empty() {
        createChart();
    }

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

    public JFreeChart getChart() {
        return chart;
    }
}



