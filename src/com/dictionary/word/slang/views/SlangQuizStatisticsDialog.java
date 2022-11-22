package com.dictionary.word.slang.views;

import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SlangQuizStatisticsDialog extends JDialog {
    public SlangQuizStatisticsDialog(Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        List<Integer> scores = FileIO.readScores(Constant.Path.SCORES);

        JFreeChart lineChart = ChartFactory.createXYLineChart(
                "",
                "Attempts",
                "Scores",
                createDataset(scores)
        );

        lineChart.removeLegend();

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 500));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setMouseWheelEnabled(true);

        setContentPane(chartPanel);
    }

    private XYDataset createDataset(List<Integer> scores) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries(10);
        dataset.addSeries(series);

        for (int i = 0; i < scores.size(); i++) {
            series.add(i + 1, scores.get(i));
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
