package com.dictionary.word.slang.views;

import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class SlangQuizStatisticsDialog extends JDialog {
    public SlangQuizStatisticsDialog(Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        DefaultCategoryDataset dataset = FileIO.readQuizStatistics(Constant.Path.QUIZ_STATISTICS);

        JFreeChart lineChart = ChartFactory.createLineChart(
                "",
                "Attempts",
                "Scores",
                dataset
        );

        lineChart.removeLegend();

        ChartPanel panel = new ChartPanel(lineChart);
        panel.setPreferredSize(new Dimension(800, 500));
        setContentPane(panel);
    }

    public void showDialog() {
        setTitle("Quiz scores statistics");
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}
