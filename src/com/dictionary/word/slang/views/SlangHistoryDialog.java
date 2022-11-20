package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangHistory;
import com.dictionary.word.slang.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.AbstractMap;

public class SlangHistoryDialog extends JDialog {
    private AbstractMap.SimpleEntry<Integer, String> result = null;

    public SlangHistoryDialog(Frame parent) {
        super(parent, true);

        JButton btnClear = new JButton("Clear");
        btnClear.setPreferredSize(new Dimension(90, 30));

        JComboBox<String> cbxHistoryBy = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxHistoryBy.setPreferredSize(new Dimension(150, 30));

        DefaultListModel<String> model = new DefaultListModel<>();
        model.addAll(SlangHistory.getInstance().getSlangHistory());
        JList<String> listHistory = new JList<>(model);
        listHistory.setFixedCellHeight(30);

        listHistory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (listHistory.getSelectedValue() != null) {
                        result = new AbstractMap.SimpleEntry<>(cbxHistoryBy.getSelectedIndex(), listHistory.getSelectedValue());
                        setVisible(false);
                    }
                }
            }
        });

        JScrollPane spHistory = new JScrollPane(listHistory);
        spHistory.setPreferredSize(new Dimension(300, 350));

        cbxHistoryBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                model.removeAllElements();

                if (cbxHistoryBy.getSelectedIndex() == 0) {
                    model.addAll(SlangHistory.getInstance().getSlangHistory());
                } else {
                    model.addAll(SlangHistory.getInstance().getDefinitionHistory());
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (cbxHistoryBy.getSelectedIndex() == 0) {
                    SlangHistory.getInstance().clearSlangHistory();
                } else {
                    SlangHistory.getInstance().clearDefinitionHistory();
                }

                model.removeAllElements();
            }
        });

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
}
