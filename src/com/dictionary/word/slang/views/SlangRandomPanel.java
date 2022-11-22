package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangDictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SlangRandomPanel extends JPanel implements ActionListener {
    private JTextField tfSlang;
    private JTextField tfDefinition;
    private JButton btnRandom;

    SlangRandomPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        JLabel lbSlangRandom = new JLabel("Slang: ");
        JLabel lbDefinitionRandom = new JLabel("Definition: ");

        tfSlang = new JTextField();
        tfSlang.setPreferredSize(new Dimension(300, 30));
        tfSlang.setEditable(false);
        tfSlang.setBackground(Color.WHITE);

        tfDefinition = new JTextField();
        tfDefinition.setPreferredSize(new Dimension(300, 30));
        tfDefinition.setEditable(false);
        tfDefinition.setBackground(Color.WHITE);

        lbSlangRandom.setLabelFor(tfSlang);
        lbDefinitionRandom.setLabelFor(tfDefinition);

        btnRandom = new JButton("Random");
        btnRandom.setFocusPainted(false);
        btnRandom.setPreferredSize(new Dimension(100, 30));
        btnRandom.addActionListener(this);
        btnRandom.doClick();

        setLayout(new GridBagLayout());

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Did you know?"),
                BorderFactory.createEmptyBorder(10, 10, 5, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        add(lbSlangRandom, gbc);
        gbc.gridy = 1;
        add(lbDefinitionRandom, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(tfSlang, gbc);
        gbc.gridy = 1;
        add(tfDefinition, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JPanel panelButtonContainer = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        panelButtonContainer.add(btnRandom);
        add(panelButtonContainer, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source.equals(btnRandom)) {
            List<String[]> slang = SlangDictionary.getInstance().generateRandomSlangList(1);

            if (slang == null) {
                return;
            }

            tfSlang.setText(slang.get(0)[0]);
            tfSlang.setCaretPosition(0);

            tfDefinition.setText(slang.get(0)[1]);
            tfDefinition.setCaretPosition(0);
        }
    }
}
