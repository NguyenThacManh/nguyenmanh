import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DictionaryTableModel extends AbstractTableModel {
    private List<Record> record;
    private final String[] columnNames = {"Word", "Mean"};

    public DictionaryTableModel(List<Record> record) {
        this.record = record;
    }

    @Override
    public int getRowCount() {
        return record.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Record record1 = record.get(row);
        switch (column) {
            case 0:
                return record1.getWord();
            case 1:
                return record1.getMeaning();
            default:
                throw new IllegalArgumentException("Chỉ số cột không hợp lệ");
        }
    }

}