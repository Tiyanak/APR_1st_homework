package hr.fer.apr.golden_lab;

import hr.fer.apr.linear_algebra.IMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.util.List;

/**
 * Created by Igor on 6.1.2017..
 */
public class SingleChart extends ApplicationFrame{

    public SingleChart(String applicationTitle, String title, List<Integer> x, List<IMatrix> y) {
        super(applicationTitle);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "t",
                "x",
                createDataset(x, y),
                PlotOrientation.VERTICAL,
                true, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1080, 640));
        final XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );

    }

    private XYDataset createDataset(List<Integer> x, List<IMatrix> y){

        final XYSeries firstdim = new XYSeries("1. dimension");

        for(int i=0; i<x.size(); i++){
            firstdim.add((double)x.get(i), y.get(i).getElement(0, 0));
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(firstdim);

        return dataset;

    }

}
