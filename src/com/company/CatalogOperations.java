package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class CatalogOperations {

    public void importTable(String openedFile) {
        String filePath = openedFile + ".xml";
        File importedFile = new File(filePath);
        File xmlFile = new File("catalog.xml");
        if(importedFile.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                Element documentElement = doc.getDocumentElement();

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();
                boolean tableInDatabase = false;

                for(int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        String cElement =  eElement.getElementsByTagName("filePath").item(0).getTextContent();
                        if(cElement.equals(filePath)) {
                            tableInDatabase = true;
                            System.out.println(filePath + " already in the database\n");
                        }
                    }
                }

                if(!tableInDatabase) {
                    Element textNode = doc.createElement("fileName");
                    textNode.setTextContent(openedFile);
                    Element textNode1 = doc.createElement("filePath");
                    textNode1.setTextContent(filePath);
                    Element nodeElement = doc.createElement("table");
                    nodeElement.appendChild(textNode);
                    nodeElement.appendChild(textNode1);
                    documentElement.appendChild(nodeElement);
                    doc.replaceChild(documentElement, documentElement);

                    Transformer transformerFactory = TransformerFactory.newInstance().newTransformer();
                    transformerFactory.setOutputProperty(OutputKeys.METHOD, "xml");
                    transformerFactory.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformerFactory.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    Source source = new DOMSource(doc);
                    Result result = new StreamResult(xmlFile);
                    transformerFactory.transform(source, result);

                    System.out.println("Successfully imported " + filePath + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File does not exist!\n");
        }
    }
}
