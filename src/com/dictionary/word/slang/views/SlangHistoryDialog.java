package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangHistory;
import com.dictionary.word.slang.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.AbstractMap;

class SlangHistoryDialog extends JDialog implements ActionListener {
    private AbstractMap.SimpleEntry<Integer, String> result = null;
    private DefaultListModel<String> model;
    private JButton btnClear;
    private JComboBox<String> cbxHistoryBy;
    private JList<String> listHistory;

    SlangHistoryDialog(Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        model = new DefaultListModel<>();
        model.addAll(SlangHistory.getInstance().getSlangHistory());

        btnClear = new JButton("Clear");
        btnClear.setPreferredSize(new Dimension(90, 30));
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(this);

        cbxHistoryBy = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxHistoryBy.setPreferredSize(new Dimension(150, 30));
        cbxHistoryBy.addActionListener(this);

        listHistory = new JList<>(model);
        listHistory.setFixedCellHeight(30);
        listHistory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (listHistory.getSelectedValue() != null) {
                        result = new AbstractMap.SimpleEntry<>(
                                cbxHistoryBy.getSelectedIndex(),
                                listHistory.getSelectedValue()
                        );
                        setVisible(false);
                    }
                }
            }
        });

        JScrollPane spHistory = new JScrollPane(listHistory);
        spHistory.setPreferredSize(new Dimension(300, 350));

        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panelControl.add(btnClear);
        panelControl.add(Box.createRigidArea(new Dimension(60, 0)));
        panelControl.add(cbxHistoryBy);

        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setBorder(new EmptyBorder(0, 10, 10, 10));
        panelContainer.add(panelControl);
        panelContainer.add(spHistory);

        add(panelContainer);
    }

    public AbstractMap.SimpleEntry<Integer, String> showDialog() {
        setTitle("History");
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);

        return result;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source.equals(btnClear)) {
            handleClearHistory();
        }

        if (source.equals(cbxHistoryBy)) {
            handleSwapHistoryList();
        }
    }

    private void handleClearHistory() {
        if (model.isEmpty()) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear the history?",
                "Warning",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (cbxHistoryBy.getSelectedIndex() == 0) {
                SlangHistory.getInstance().clearSlangHistory();
            } else {
                SlangHistory.getInstance().clearDefinitionHistory();
            }

            model.removeAllElements();
        }
    }

    private void handleSwapHistoryList() {
        model.removeAllElements();

        if (cbxHistoryBy.getSelectedIndex() == 0) {
            model.addAll(SlangHistory.getInstance().getSlangHistory());
        } else {
            model.addAll(SlangHistory.getInstance().getDefinitionHistory());
        }
    }
}
