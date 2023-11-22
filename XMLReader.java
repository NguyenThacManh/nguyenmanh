import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLReader {
    public static Dictionary readXML(String fileName) throws Exception {
        File file = new File(fileName);
        Document document = buildDocument(file);
        List<Record> records = parseRecords(document);
        return createDictionary(records);
    }

    private static Document buildDocument(File file) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(file);
    }

    private static List<Record> parseRecords(Document document) {
        List<Record> records = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName("record");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String word = getElementTextContent(element, "word");
                String meaning = getElementTextContent(element, "meaning");
                Record record = new Record(word, meaning);
                records.add(record);
            }
        }
        return records;
    }

    private static String getElementTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent();
            }
        }
        return null;
    }
    private static Dictionary createDictionary(List<Record> records) {
        Dictionary dictionary = new Dictionary();
        dictionary.setRecords(records);
        return dictionary;
    }
}
