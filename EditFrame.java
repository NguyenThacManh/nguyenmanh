import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditFrame extends JFrame {
    static Color myThemeColor = Color.BLUE;
    JButton clearButton, addButton, removeButton, updateButton;

    JTable editWordTable;
    DictionaryTableModel model;
    JTextArea editWordField, editMeaningField;
    JTextField findWordField;
    int indexFind;

    EditFrame() {
        switch (MainFrame.myEditDictionaryType) {
            case 1:
                model = new DictionaryTableModel(DictionaryEN2VN.getInstance().getRecords());
                break;
            case 2:
                model = new DictionaryTableModel(DictionaryVN2EN.getInstance().getRecords());
                break;

            default :
        editWordTable = new JTable(model);

        editWordTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        editWordTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = editWordTable.getSelectedRow();
                if (index >= 0 && index < model.getRowCount()) {
                    editWordField.setText(editWordTable.getValueAt(index, 0) + "");
                    editMeaningField.setText(editWordTable.getValueAt(index, 1) + "");
                }
            }
        });};

        setTitle("Chỉnh sửa " + ((MainFrame.myEditDictionaryType == 1) ? "English-Vietnamese" : "Vietnamese-English")
                + " Dictionary");

        JPanel stackPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 0, 10);

        JLabel findWordLabel = new JLabel("Tìm kiếm:");
        findWordLabel.setFont(new Font("Times", Font.BOLD, 14));
        findWordLabel.setForeground(myThemeColor);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(findWordLabel, c);

        findWordField = new JTextField();
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(findWordField, c);

        JLabel editWordLabel = new JLabel("Từ:");
        editWordLabel.setFont(new Font("Times", Font.BOLD, 14));
        editWordLabel.setForeground(myThemeColor);
        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(editWordLabel, c);

        editWordField = new JTextArea(2, 1);
        editWordField.setLineWrap(true);
        JScrollPane scrollPane1 = new JScrollPane(editWordField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        c.gridx = 0;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(scrollPane1, c);

        JLabel editMeaningLabel = new JLabel("Nghĩa của từ:");
        editMeaningLabel.setFont(new Font("Times", Font.BOLD, 14));
        editMeaningLabel.setForeground(myThemeColor);
        c.gridx = 0;
        c.gridy = 4;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(editMeaningLabel, c);

        editMeaningField = new JTextArea(10, 1);
        editMeaningField.setLineWrap(true);
        JScrollPane scrollPane2 = new JScrollPane(editMeaningField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(scrollPane2, c);

        clearButton = new JButton("Làm mới");
        clearButton.setPreferredSize(new Dimension(200, 25));
        c.gridx = 0;
        c.gridy = 6;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(clearButton, c);

        addButton = new JButton("Thêm");
        addButton.setPreferredSize(new Dimension(200, 25));
        c.gridx = 0;
        c.gridy = 7;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(addButton, c);

        removeButton = new JButton("Xóa");
        removeButton.setPreferredSize(new Dimension(200, 25));
        c.gridx = 0;
        c.gridy = 8;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(removeButton, c);

        updateButton = new JButton("Cập nhật");
        updateButton.setPreferredSize(new Dimension(200, 25));
        c.gridx = 0;
        c.gridy = 9;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        stackPanel.add(updateButton, c);

        JLabel nonLabel = new JLabel();
        c.gridx = 0;
        c.gridy = 10;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 40.0;
        c.fill = GridBagConstraints.BOTH;
        stackPanel.add(nonLabel, c);

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(editWordTable);
        panel.add(scrollPane);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

        add(panel, BorderLayout.CENTER);
        add(stackPanel, BorderLayout.EAST);

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = editWordField.getText().trim();
                String meaning = editMeaningField.getText().trim();
                Record newRecord = new Record(word, meaning);
                if ((!word.isEmpty()) && (!meaning.isEmpty())) {
                    switch (MainFrame.myEditDictionaryType) {
                        case 1:
                            DictionaryEN2VN.getInstance().addRecord(newRecord);
                            model.fireTableDataChanged();
                            break;
                        case 2:
                            DictionaryVN2EN.getInstance().addRecord(newRecord);
                            model.fireTableDataChanged();
                            break;
                        default:
                            break;
                    }
                    editWordTable.clearSelection();
                    JOptionPane.showMessageDialog(scrollPane, "Thêm thành công");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(scrollPane, "Hãy nhập từ và nghĩa của từ đấy");
                }
            }
        });


        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = editWordTable.getSelectedRow();
                if (index >= 0) {
                    int ans = JOptionPane.showConfirmDialog(scrollPane, "Bạn có chắc chắn xóa? "
                            + "\"" + editWordTable.getValueAt(index, 0) + "\"", "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) {
                        if (MainFrame.myEditDictionaryType == 1) {
                            switch (index) {
                                case 0:
                                    if (index < DictionaryEN2VN.getInstance().getRecords().size()) {
                                        DictionaryEN2VN.getInstance().removeRecord(index);
                                        model.fireTableDataChanged();
                                    }
                                    break;
                                // Add more cases if needed
                                default:
                                    // Handle other cases if necessary
                                    break;
                            }
                        }

                        if (MainFrame.myEditDictionaryType == 2) {
                            switch (index) {
                                case 0:
                                    if (index < DictionaryVN2EN.getInstance().getRecords().size()) {
                                        DictionaryVN2EN.getInstance().removeRecord(index);
                                        model.fireTableDataChanged();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        JOptionPane.showMessageDialog(scrollPane, "Xóa thành công!");
                    }
                } else {
                    JOptionPane.showMessageDialog(scrollPane, "Hãy chọn hàng bạn muốn bỏ.");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = editWordTable.getSelectedRow();
                if (index >= 0) {
                    String word = editWordField.getText().trim();
                    String meaning = editMeaningField.getText().trim();
                    Record updatedRecord = new Record(word, meaning);
                    if (MainFrame.myEditDictionaryType == 1) {
                        switch (index) {
                            case 0:
                                if (index < DictionaryEN2VN.getInstance().getRecords().size()) {
                                    DictionaryEN2VN.getInstance().updateRecord(index, updatedRecord);
                                    model.fireTableDataChanged();
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    if (MainFrame.myEditDictionaryType == 2) {
                        switch (index) {
                            case 0:
                                if (index < DictionaryVN2EN.getInstance().getRecords().size()) {
                                    DictionaryVN2EN.getInstance().updateRecord(index, updatedRecord);
                                    model.fireTableDataChanged();
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    JOptionPane.showMessageDialog(scrollPane, "Cập nhật thành công!");

                } else {
                    JOptionPane.showMessageDialog(scrollPane, "Hãy chọn hàng để cập nhật.");
                }
            }
        });

        findWordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (findWordField.getText() != "") {
                        String keyWord = findWordField.getText().toLowerCase();
                        int minDistance = Integer.MAX_VALUE;
                        String closestWord = null;
                        String closestMeaning = null;
                        Dictionary dictionary = null;
                        int index = 0;

                        if (MainFrame.myEditDictionaryType == 1) {
                            dictionary = DictionaryEN2VN.getInstance();
                        }

                        if (MainFrame.myEditDictionaryType == 2) {
                            dictionary = DictionaryVN2EN.getInstance();
                        }

                        for (Record record : dictionary.getRecords()) {
                            int distance = Helper.LevenshteinDistance(keyWord, record.getWord().toLowerCase());
                            if (distance < minDistance) {
                                minDistance = distance;
                                closestWord = record.getWord();
                                closestMeaning = record.getMeaning();
                                indexFind = index;
                            }
                            index++;
                        }
                        editWordField.setText(closestWord);
                        editMeaningField.setText(closestMeaning);
                        editWordTable.setRowSelectionInterval(indexFind, indexFind);

                        Rectangle rect = editWordTable.getCellRect(indexFind, 0, true);
                        editWordTable.scrollRectToVisible(rect);
                    }
                }
            }
        });

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setVisible(true);
    }

    void clearFields() {
        findWordField.setText("");
        editWordField.setText("");
        editMeaningField.setText("");
    }
}