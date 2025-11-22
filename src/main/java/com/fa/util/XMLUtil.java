package com.fa.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class XMLUtil {

    public static final Object DEFAULT_INDENT = 4;

    private static final Logger LOG = Logger.getLogger(XMLUtil.class);

    public static Document createEmptyDocument() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            LOG.error("Could not create a new document.");
            return null;
        }
    }

    public static void saveDocument(Document document, File file, Object indentSize) {
        try (FileOutputStream output = new FileOutputStream(file)) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indentSize);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            removeWhitespaces(document.getDocumentElement());
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeWhitespaces(Element element) {
        if (element == null) {
            return;
        }

        NodeList children = element.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child instanceof Text text && text.getData().isBlank()) {
                element.removeChild(child);
            } else if (child instanceof Element childElement) {
                removeWhitespaces(childElement);
            }
        }
    }

    public static Element[] getElements(NodeList nodeList) {
        List<Element> elements = new ArrayList<>();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node instanceof Element) {
                elements.add((Element) node);
            }
        }

        return elements.toArray(new Element[0]);
    }

    public static Element[] getElementsInReverse(NodeList nodeList) {
        List<Element> elements = new ArrayList<>();
        for(int i = nodeList.getLength() - 1; i >= 0; i--) {
            Node node = nodeList.item(i);
            if(node instanceof Element) {
                elements.add((Element) node);
            }
        }

        return elements.toArray(new Element[0]);
    }

    public static Document loadDocumentFromFile(File file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(file);
        } catch (Exception e) {
            LOG.error("Could not load document from file.", e);
            return null;
        }
    }

    public static Element getRootElement(Document document) {
        return document.getDocumentElement();
    }

    public static boolean createIfNotExisting(File file, String rootTag) {
        if (file.exists()) {
            return true;
        }

        boolean isCreated = FileUtil.createIfNotExisting(file);
        if (!isCreated) {
            return false;
        }

        Document document = XMLUtil.createEmptyDocument();
        Element root = document.createElement(rootTag);
        document.appendChild(root);

        XMLUtil.saveDocument(document, file, XMLUtil.DEFAULT_INDENT);
        return true;
    }

    public static boolean forEachSubElement(Element root, Consumer<Element> action) {
        if (root == null) {
            LOG.error("Cannot perform action on sub elements because root is null.");
            return false;
        }

        for (Element childElement : XMLUtil.getElements(root.getChildNodes())) {
            action.accept(childElement);
        }

        return true;
    }

    public static boolean forEachSubElement(Document document, Consumer<Element> action) {
        if (document == null) {
            LOG.error("Cannot perform action on sub elements because document is null.");
            return false;
        }

        Element root = document.getDocumentElement();
        return forEachSubElement(root, action);
    }

    public static boolean forEachSubElement(File file, Consumer<Element> action) {
        if (file == null) {
            LOG.error("Cannot perform action on sub elements because file is null.");
            return false;
        }

        Document document = XMLUtil.loadDocumentFromFile(file);
        return forEachSubElement(document, action);
    }

    public static boolean removeSubElements(File file, Predicate<Element> filter) {
        if (file == null) {
            LOG.error("Cannot remove sub elements because file is null.");
            return false;
        }

        Document document = XMLUtil.loadDocumentFromFile(file);
        if (document == null) {
            LOG.error("Cannot remove sub elements because document is null.");
            return false;
        }

        Element root = document.getDocumentElement();
        if (root == null) {
            LOG.error("Cannot remove sub elements because root is null.");
            return false;
        }

        for (Element childElement : XMLUtil.getElements(root.getChildNodes())) {
            if (filter.test(childElement)) {
                root.removeChild(childElement);
            }
        }

        XMLUtil.saveDocument(document, file, XMLUtil.DEFAULT_INDENT);
        return true;
    }
}
