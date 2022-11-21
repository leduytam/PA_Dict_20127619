package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangDictionary;
import com.dictionary.word.slang.objects.SlangQuiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SlangQuizDialog extends JDialog implements ActionListener {
    private final SlangQuizDialog me;
    private int score;
    private final List<SlangQuiz> quizzes;
    private int currentQuiz;
    private JLabel lbQuestion;
    private JButton[] btnOptions;
    private JLabel lbScore;
    private JLabel lbCurrentQuiz;

    public SlangQuizDialog(Frame parent, boolean isReverse) {
        super(parent, true);

        me = this;
        quizzes = SlangDictionary.getInstance().generateRandomQuizzes(10, isReverse);
        score = 0;
        currentQuiz = 0;

        initComponents();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                int choice = JOptionPane.showConfirmDialog(me, "Are you sure you want to quit?", "Warning", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    me.dispose();
                }
            }
        });
    }

    private void initComponents() {
        JPanel panelQuestion = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));

        lbQuestion = new JLabel(quizzes.get(currentQuiz).getShortQuestion(60));
        lbQuestion.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lbQuestion.setForeground(Color.RED);

        JScrollPane spQuestion = new JScrollPane(lbQuestion);
        panelQuestion.add(lbQuestion);

        JPanel panelOptions = new JPanel(new GridLayout(2, 2, 20, 20));

        btnOptions = new JButton[4];
        for (int i = 0; i < btnOptions.length; i++) {
            btnOptions[i] = new JButton(String.format("<html><p>%s</p></html>", quizzes.get(currentQuiz).getOptions().get(i)));
            btnOptions[i].setPreferredSize(new Dimension(250, 100));
            btnOptions[i].setActionCommand("check#" + i);
            btnOptions[i].addActionListener(this);
            btnOptions[i].setFocusPainted(false);
            panelOptions.add(btnOptions[i]);
        }

        JPanel panelScore = new JPanel();

        lbScore = new JLabel("Current score: 0");
        lbCurrentQuiz = new JLabel(String.format("Current quiz: %d/%d", currentQuiz + 1, quizzes.size()));

        panelScore.add(lbScore);
        panelScore.add(Box.createRigidArea(new Dimension(60, 0)));
        panelScore.add(lbCurrentQuiz);

        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelContainer.add(panelQuestion);
        panelContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        panelContainer.add(panelOptions);
        panelContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        panelContainer.add(panelScore);
        panelContainer.setPreferredSize(new Dimension(600, 400));

        add(panelContainer);
    }

    public int showDialog() {
        setTitle("Quizzes");
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);

        return score;
    }

    private void loadNextQuiz() {
        for (JButton btnOption : btnOptions) {
            btnOption.setBorder(UIManager.getBorder("Button.border"));
        }

        currentQuiz++;

        if (currentQuiz == quizzes.size()) {
            JOptionPane.showMessageDialog(me, String.format("Congratulation! Your score is: %d", score));
            setVisible(false);
            return;
        }

        lbQuestion.setText(quizzes.get(currentQuiz).getShortQuestion(60));
        lbCurrentQuiz.setText(String.format("Current quiz: %d/%d", currentQuiz + 1, quizzes.size()));

        for (int i = 0; i < btnOptions.length; i++) {
            btnOptions[i].setText(String.format("<html><p>%s</p></html>", quizzes.get(currentQuiz).getOptions().get(i)));
        }
    }

    private void checkAnswer(int yourAnswerIndex) {
        for (int i = 0; i < btnOptions.length; i++) {
            if (quizzes.get(currentQuiz).checkAnswer(i)) {
                btnOptions[i].setBorder(new LineBorder(Color.GREEN, 5));

                if (i == yourAnswerIndex) {
                    score++;
                    lbScore.setText("Current score: " + score);
                }
            } else if (i == yourAnswerIndex) {
                btnOptions[i].setBorder(new LineBorder(Color.RED, 5));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.startsWith("check#")) {
            setEnableAllButtons(false);
            int index = Integer.parseInt(command.split("#")[1]);

            checkAnswer(index);

            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    setEnableAllButtons(true);
                    loadNextQuiz();
                }
            });

            timer.setRepeats(false);
            timer.start();
        }
    }

    private void setEnableAllButtons(boolean isEnable) {
        for (JButton btn : btnOptions) {
            if (isEnable) {
                btn.addActionListener(this);
            } else {
                btn.removeActionListener(this);
            }
        }
    }
}
