import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

public class XMLWriter {
    public static void writeXML(String fileName, Dictionary dictionary) throws ParserConfigurationException, TransformerException {
        List<Record> records = dictionary.getRecords();
        Document document = createDocument(records);

        Transformer transformer = createTransformer();
        writeToFile(fileName, transformer, document);
    }

    private static Document createDocument(List<Record> records) throws ParserConfigurationException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.setXmlStandalone(true);

        Element rootElement = document.createElement("dictionary");
        records.forEach(record -> {
            Element recordElement = document.createElement("record");

            Element wordElement = document.createElement("word");
            wordElement.appendChild(document.createTextNode(record.getWord()));
            recordElement.appendChild(wordElement);

            Element meaningElement = document.createElement("mean");
            meaningElement.appendChild(document.createTextNode(record.getMeaning()));
            recordElement.appendChild(meaningElement);

            rootElement.appendChild(recordElement);
        });
        document.appendChild(rootElement);
        return document;
    }

    private static Transformer createTransformer() throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

    private static void writeToFile(String file, Transformer transformer, Document document) throws TransformerException {
        try (OutputStream outputStream = new FileOutputStream(new File(file));
             Writer writer = new OutputStreamWriter(outputStream, "UTF-8")) {
            transformer.transform(new DOMSource(document), new StreamResult(writer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
