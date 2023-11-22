import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainFrame extends JFrame {
    static Color myThemeColor = Color.BLACK;
    static int myEditDictionaryType = 0;
    static int mySelectedLanguage = 0;
    Record recentRecord = null;
    DictionaryTableModel favoriteModel, recentModel;
    Dictionary favoriteDictionary, recentDictionary;

    JMenuBar menuBar;
    JMenu file, edit, fileImport, fileExport, editDictionary;
    JMenuItem fileImportENtoVN, fileImportVNtoEN,
            fileExportENtoVN, fileExportVNtoEN, editTheme,
            editDictionaryENtoVN, editDictionaryVNtoEN;
    JTable favoriteTable, recentTable;

    JTextArea outputTextArea, inputTextArea;

    MainFrame() {


        setTitle("Từ điển của nhóm ABCD");

        try {
            favoriteDictionary = XMLReader.readXML("Assets/Favorite.xml");
            if (favoriteDictionary == null) {
                favoriteDictionary = new Dictionary();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // favoriteDictionary = new Dictionary();
        favoriteModel = new DictionaryTableModel(favoriteDictionary.getRecords());
        try {
            recentDictionary = XMLReader.readXML("Assets/Recent.xml");
            if (recentDictionary == null) {
                recentDictionary = new Dictionary();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        recentModel = new DictionaryTableModel(recentDictionary.getRecords());
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        file = new JMenu("Tệp");
        menuBar.add(file);
        edit = new JMenu("Chỉnh sửa");
        menuBar.add(edit);
        fileImport = new JMenu("Import");
        fileImportENtoVN = new JMenuItem("Từ điển Anh - Việt");
        fileImportENtoVN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open XML File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
                fileChooser.setFileFilter(filter);
                int userSelection = fileChooser.showOpenDialog(fileImportENtoVN);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fileName = file.getPath();
                    DictionaryEN2VN.getInstance(fileName);
                    fileImportENtoVN.setEnabled(false);
                }
            }
        });
        fileImportVNtoEN = new JMenuItem("Từ điển Việt - Anh");
        fileImportVNtoEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open XML File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
                fileChooser.setFileFilter(filter);
                int userSelection = fileChooser.showOpenDialog(fileImportVNtoEN);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fileName = file.getPath();
                    DictionaryVN2EN.getInstance(fileName);
                    fileImportVNtoEN.setEnabled(false);
                }
            }
        });
        fileImport.add(fileImportENtoVN);
        fileImport.add(fileImportVNtoEN);
        file.add(fileImport);

        fileExport = new JMenu("Export");
        fileExportENtoVN = new JMenuItem("Từ điển Anh - Việt");
        fileExportENtoVN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save XML File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
                fileChooser.setFileFilter(filter);
                int userSelection = fileChooser.showSaveDialog(fileExportENtoVN);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    DictionaryEN2VN.getInstance().exportRecords(fileToSave.getAbsolutePath());
                }
            }
        });
        fileExportVNtoEN = new JMenuItem("Từ điển Việt - Anh");
        fileExportVNtoEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save XML File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
                fileChooser.setFileFilter(filter);
                int userSelection = fileChooser.showSaveDialog(fileExportVNtoEN);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    DictionaryVN2EN.getInstance().exportRecords(fileToSave.getAbsolutePath());
                }
            }
        });
        fileExport.add(fileExportENtoVN);
        fileExport.add(fileExportVNtoEN);
        file.add(fileExport);

        editTheme = new JMenuItem("Màu");
        edit.add(editTheme);

        editDictionary = new JMenu("Từ điển");
        editDictionaryENtoVN = new JMenuItem("Tiếng Anh - Tiếng Việt");
        editDictionaryENtoVN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myEditDictionaryType = 1;
                new EditFrame();
            }
        });
        editDictionaryVNtoEN = new JMenuItem("Tiếng Việt - Tiếng Anh");
        editDictionaryVNtoEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myEditDictionaryType = 2;
                new EditFrame();
            }
        });
        editDictionary.add(editDictionaryENtoVN);
        editDictionary.add(editDictionaryVNtoEN);
        edit.add(editDictionary);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel headerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(headerContainer, c);

        JPanel optionContainer = new JPanel(new GridBagLayout());
        String languages[] = { "(Chọn ngôn ngữ)", "Tiếng Anh", "Tiếng Việt" };

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 20);

        final JComboBox<String> inputOptionComboBox = new JComboBox<>(languages);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 30, 0, 60);
        optionContainer.add(inputOptionComboBox, c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 20, 0, 0);

        final JComboBox<String> outputOptionComboBox = new JComboBox<>(languages);
        c.gridx = 4;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 15);
        optionContainer.add(outputOptionComboBox, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(optionContainer, c);

        inputOptionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = inputOptionComboBox.getSelectedIndex();

                switch (selectedIdx) {
                    case 1:
                        mySelectedLanguage = 1;
                        outputOptionComboBox.setSelectedIndex(2);
                        break;
                    case 2:
                        mySelectedLanguage = 2;
                        outputOptionComboBox.setSelectedIndex(1);
                        break;
                    case 0:
                        mySelectedLanguage = 0;
                        outputOptionComboBox.setSelectedIndex(0);
                        break;
                }
            }

        });

        outputOptionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIdx = outputOptionComboBox.getSelectedIndex();

                switch (selectedIdx) {
                    case 1:
                        mySelectedLanguage = 2;
                        inputOptionComboBox.setSelectedIndex(2);
                        break;
                    case 2:
                        mySelectedLanguage = 1;
                        inputOptionComboBox.setSelectedIndex(1);
                        break;
                    case 0:
                        mySelectedLanguage = 0;
                        inputOptionComboBox.setSelectedIndex(0);
                        break;
                }
            }

        });

        JPanel textAreaContainer = new JPanel(new GridLayout(1, 2, 60, 0));

        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane1 = new JScrollPane(inputTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textAreaContainer.add(scrollPane1, c);

        inputTextArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    switch (mySelectedLanguage) {
                        case 0:
                            JOptionPane.showMessageDialog(inputTextArea, "Bạn hãy chọn ngôn ngữ!", "Lỗi!",
                                    JOptionPane.WARNING_MESSAGE);
                            break;
                        case 1:
                        case 2:
                            String keyWord = inputTextArea.getText().replace("\n", "").toLowerCase();
                            if (!search(keyWord)) {
                                JOptionPane.showMessageDialog(inputTextArea, "Không tìm thấy!", "Lỗi!",
                                        JOptionPane.WARNING_MESSAGE);
                                clearFields();
                            } else {
                                inputTextArea.setText(recentRecord.getWord());
                                recentDictionary.addRecordAtTop(recentRecord);
                                recentModel.fireTableDataChanged();
                            }
                            break;
                    }
                }
            }
        });

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane2 = new JScrollPane(outputTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textAreaContainer.add(scrollPane2, c);

        JPopupMenu popupMenu1 = new JPopupMenu();
        JMenuItem cut1 = new JMenuItem("Cut");
        JMenuItem copy1 = new JMenuItem("Copy");
        JMenuItem paste1 = new JMenuItem("Paste");

        copy1.addActionListener(e -> inputTextArea.copy());
        paste1.addActionListener(e -> inputTextArea.paste());
        cut1.addActionListener(e -> inputTextArea.cut());

        JPopupMenu popupMenu2 = new JPopupMenu();
        JMenuItem copy2 = new JMenuItem("Copy");

        copy2.addActionListener(e -> outputTextArea.copy());

        popupMenu1.add(cut1);
        popupMenu1.add(copy1);
        popupMenu1.add(paste1);
        inputTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu1.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    switch (e.getComponent().getName()) {
                        case "someComponentName1":
                            popupMenu1.show(e.getComponent(), e.getX(), e.getY());
                            break;
                    }
                }
            }


            @Override
            public void mouseEntered(MouseEvent e) {
                favoriteTable.clearSelection();
            }
        });

        popupMenu2.add(copy2);

        outputTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    switch (e.getComponent().getName()) {
                        case "someComponentName2":
                            popupMenu2.show(e.getComponent(), e.getX(), e.getY());
                            break;
                    }
                }
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu2.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        c.gridx = 0;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 10.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 30, 5, 30);
        mainPanel.add(textAreaContainer, c);

        JPanel extendContainer = new JPanel(new GridLayout(1, 2, 60, 0));
        JPanel favoriteContainer = new JPanel(new BorderLayout());
        JPanel stackContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addFavoriteButton = new JButton("Thêm từ yêu thích");
        stackContainer.add(addFavoriteButton);

        JButton deleteButton = new JButton("Xóa");
        stackContainer.add(deleteButton);

        favoriteContainer.add(stackContainer, BorderLayout.EAST);

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        extendContainer.add(favoriteContainer, c);
        JPanel recentContainer = new JPanel(new BorderLayout());
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        extendContainer.add(recentContainer, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(extendContainer, c);

        // Table
        JPanel tableContainer = new JPanel(new GridLayout(1, 2, 60, 0));

        // Favorite
        favoriteTable = new JTable(favoriteModel);

        ListSelectionModel favoriteTableModel = favoriteTable.getSelectionModel();
        favoriteTableModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        favoriteTableModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = favoriteTable.getSelectedRow();
                    if (index >= 0 && index < favoriteDictionary.getRecords().size()) {
                        inputTextArea.setText(favoriteTable.getValueAt(index, 0) + "");
                        outputTextArea.setText(favoriteTable.getValueAt(index, 1) + "");
                    }
                }
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane3 = new JScrollPane(favoriteTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableContainer.add(scrollPane3, c);

        // Recent
        recentTable = new JTable(recentModel);

        recentTable.setEnabled(false);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane4 = new JScrollPane(recentTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableContainer.add(scrollPane4, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 20.0;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(tableContainer, c);

        // Footer
        JLabel footerLabel = new JLabel("Từ điển Anh - Việt");
        footerLabel.setFont(new Font("Times", Font.PLAIN, 14));
        footerLabel.setForeground(myThemeColor);
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(footerLabel, c);

        editTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myThemeColor = JColorChooser.showDialog(edit, "Chỉnh sửa màu", Color.BLUE);
                EditFrame.myThemeColor = myThemeColor;
            }
        });

        addFavoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addFavoriteButton) {
                    if (recentRecord != null) {
                        favoriteDictionary.addRecord(recentRecord);
                        favoriteModel.fireTableDataChanged();
                        recentRecord = null;
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(favoriteTable,
                                "Hãy tìm kiếm từ trước khi thêm vào danh sách yêu thích!\nTips: Ấn \"Enter\" để tìm kiếm",
                                "Lỗi!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == deleteButton) {
                    int index = favoriteTable.getSelectedRow();
                    if (index >= 0) {
                        favoriteDictionary.removeRecord(index);
                        favoriteModel.fireTableDataChanged();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(favoriteTable, "Hãy chọn một hàng không mong muốn.");
                    }
                }
            }
        });
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    XMLWriter.writeXML("Assets/Favorite.xml", favoriteDictionary);
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                } catch (TransformerException e1) {
                    e1.printStackTrace();
                }
                try {
                    XMLWriter.writeXML("Assets/Recent.xml", recentDictionary);
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                } catch (TransformerException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) { }

            @Override
            public void windowDeactivated(WindowEvent e) { }
            
        });
        WordDateSearch.getInstance("Assets/History.txt");
        add(mainPanel);
        setSize(1000, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    boolean search(String keyWord) {
        int minDistance = Integer.MAX_VALUE;
        String closestWord = null;
        Dictionary dictionary = null;

        switch (mySelectedLanguage) {
            case 1:
                dictionary = DictionaryEN2VN.getInstance();
                break;
            case 2:
                dictionary = DictionaryVN2EN.getInstance();
                break;
        }

        for (Record record : dictionary.getRecords()) {
            int distance = Helper.LevenshteinDistance(keyWord, record.getWord().toLowerCase());
            if (distance == 0) {
                outputTextArea.setText(record.getMeaning());
                recentRecord = record;
                WordDateSearch.getInstance().addWordDateSearch(record.getWord());
                return true;
            } else if (distance < minDistance) {
                minDistance = distance;
                closestWord = record.getWord();
            }
        }

        int ans = JOptionPane.showConfirmDialog(inputTextArea, "Xin lỗi\n" + "Không tìm thấy:" + "\"" + keyWord +
                "\".\n" + "Từ đồng nghĩa với từ trên là: " + "\"" + closestWord + "\". Bạn muốn thay đổi không?");

        if (ans == JOptionPane.YES_OPTION) {
            return search(closestWord);
        }

        return false;
    }

    void clearFields() {
        inputTextArea.setText("");
        outputTextArea.setText("");
    }
}