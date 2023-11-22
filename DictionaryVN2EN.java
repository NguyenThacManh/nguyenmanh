import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DictionaryVN2EN extends Dictionary {
    private static DictionaryVN2EN instance = null;
    private static final String DEFAULT_FILE_NAME = "Assets/Viet_Anh.xml";

    private DictionaryVN2EN(String file) {
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

    private DictionaryVN2EN() {
        this(DEFAULT_FILE_NAME);
    }

    public static DictionaryVN2EN getInstance(String file) {
        if (instance == null) {
            synchronized (DictionaryEN2VN.class) {
                if (instance == null) {
                    instance = new DictionaryVN2EN(file);
                }
            }
        }
        return instance;
    }

    public static DictionaryVN2EN getInstance() {
        return getInstance(DEFAULT_FILE_NAME);
    }

    public void exportRecords(String fileName) {
        super.exportRecords(fileName, this);
    }
}
