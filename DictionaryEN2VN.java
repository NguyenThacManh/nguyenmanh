import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DictionaryEN2VN extends Dictionary {
    private static DictionaryEN2VN instance = null;
    private static final String DEFAULT_FILE_NAME = "Assets/Anh_Viet.xml";

    private DictionaryEN2VN(String file) {
        try {
            if (Files.isReadable(Paths.get(file))) {
                setRecords(XMLReader.readXML(file).getRecords());
            } else {
                System.err.println("File not found " + file);
            }
        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DictionaryEN2VN() {
        this(DEFAULT_FILE_NAME);
    }

    public static DictionaryEN2VN getInstance(String file) {
        if (instance == null) {
            synchronized (DictionaryEN2VN.class) {
                if (instance == null) {
                    instance = new DictionaryEN2VN(file);
                }
            }
        }
        return instance;
    }

    public static DictionaryEN2VN getInstance() {
        return getInstance(DEFAULT_FILE_NAME);
    }

    public void exportRecords(String fileName) {
        super.exportRecords(fileName, this);
    }
}
