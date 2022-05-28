package com.company;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

public class TableOperations {

    CatalogOperations catalogOperations = new CatalogOperations();

    public void insertNode(String fileName, File openedFile){
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Scanner sc = new Scanner(System.in);

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                Element documentElement = doc.getDocumentElement();
                NodeList nList = doc.getElementsByTagName("config");
                Element nodeElement = doc.createElement(nList.item(0).getAttributes().item(0).getTextContent());

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);

                    if (nNode.getNodeType() == nNode.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        NodeList configElement = eElement.getElementsByTagName("configElement");

                        for(int j = 0; j < configElement.getLength(); j++) {
                            Node node1 = configElement.item(j);

                            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                Element el = (Element) node1;
                                Element textNode = doc.createElement(el.getAttribute("columnName"));
                                System.out.println(el.getAttribute("columnName") +" ("+ el.getTextContent() +"): ");
                                String xmlText = sc.nextLine();

                                if (el.getTextContent().equalsIgnoreCase("string")) {
                                    textNode.setTextContent(xmlText);
                                    Attr attrType = doc.createAttribute("type");
                                    attrType.setValue(el.getTextContent());
                                    textNode.setAttributeNode(attrType);
                                    nodeElement.appendChild(textNode);
                                } else if (el.getTextContent().equalsIgnoreCase("integer")) {
                                    try {
                                        if(!xmlText.equalsIgnoreCase("null")) {
                                            Integer.parseInt(xmlText);
                                        }
                                    } catch (NumberFormatException e) {
                                        throw new NumberFormatException(xmlText + " is not a valid integer. Try again");
                                    }

                                    textNode.setTextContent(xmlText);
                                    Attr attrType = doc.createAttribute("type");
                                    attrType.setValue(el.getTextContent());
                                    textNode.setAttributeNode(attrType);
                                    nodeElement.appendChild(textNode);
                                } else if (el.getTextContent().equalsIgnoreCase("double")) {
                                    try {
                                        if(!xmlText.equalsIgnoreCase("null")) {
                                            Double.parseDouble(xmlText);
                                        }
                                    } catch (NumberFormatException e) {
                                        throw new NumberFormatException(xmlText + " is not a valid double. Try again");
                                    }

                                    textNode.setTextContent(xmlText);
                                    Attr attrType = doc.createAttribute("type");
                                    attrType.setValue(el.getTextContent());
                                    textNode.setAttributeNode(attrType);
                                    nodeElement.appendChild(textNode);
                                }
                            }
                        }
                    }
                }

                documentElement.appendChild(nodeElement);
                doc.replaceChild(documentElement, documentElement);

                System.out.println("New node added to the table");

                //writing the content into the xml file
                Transformer transformerFactory = TransformerFactory.newInstance().newTransformer();
                transformerFactory.setOutputProperty(OutputKeys.METHOD, "xml");
                transformerFactory.setOutputProperty(OutputKeys.INDENT, "yes");
                transformerFactory.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                Source source = new DOMSource(doc);
                Result result = new StreamResult(xmlFile);
                transformerFactory.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void describe(String fileName, File openedFile) {
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("config");
                Node nNode = nList.item(0);
                System.out.println(nNode.getNodeName());

                if (nNode.getNodeType() == nNode.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NodeList configElement = eElement.getElementsByTagName("configElement");

                    for(int i = 0; i < configElement.getLength(); i++) {
                        Node node1 = configElement.item(i);

                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element el = (Element) node1;
                            System.out.println(el.getAttribute("columnName") + ": " + el.getTextContent());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteColumn(String fileName, String columnName, String columnValue, File openedFile) {
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();
                boolean isDeleted = false;

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);

                    if (!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();

                            if (cElement.equals(columnValue)) {
                                root.removeChild(node);
                                isDeleted = true;
                            }
                        }
                    }
                }

                if (isDeleted) {
                    System.out.println("Deleted all columns '" + columnName + "' with value " + columnValue);
                } else {
                    System.out.println("Columns '" + columnName + "' with value " + columnValue + " not found");
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("temp.xml"));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateColumn(String fileName, String columnName, String columnValue, String newColumnValue, File openedFile) {
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();
                boolean isUpdated = false;

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);

                    if (!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String columnTextContent =  eElement.getElementsByTagName(columnName).item(0).getTextContent();
                            String columnDataType = eElement.getElementsByTagName(columnName).item(0).getAttributes().item(0).getTextContent();

                            if (columnTextContent.equals(columnValue)) {
                                if(columnDataType.equalsIgnoreCase("String")) {
                                    eElement.getElementsByTagName(columnName).item(0).setTextContent(newColumnValue);
                                    isUpdated = true;
                                } else if(columnDataType.equalsIgnoreCase("Integer")) {
                                    try {
                                        if(!newColumnValue.equalsIgnoreCase("null")) {
                                            Integer.parseInt(newColumnValue);
                                        }
                                    } catch (NumberFormatException e) {
                                        throw new NumberFormatException(newColumnValue + " is not a valid Integer. Try again");
                                    }

                                    eElement.getElementsByTagName(columnName).item(0).setTextContent(newColumnValue);
                                    isUpdated = true;
                                } else if(columnDataType.equalsIgnoreCase("Double")) {
                                    try {
                                        if(!newColumnValue.equalsIgnoreCase("null")) {
                                            Double.parseDouble(newColumnValue);
                                        }
                                    } catch (NumberFormatException e) {
                                        throw new NumberFormatException(newColumnValue + " is not a valid Double. Try again");
                                    }

                                    eElement.getElementsByTagName(columnName).item(0).setTextContent(newColumnValue);
                                    isUpdated = true;
                                }
                            }
                        }
                    }
                }

                if (isUpdated) {
                    System.out.println("All columns with name '" + columnName + "' and value '" + columnValue
                            + "' successfully updated with the new value '" + newColumnValue + "'");
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("temp.xml"));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void selectNode(String fileName, String columnName, String columnValue, File openedFile) {
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);

                    if (!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();

                            if (cElement.equals(columnValue)) {
                                NodeList configList = doc.getElementsByTagName("config");
                                Node configNode = configList.item(0);

                                if (configNode.getNodeType() == configNode.ELEMENT_NODE) {
                                    Element conElement = (Element) configNode;
                                    NodeList configElement = conElement.getElementsByTagName("configElement");

                                    for (int k = 0; k < configElement.getLength(); k++) {
                                        Node node1 = configElement.item(k);

                                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                            Element el = (Element) node1;
                                            System.out.println(el.getAttribute("columnName") + ": " +
                                                    eElement.getElementsByTagName(el.getAttribute("columnName")).item(0).getTextContent());
                                        }
                                    }
                                    System.out.println();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void count(String fileName, String columnName, String columnValue, File openedFile) {
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            int count = 0;

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);

                    if (!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();

                            if (cElement.equals(columnValue)) {
                                count++;
                            }
                        }
                    }
                }

                System.out.println("Nodes with value " + columnValue + " in column " + columnName + ": " + count + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addColumn(String fileName, String columnName, String columnType, File openedFile){
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                //check if column is already added to the table
                int flag = 0;
                NodeList nList = doc.getElementsByTagName("config");
                Node nNode = nList.item(0);

                if (nNode.getNodeType() == nNode.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NodeList configElement = eElement.getElementsByTagName("configElement");

                    for (int j = 0; j < configElement.getLength(); j++) {
                        Node node1 = configElement.item(j);

                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element el = (Element) node1;

                            if (el.getAttribute("columnName").equalsIgnoreCase(columnName)) {
                                flag = 1;
                                break;
                            }
                        }
                    }
                }

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                if (flag==0) {
                    boolean isAdded = true;

                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node node = childNodes.item(i);

                        //column type must be one of the three data types
                        if (columnType.equalsIgnoreCase("String") ||
                                columnType.equalsIgnoreCase("Integer") ||
                                columnType.equalsIgnoreCase("Double")) {

                            if (!node.getNodeName().equals("config")) {
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    //Create the new column
                                    Element elem = doc.createElement(columnName);
                                    Attr attr = doc.createAttribute("type");
                                    attr.setValue(columnType);
                                    elem.setAttributeNode(attr);
                                    //the new column does not have a value in the already existing nodes
                                    elem.setTextContent("NULL");

                                    //Add the column at the end of the parent node
                                    node.insertBefore(elem, node.getLastChild());
                                }
                            } else {
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    //Create the new column in the config node and assign its data type
                                    Element elem = doc.createElement("configElement");
                                    Attr attr = doc.createAttribute("columnName");
                                    attr.setValue(columnName);
                                    elem.setAttributeNode(attr);
                                    elem.setTextContent(columnType);

                                    //Add the column at the end of the config node
                                    node.insertBefore(elem, node.getLastChild());
                                }
                            }
                        } else {
                            System.out.println("Invalid column type");
                            isAdded = false;
                            break;
                        }
                    }

                    if (isAdded) {
                        System.out.println("Column successfully added");
                    }
                } else {
                    System.out.println("Column already in the table");
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("temp.xml"));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void print(String fileName, File openedFile){
        String filePath = fileName + ".xml";

        if(!filePath.equalsIgnoreCase(openedFile.getName())) {
            throw new RuntimeException("Not equal");
        }

        if (catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File("temp.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                String mainTag = doc.getElementsByTagName("config").item(0).getAttributes().item(0).getTextContent();
                NodeList childNodes = doc.getElementsByTagName(mainTag);

                int i = 0;
                String pageOption;
                Scanner sc = new Scanner(System.in);

                do {
                    Node node = childNodes.item(i);
                    System.out.println("page " + (i+1) + "/" + childNodes.getLength());
                    System.out.println("----------------------");

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        NodeList configList = doc.getElementsByTagName("config");
                        Node configNode = configList.item(0);
                        Element conElement = (Element) configNode;
                        NodeList configElement = conElement.getElementsByTagName("configElement");

                        for (int j = 0; j < configElement.getLength(); j++) {
                            Node node1 = configElement.item(j);

                            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                Element el = (Element) node1;
                                System.out.println(el.getAttribute("columnName") + ": " +
                                        eElement.getElementsByTagName(el.getAttribute("columnName")).item(0).getTextContent());
                            }
                        }
                    }

                    System.out.println("----------------------");

                    if (i > 0 && i < childNodes.getLength()) {
                        System.out.println("next | previous | exit");
                    } else if (i==0) {
                        System.out.println("next | exit");
                    } else {
                        System.out.println("previous | exit");
                    }

                    pageOption = sc.nextLine();

                    switch (pageOption) {
                        case "next":
                            if (i < childNodes.getLength()-1) {
                                i++;
                            } else {
                                System.out.println("This is the last element in the table");
                            }
                            break;
                        case "previous":
                            if (i > 0) {
                                i--;
                            } else {
                                System.out.println("This is the first element in the table");
                            }
                            break;
                        case "exit":
                            System.out.println("Closing print method");
                            break;
                        default:
                            System.out.println("Invalid command");
                    }
                } while (!pageOption.equalsIgnoreCase("exit"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
