package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangDictionary;
import com.dictionary.word.slang.utils.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.AbstractMap;
import java.util.List;

public class SlangFrame extends JFrame implements ActionListener {
    private final DefaultTableModel model;
    private JTextField tfSearch;
    private JComboBox<String> cbxSearchBy;
    private JButton btnSearch;
    private JButton btnHistory;
    private JLabel lbSearchTime;
    private JLabel lbSearchResultsCount;
    private String lastSearchKeyword = "";
    private int lastSelectedSearchByIndex = 0;
    private JButton btnRandom;
    private JTextField tfSlangRandom;
    private JTextField tfDefinitionRandom;

    public SlangFrame() {
        model = new DefaultTableModel(SlangDictionary.getInstance().getAll(), Constant.View.TABLE_COLUMN_NAMES);

        initComponents();
        loadFrameSettings();

        tfSearch.requestFocus();
    }

    private void initComponents() {
        // ========================= CONTROLS PANEL =========================
        JPanel panelControls = new JPanel();
        panelControls.setBorder(new TitledBorder("Controls"));
        panelControls.setPreferredSize(new Dimension(400, 220));

        // ========================= QUIZ PANEL =========================
        JPanel panelQuiz = new JPanel();
        panelQuiz.setBorder(new TitledBorder("Quizzes"));
        panelQuiz.setPreferredSize(new Dimension(400, 220));

        // ========================= QUIZ PANEL =========================
        JPanel panelRandom = new JPanel(new GridBagLayout());
        panelRandom.setBorder(new TitledBorder("Did you know?"));
//        panelRandom.setPreferredSize(new Dimension(400, 220));

        JLabel lbSlangRandom = new JLabel("Slang: ");
        JLabel lbDefinitionRandom = new JLabel("Definition: ");

        tfSlangRandom = new JTextField();
        tfSlangRandom.setPreferredSize(new Dimension(300, 30));
        tfSlangRandom.setEditable(false);

        tfDefinitionRandom = new JTextField();
        tfDefinitionRandom.setPreferredSize(new Dimension(300, 30));
        tfDefinitionRandom.setEditable(false);

        lbSlangRandom.setLabelFor(tfSlangRandom);
        lbDefinitionRandom.setLabelFor(tfDefinitionRandom);

        btnRandom = new JButton("Random");
        btnRandom.setPreferredSize(new Dimension(100, 30));
        btnRandom.addActionListener(this);
        btnRandom.doClick();

        GridBagConstraints gbcRandom = new GridBagConstraints();
        gbcRandom.insets = new Insets(5, 5, 5, 5);

        gbcRandom.gridx = 0;
        gbcRandom.gridy = 0;
        gbcRandom.anchor = GridBagConstraints.WEST;

        panelRandom.add(lbDefinitionRandom, gbcRandom);
        gbcRandom.gridy = 1;
        panelRandom.add(lbSlangRandom, gbcRandom);

        gbcRandom.gridx = 1;
        gbcRandom.gridy = 0;
        gbcRandom.weightx = 1;
        gbcRandom.fill = GridBagConstraints.HORIZONTAL;

        panelRandom.add(tfSlangRandom, gbcRandom);
        gbcRandom.gridy = 1;
        panelRandom.add(tfDefinitionRandom, gbcRandom);

        gbcRandom.gridx = 1;
        gbcRandom.gridy = 2;
        JPanel panelRandomButton = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        panelRandomButton.add(btnRandom);
        panelRandom.add(panelRandomButton, gbcRandom);

        // ========================= TABLE PANEL =========================
        JPanel panelTable = new JPanel(new GridLayout());

        JTable table = new JTable(model);
        table.setRowHeight(30);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane spTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spTable.setPreferredSize(new Dimension(600, 590));

        panelTable.add(spTable);

        // ========================= SEARCH PANEL =========================
        JPanel searchPanel = new JPanel();

        tfSearch = new JTextField();
        tfSearch.setColumns(25);
        tfSearch.setPreferredSize(new Dimension(150, 30));
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSearch();
                }
            }
        });

        cbxSearchBy = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxSearchBy.setPreferredSize(new Dimension(90, 30));

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(90, 30));
        btnSearch.addActionListener(this);

        btnHistory = new JButton("History");
        btnHistory.setPreferredSize(new Dimension(90, 30));
        btnHistory.addActionListener(this);

        searchPanel.add(tfSearch);
        searchPanel.add(cbxSearchBy);
        searchPanel.add(btnSearch);
        searchPanel.add(btnHistory);

        //  ========================= SEARCH DESCRIPTION PANEL =========================
        JPanel panelSearchDescription = new JPanel();
        panelSearchDescription.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        panelSearchDescription.setPreferredSize(new Dimension(300, 20));

        lbSearchResultsCount = new JLabel();
        lbSearchTime = new JLabel();

        panelSearchDescription.add(lbSearchResultsCount);
        panelSearchDescription.add(lbSearchTime);

        // ========================= LEFT PANEL =========================
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.add(panelControls);
        panelLeft.add(panelQuiz);
        panelLeft.add(panelRandom);

        // ========================= RIGHT PANEL =========================
        JPanel panelRight = new JPanel(new BorderLayout());
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.add(searchPanel);
        panelRight.add(panelSearchDescription);
        panelRight.add(panelTable);

        // ========================= CONTAINER PANEL =========================
        JPanel panelContainer = new JPanel(new GridBagLayout());
        panelContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(4, 2, 4, 2);
        panelContainer.add(panelLeft, gbc);
        panelContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        panelContainer.add(panelRight, gbc);

        add(panelContainer);
    }

    private void loadFrameSettings() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Slang Word Dictionary - Tâm Lê - 20127619");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnSearch)) {
            handleSearch();
        }

        if (source.equals(btnHistory)) {
            handleShowHistory();
        }

        if (source.equals(btnRandom)) {
            handleRandom();
        }
    }

    private void handleSearch() {
        int index = cbxSearchBy.getSelectedIndex();

        String[][] data = null;
        String keyword = tfSearch.getText();

        if (keyword.equals(lastSearchKeyword) && index == lastSelectedSearchByIndex) {
            return;
        }

        lastSearchKeyword = keyword;
        lastSelectedSearchByIndex = index;

        long start = System.currentTimeMillis();

        if (index == 0) {
            data = SlangDictionary.getInstance().searchBySlang(keyword);
        } else {
            data = SlangDictionary.getInstance().searchByDefinition(keyword);
        }

        long end = System.currentTimeMillis();

        lbSearchTime.setText(String.format("%ss", (end - start) / 1000.0));
        lbSearchResultsCount.setText(String.format("%d results", data.length));

        model.setRowCount(0);
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    private void handleShowHistory() {
        SlangHistoryDialog dialog = new SlangHistoryDialog(this);
        AbstractMap.SimpleEntry<Integer, String> result = dialog.showDialog();

        if (result != null) {
            cbxSearchBy.setSelectedIndex(result.getKey());
            tfSearch.setText(result.getValue());
            btnSearch.doClick();
        }
    }

    private void handleRandom() {
        btnRandom.setEnabled(false);
        List<String[]> slang = SlangDictionary.getInstance().generateRandomSlangList(1);

        tfSlangRandom.setText(slang.get(0)[0]);
        tfSlangRandom.setCaretPosition(0);

        tfDefinitionRandom.setText(slang.get(0)[1]);
        tfDefinitionRandom.setCaretPosition(0);
        btnRandom.setEnabled(true);
    }
}
