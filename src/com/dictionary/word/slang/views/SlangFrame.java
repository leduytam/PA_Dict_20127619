package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangDictionary;
import com.dictionary.word.slang.utils.Constant;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.AbstractMap;

public class SlangFrame extends JFrame implements ActionListener {
    private final DefaultTableModel model;
    private JTextField tfKeyword;
    private JComboBox<String> cbxSearchBy;
    private JButton btnSearch;
    private JButton btnHistory;
    private JLabel lbSearchTime;
    private JLabel lbSearchResultsCount;
    private String lastSearchKeyword = "";
    private int lastSelectedSearchByIndex = 0;

    public SlangFrame() {
        model = new DefaultTableModel(SlangDictionary.getInstance().getAll(), Constant.View.TABLE_COLUMN_NAMES);

        initComponents();
        loadFrameSettings();
    }

    private void initComponents() {
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

        tfKeyword = new JTextField();
        tfKeyword.setColumns(20);
        tfKeyword.setPreferredSize(new Dimension(120, 30));
        tfKeyword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSearch();
                }
            }
        });

        cbxSearchBy = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxSearchBy.setPreferredSize(new Dimension(100, 30));

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(100, 30));
        btnSearch.addActionListener(this);

        btnHistory = new JButton("History");
        btnHistory.setPreferredSize(new Dimension(100, 30));
        btnHistory.addActionListener(this);

        searchPanel.add(tfKeyword);
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

        //  ========================= MAIN PANEL =========================
        JPanel panelContainer = new JPanel(new BorderLayout());
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.add(searchPanel);
        panelContainer.add(panelSearchDescription);
        panelContainer.add(panelTable);

        add(panelContainer);
    }

    private void loadFrameSettings() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Slang Word Dictionary - Tâm Lê");
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
    }

    private void handleSearch() {
        int index = cbxSearchBy.getSelectedIndex();

        String[][] data = null;
        String keyword = tfKeyword.getText();

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
            tfKeyword.setText(result.getValue());
            btnSearch.doClick();
        }
    }
}
