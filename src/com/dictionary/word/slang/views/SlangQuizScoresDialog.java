package com.dictionary.word.slang.views;

import com.dictionary.word.slang.utils.Triplet;
import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SlangQuizScoresDialog extends JDialog {
    SlangQuizScoresDialog() {
        setModal(true);
        initComponents();
    }

    private void initComponents() {
        List<Triplet<String, Integer, Date>> scores = FileIO.readScores(Constant.Path.SCORES);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",
                "Times",
                "Scores",
                createDataset(scores),
                true,
                true,
                true
        );

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setDefaultShapesVisible(true);
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setDateFormatOverride(formatter);
        domain.setVerticalTickLabels(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 500));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setMouseWheelEnabled(true);

        setContentPane(chartPanel);
    }

    private TimeSeriesCollection createDataset(List<Triplet<String, Integer, Date>> scores) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        for (Triplet<String, Integer, Date> score : scores) {
            String seriesName = score.getFirst();

            if (dataset.indexOf(seriesName) == -1) {
                dataset.addSeries(new TimeSeries(seriesName));
            }

            TimeSeries series = dataset.getSeries(seriesName);
            series.add(new Second(score.getThird()), score.getSecond());
        }

        return dataset;
    }

    public void showDialog() {
        setTitle("Quiz scores statistics");
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}
