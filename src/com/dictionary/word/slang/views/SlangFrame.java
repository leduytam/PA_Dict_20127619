package com.dictionary.word.slang.views;

import com.dictionary.word.slang.objects.SlangDictionary;
import com.dictionary.word.slang.utils.Constant;
import com.dictionary.word.slang.utils.FileIO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;
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

    private JTable table;

    private JButton btnRandom;
    private JTextField tfSlangRandom;
    private JTextField tfDefinitionRandom;

    private JButton btnQuiz;
    private JButton btnQuizStatistics;
    private JComboBox<String> cbxQuiz;

    private JTextField tfSlang;
    private JTextField tfDefinition;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnCancel;
    private ActionState actionState;

    private JButton btnRestoreDefault;

    private enum ActionState {
        Adding,
        Editing,
        None
    }

    public SlangFrame() {
        model = new DefaultTableModel(SlangDictionary.getInstance().getAll(), Constant.View.TABLE_COLUMN_NAMES);
        actionState = ActionState.None;

        initComponents();
        loadFrameSettings();
    }

    private void initComponents() {
        // ========================= CONTROLS PANEL =========================
        JPanel panelControls = new JPanel(new GridBagLayout());
        panelControls.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Controls"),
                BorderFactory.createEmptyBorder(10, 10, 5, 10)
        ));

        tfSlang = new JTextField();
        tfSlang.setPreferredSize(new Dimension(300, 30));
        tfSlang.setEditable(false);
        tfSlang.setBackground(Color.WHITE);

        tfDefinition = new JTextField();
        tfDefinition.setPreferredSize(new Dimension(300, 30));
        tfDefinition.setEditable(false);
        tfDefinition.setBackground(Color.WHITE);

        JLabel lbSlang = new JLabel("Slang:");
        lbSlang.setLabelFor(tfSlang);

        JLabel lbDefinition = new JLabel("Definition:");
        lbDefinition.setLabelFor(lbDefinition);

        btnAdd = new JButton("Add");
        btnAdd.setPreferredSize(new Dimension(90, 30));
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(this);

        btnEdit = new JButton("Edit");
        btnEdit.setPreferredSize(new Dimension(90, 30));
        btnEdit.setEnabled(false);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(this);

        btnDelete = new JButton("Delete");
        btnDelete.setPreferredSize(new Dimension(90, 30));
        btnDelete.setEnabled(false);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(this);

        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(90, 30));
        btnCancel.setEnabled(false);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);

        GridBagConstraints gbcControls = new GridBagConstraints();
        gbcControls.insets = new Insets(5, 5, 5, 5);

        gbcControls.gridx = 0;
        gbcControls.gridy = 0;
        gbcControls.anchor = GridBagConstraints.WEST;

        panelControls.add(lbSlang, gbcControls);
        gbcControls.gridy = 1;
        panelControls.add(lbDefinition, gbcControls);

        gbcControls.gridx = 1;
        gbcControls.gridy = 0;
        gbcControls.weightx = 1;
        gbcControls.fill = GridBagConstraints.HORIZONTAL;

        panelControls.add(tfSlang, gbcControls);
        gbcControls.gridy = 1;
        panelControls.add(tfDefinition, gbcControls);

        JPanel panelButtonControls = new JPanel(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        panelButtonControls.add(btnAdd);
        panelButtonControls.add(Box.createHorizontalStrut(10));
        panelButtonControls.add(btnEdit);
        panelButtonControls.add(Box.createHorizontalStrut(10));
        panelButtonControls.add(btnDelete);
        panelButtonControls.add(Box.createHorizontalStrut(10));
        panelButtonControls.add(btnCancel);

        gbcControls.gridx = 0;
        gbcControls.gridy = 2;
        gbcControls.gridwidth = 2;
        panelControls.add(panelButtonControls, gbcControls);

        // ========================= QUIZ PANEL =========================
        JPanel panelQuiz = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelQuiz.setBorder(BorderFactory.createTitledBorder("Quizzes"));

        btnQuiz = new JButton("Let's play");
        btnQuiz.setFocusPainted(false);
        btnQuiz.addActionListener(this);
        btnQuiz.setPreferredSize(new Dimension(110, 30));

        cbxQuiz = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxQuiz.setPreferredSize(new Dimension(90, 30));

        btnQuizStatistics = new JButton("View score statistics");
        btnQuizStatistics.setFocusPainted(false);
        btnQuizStatistics.addActionListener(this);
        btnQuizStatistics.setPreferredSize(new Dimension(200, 30));

        panelQuiz.add(btnQuiz);
        panelQuiz.add(cbxQuiz);
        panelQuiz.add(btnQuizStatistics);

        // ========================= RANDOM PANEL =========================
        JPanel panelRandom = new JPanel(new GridBagLayout());
        panelRandom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Did you know?"),
                BorderFactory.createEmptyBorder(10, 10, 5, 10)
        ));

        JLabel lbSlangRandom = new JLabel("Slang: ");
        JLabel lbDefinitionRandom = new JLabel("Definition: ");

        tfSlangRandom = new JTextField();
        tfSlangRandom.setPreferredSize(new Dimension(300, 30));
        tfSlangRandom.setEditable(false);
        tfSlangRandom.setBackground(Color.WHITE);

        tfDefinitionRandom = new JTextField();
        tfDefinitionRandom.setPreferredSize(new Dimension(300, 30));
        tfDefinitionRandom.setEditable(false);
        tfDefinitionRandom.setBackground(Color.WHITE);

        lbSlangRandom.setLabelFor(tfSlangRandom);
        lbDefinitionRandom.setLabelFor(tfDefinitionRandom);

        btnRandom = new JButton("Random");
        btnRandom.setFocusPainted(false);
        btnRandom.setPreferredSize(new Dimension(100, 30));
        btnRandom.addActionListener(this);
        btnRandom.doClick();

        GridBagConstraints gbcRandom = new GridBagConstraints();
        gbcRandom.insets = new Insets(5, 5, 5, 5);

        gbcRandom.gridx = 0;
        gbcRandom.gridy = 0;
        gbcRandom.anchor = GridBagConstraints.WEST;

        panelRandom.add(lbSlangRandom, gbcRandom);
        gbcRandom.gridy = 1;
        panelRandom.add(lbDefinitionRandom, gbcRandom);

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

        table = new JTable(model) {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(30);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(0);
        table.getTableHeader().setFont(new Font(new JLabel().getFont().getFontName(), Font.PLAIN, 14));
        table.getTableHeader().setEnabled(false);
        table.getSelectionModel().addListSelectionListener(this::handleSelectRowTable);

        JScrollPane spTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spTable.setPreferredSize(new Dimension(600, 600));
        table.getTableHeader().setPreferredSize(new Dimension(spTable.getHeight(), 30));

        panelTable.add(spTable);

        // ========================= SEARCH PANEL =========================
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));

        tfSearch = new JTextField();
        tfSearch.setColumns(30);
        tfSearch.setPreferredSize(new Dimension(200, 30));
        tfSearch.setBackground(Color.WHITE);
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                super.keyPressed(event);

                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSearch();
                }
            }
        });

        cbxSearchBy = new JComboBox<>(Constant.View.SEARCH_BY_VALUES);
        cbxSearchBy.setPreferredSize(new Dimension(100, 30));

        btnSearch = new JButton("Search");
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(100, 30));
        btnSearch.addActionListener(this);

        btnHistory = new JButton("History");
        btnHistory.setFocusPainted(false);
        btnHistory.setPreferredSize(new Dimension(100, 30));
        btnHistory.addActionListener(this);

        searchPanel.add(tfSearch);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(cbxSearchBy);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(btnSearch);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
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

        btnRestoreDefault = new JButton("Restore to default");
        btnRestoreDefault.addActionListener(this);
        btnRestoreDefault.setFocusPainted(false);
        btnRestoreDefault.setPreferredSize(new Dimension(150, 30));
        btnRestoreDefault.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelLeft.add(panelControls);
        panelLeft.add(Box.createVerticalStrut(20));
        panelLeft.add(panelQuiz);
        panelLeft.add(Box.createVerticalStrut(20));
        panelLeft.add(panelRandom);
        panelLeft.add(Box.createVerticalStrut(148));
        panelLeft.add(btnRestoreDefault);

        // ========================= RIGHT PANEL =========================
        JPanel panelRight = new JPanel(new BorderLayout());
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.add(searchPanel);
        panelRight.add(panelSearchDescription);
        panelRight.add(panelTable);

        // ========================= CONTAINER PANEL =========================
        JPanel panelContainer = new JPanel(new GridBagLayout());
        panelContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        try {
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Slang Word Dictionary - Tâm Lê - 20127619");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source.equals(btnSearch)) {
            handleSearch();
        }

        if (source.equals(btnHistory)) {
            handleShowHistory();
        }

        if (source.equals(btnRandom)) {
            handleShowRandomSlang();
        }

        if (source.equals(btnQuiz)) {
            handleShowQuizGame();
        }

        if (source.equals(btnQuizStatistics)) {
            handleShowQuizStatistics();
        }

        if (source.equals(btnAdd)) {
            handleAdd();
        }

        if (source.equals(btnEdit)) {
            handleEdit();
        }

        if (source.equals(btnDelete)) {
            handleDelete();
        }

        if (source.equals(btnCancel)) {
            setDefaultState();
        }

        if (source.equals(btnRestoreDefault)) {
            handleRestoreDefault();
        }
    }

    private void handleSelectRowTable(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        if (table.getSelectedRow() == -1) {
            setDefaultState();
            return;
        }

        int row = table.getSelectedRow();

        tfSlang.setText(model.getValueAt(row, 1).toString());
        tfSlang.setCaretPosition(0);

        tfDefinition.setText(model.getValueAt(row, 2).toString());
        tfDefinition.setCaretPosition(0);

        setEnableButtonAEDC(false, true, true, true);
    }

    private void handleAdd() {
        if (actionState == ActionState.None) {
            setAddingOrEditingState(ActionState.Adding);
        } else if (actionState == ActionState.Adding) {
            String slang = tfSlang.getText();
            String definition = tfDefinition.getText();

            if (slang.isBlank() || definition.isBlank()) {
                JOptionPane.showMessageDialog(this, "Slang and Definition is required");
                return;
            }

            int choice = JOptionPane.YES_OPTION;

            String message = """
                    This slang word is already exists.
                    Yes to add new slang word.
                    No to overwrite all duplicate slang words.
                    Cancel to discard.
                    """;

            if (SlangDictionary.getInstance().isExists(slang)) {
                choice = JOptionPane.showConfirmDialog(this, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            }

            if (choice == JOptionPane.YES_OPTION) {
                SlangDictionary.getInstance().addNew(slang, definition);
            } else {
                SlangDictionary.getInstance().addOverwrite(slang, definition);
            }

            reloadModel(SlangDictionary.getInstance().getAll());
            setDefaultState();
        }
    }

    private void handleEdit() {
        if (actionState == ActionState.None) {
            setAddingOrEditingState(ActionState.Editing);
        } else if (actionState == ActionState.Editing) {
            int selectedRow = table.getSelectedRow();
            String slang = (String) model.getValueAt(selectedRow, 1);
            String oldDefinition = (String) model.getValueAt(selectedRow, 2);
            int definitionIndex = Integer.parseInt((String) model.getValueAt(selectedRow, 3));

            String newDefinition = tfDefinition.getText();

            if (newDefinition.isBlank()) {
                JOptionPane.showMessageDialog(this, "Definition is required");
                return;
            }

            if (!newDefinition.equals(oldDefinition)) {
                SlangDictionary.getInstance().set(slang, newDefinition, definitionIndex);
                reloadModel(SlangDictionary.getInstance().getAll());
            }

            setDefaultState();
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this slang word?", "Warning", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            String slang = (String) model.getValueAt(selectedRow, 1);
            String definition = (String) model.getValueAt(selectedRow, 2);
            int definitionIndex = Integer.parseInt((String) model.getValueAt(selectedRow, 3));

            if (SlangDictionary.getInstance().remove(slang, definitionIndex)) {
                reloadModel(SlangDictionary.getInstance().getAll());
            }
        }

        setDefaultState();
    }

    private void setAddingOrEditingState(ActionState state) {
        if (state == ActionState.Adding) {
            setEnableButtonAEDC(true, false, false, true);

            btnAdd.setText("Create");

            tfSlang.setEditable(true);
            tfSlang.requestFocus();
            tfSlang.selectAll();
        } else if (state == ActionState.Editing) {
            setEnableButtonAEDC(false, true, false, true);
            btnEdit.setText("Update");
        }

        tfDefinition.setEditable(true);

        if (!tfSlang.isEditable()) {
            tfDefinition.requestFocus();
            tfDefinition.selectAll();
        }

        table.setEnabled(false);

        btnSearch.setEnabled(false);
        btnHistory.setEnabled(false);

        tfSearch.setEditable(false);

        actionState = state;
    }

    private void setDefaultState() {
        setEnableButtonAEDC(true, false, false, false);

        btnEdit.setText("Edit");
        btnAdd.setText("Add");

        tfSlang.setText("");
        tfSlang.setEditable(false);
        tfDefinition.setText("");
        tfDefinition.setEditable(false);

        table.setEnabled(true);
        table.clearSelection();

        btnSearch.setEnabled(true);
        btnHistory.setEnabled(true);

        tfSearch.setEditable(true);

        actionState = ActionState.None;
    }

    private void setEnableButtonAEDC(boolean add, boolean edit, boolean delete, boolean cancel) {
        btnAdd.setEnabled(add);
        btnEdit.setEnabled(edit);
        btnDelete.setEnabled(delete);
        btnCancel.setEnabled(cancel);
    }

    private void handleRestoreDefault() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to restore data to default?", "Warning", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_NO_OPTION) {
            SlangDictionary.getInstance().reset();
            reloadModel(SlangDictionary.getInstance().getAll());
            setDefaultState();
        }
    }

    private void handleSearch() {
        int index = cbxSearchBy.getSelectedIndex();

        String[][] data = null;
        String keyword = tfSearch.getText();

        long start = System.currentTimeMillis();

        if (index == 0) {
            data = SlangDictionary.getInstance().searchBySlang(keyword);
        } else {
            data = SlangDictionary.getInstance().searchByDefinition(keyword);
        }

        long end = System.currentTimeMillis();

        lbSearchTime.setText(String.format("%ss", (end - start) / 1000.0));
        lbSearchResultsCount.setText(String.format("%d results", data.length));

        reloadModel(data);
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

    private void handleShowRandomSlang() {
        List<String[]> slang = SlangDictionary.getInstance().generateRandomSlangList(1);

        tfSlangRandom.setText(slang.get(0)[0]);
        tfSlangRandom.setCaretPosition(0);

        tfDefinitionRandom.setText(slang.get(0)[1]);
        tfDefinitionRandom.setCaretPosition(0);
    }

    private void handleShowQuizGame() {
        SlangQuizDialog dialog = new SlangQuizDialog(this, cbxQuiz.getSelectedIndex() == 1);
        int score = dialog.showDialog();
        List<Integer> scores = FileIO.readScores(Constant.Path.SCORES);
        scores.add(score);
        FileIO.writeScores(scores, Constant.Path.SCORES);
    }

    private void handleShowQuizStatistics() {
        SlangQuizStatisticsDialog dialog = new SlangQuizStatisticsDialog(this);
        dialog.showDialog();
    }

    private void reloadModel(String[][] data) {
        model.setRowCount(0);
        for (String[] row : data) {
            model.addRow(row);
        }
    }
}
