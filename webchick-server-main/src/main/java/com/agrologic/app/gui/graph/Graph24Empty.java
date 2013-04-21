
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui.graph;

//~--- non-JDK imports --------------------------------------------------------

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Font;

/**
 * Title: Graph24Blank <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public final class Graph24Empty {
    JFreeChart chart;

    public Graph24Empty(GraphType type, String values) {
        final XYDataset xydataset = new DefaultXYDataset();

        createChart(xydataset);
    }

    protected void createChart(XYDataset xydataset) {
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


//~ Formatted by Jindent --- http://www.jindent.com
