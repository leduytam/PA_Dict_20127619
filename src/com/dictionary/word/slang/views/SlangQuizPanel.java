package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangDictionary;
import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SlangQuizPanel extends JPanel implements ActionListener {
    private JButton btnQuiz;
    private JComboBox<String> cbxQuiz;
    private JButton btnQuizStatistics;

    SlangQuizPanel() {
       super();
       initComponents();
    }

    private void initComponents() {
        btnQuiz = new JButton("Let's play");
        btnQuiz.setFocusPainted(false);
        btnQuiz.addActionListener(this);
        btnQuiz.setPreferredSize(new Dimension(110, 30));

        cbxQuiz = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxQuiz.setPreferredSize(new Dimension(100, 30));

        btnQuizStatistics = new JButton("View score statistics");
        btnQuizStatistics.setFocusPainted(false);
        btnQuizStatistics.addActionListener(this);
        btnQuizStatistics.setPreferredSize(new Dimension(200, 30));

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        setBorder(BorderFactory.createTitledBorder("Quizzes"));

        add(btnQuiz);
        add(cbxQuiz);
        add(btnQuizStatistics);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source.equals(btnQuiz)) {
            if (SlangDictionary.getInstance().getUniqueSize() < 10) {
                String message = """
                            Current slang word number is not enough to create a quiz.
                            A quiz needs at least 40 unique slang words to create.
                            Please add more slang words and try again!
                        """;
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SlangQuizDialog dialog = new SlangQuizDialog(cbxQuiz.getSelectedIndex() == 1);
            int score = dialog.showDialog();
            List<Integer> scores = FileIO.readScores(Constant.Path.SCORES);
            scores.add(score);
            FileIO.writeScores(scores, Constant.Path.SCORES);
        }

        if (source.equals(btnQuizStatistics)) {
            new SlangQuizScoresDialog().showDialog();
        }
    }
}
