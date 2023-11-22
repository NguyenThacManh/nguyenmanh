import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Dictionary extends AbstractTableModel {
    private List<Record> records = new ArrayList<>();

    public void addRecord(Record record) {
        String currentWord = Helper.UnicodeToASCII(record.getWord());
        if (records.stream().noneMatch(r -> Helper.UnicodeToASCII(r.getWord()).equals(currentWord))) {
            records.add(record);
        }
    }

    public void removeRecord(int index) {
        records.remove(index);
    }

    public void updateRecord(int index, Record record) {
        records.set(index, record);
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void sortRecords() {
        records.sort(Comparator.comparing(r -> r.getWord().toLowerCase()));
    }

    public void exportRecords(String fileName, Dictionary dictionary) {
        try {
            XMLWriter.writeXML(fileName, dictionary);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addRecordAtTop(Record record) {
        String currentWord = Helper.UnicodeToASCII(record.getWord());
        records.removeIf(r -> Helper.UnicodeToASCII(r.getWord()).equals(currentWord));
        records.add(0, record);

        if (records.size() > 30) {
            records.remove(records.size() - 1);
            fireTableRowsDeleted(records.size() - 1, records.size() - 1);
        }

        fireTableRowsInserted(0, 0);
    }

    @Override
    public int getRowCount() {
        return records.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
