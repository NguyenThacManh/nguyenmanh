import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class WordDateSearchTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private static final int COLUMN_COUNT = 3;
    private static final String[] COLUMN_NAMES = { "Date", "Word", "Frequency" };

    private List<Object[]> rows;

    public WordDateSearchTableModel(Map<LocalDate, Map<String, Integer>> data) {
        if (data == null) {
            throw new IllegalArgumentException("Dữ liệu không thể để rỗng");
        }
        this.rows = createRows(data);
    }

    private List<Object[]> createRows(Map<LocalDate, Map<String, Integer>> data) {
        List<Object[]> rows = new ArrayList<>(data.size() * 10);
        for (LocalDate date : data.keySet()) {
            Map<String, Integer> wordMap = data.get(date);
            for (String word : wordMap.keySet()) {
                int frequency = wordMap.get(word);
                rows.add(new Object[] { date, word, frequency });
            }
        }
        return rows;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            return null;
        }
        Object[] row = rows.get(rowIndex);
        return (columnIndex >= 0 && columnIndex < COLUMN_COUNT) ? row[columnIndex] : null;
    }

    @Override
    public String getColumnName(int column) {
        return (column >= 0 && column < COLUMN_COUNT) ? COLUMN_NAMES[column] : null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return LocalDate.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            default:
                return null;
        }
    }
}
