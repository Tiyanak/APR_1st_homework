package hr.fer.apr.golden_lab;

import hr.fer.apr.linear_algebra.IMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.imageio.metadata.IIOMetadata;
import java.awt.*;
import java.util.List;

/**
 * Created by Igor on 26.12.2016..
 */
public class Chart extends ApplicationFrame{


    public Chart(String applicationTitle, String title, List<Integer> x, List<IMatrix> y) {
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
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );

    }

    private XYDataset createDataset(List<Integer> x, List<IMatrix> y){

        final XYSeries firstdim = new XYSeries("1. dimension");
        final XYSeries seconddim = new XYSeries("2. dimension");

        for(int i=0; i<x.size(); i++){
            firstdim.add((double)x.get(i), y.get(i).getElement(0, 0));
            seconddim.add((double)x.get(i), y.get(i).getElement(1, 0));
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(firstdim);
        dataset.addSeries(seconddim);

        return dataset;

    }

}
