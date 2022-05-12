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

    public void insertNode(String openedFile){
        String filePath = openedFile + ".xml";
        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
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
                                int flag;
                                System.out.println(el.getAttribute("columnName") +" ("+ el.getTextContent() +"): ");

                                if(el.getTextContent().equalsIgnoreCase("string")) {
                                    String xmlText = sc.nextLine();
                                    textNode.setTextContent(xmlText);
                                    nodeElement.appendChild(textNode);
                                }
                                else if(el.getTextContent().equalsIgnoreCase("integer")) {
                                    do{
                                        String xmlText = sc.nextLine();
                                        flag = 0;
                                        try {
                                            Integer.parseInt(xmlText);
                                        }
                                        catch (NumberFormatException e) {
                                            flag = 1;
                                            System.out.println(xmlText + " is not a valid integer. Try again");
                                        }
                                        if(flag == 0) {
                                            textNode.setTextContent(xmlText);
                                            nodeElement.appendChild(textNode);
                                        }
                                    } while (flag == 1);
                                }
                                else if(el.getTextContent().equalsIgnoreCase("double")) {
                                    do{
                                        String xmlText = sc.nextLine();
                                        flag = 0;
                                        try {
                                            Double.parseDouble(xmlText);
                                        }
                                        catch (NumberFormatException e) {
                                            flag = 1;
                                            System.out.println(xmlText + " is not a valid double. Try again");
                                        }
                                        if(flag == 0) {
                                            textNode.setTextContent(xmlText);
                                            nodeElement.appendChild(textNode);
                                        }
                                    } while (flag == 1);
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
        else {
            System.out.println("File not in the database");
        }
    }

    public void describe(String openedFile) {
        String filePath = openedFile + ".xml";
        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("config");

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    System.out.println(nNode.getNodeName());
                    if (nNode.getNodeType() == nNode.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        NodeList configElement = eElement.getElementsByTagName("configElement");
                        for(int j = 0; j < configElement.getLength(); j++) {
                            Node node1 = configElement.item(j);
                            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                Element el = (Element) node1;
                                System.out.println(el.getAttribute("columnName") + ": " + el.getTextContent());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File not in the database");
        }
    }

    public void deleteColumn(String openedFile, String columnName, String columnValue) {
        String filePath = openedFile + ".xml";
        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                boolean isDeleted = false;

                for(int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if(!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();
                            if(cElement.equals(columnValue)) {
                                root.removeChild(node);
                                isDeleted = true;
                            }
                        }
                    }
                }

                if(isDeleted) {
                    System.out.println("Deleted all columns '" + columnName + "' with value " + columnValue);
                } else {
                    System.out.println("Columns '" + columnName + "' with value " + columnValue + " not found");
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File not in the database");
        }
    }

    public void updateColumn(String openedFile, String columnName, String columnValue, String newColumnValue) {
        String filePath = openedFile + ".xml";
        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                boolean isUpdated = false;

                for(int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if(!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();
                            if(cElement.equals(columnValue)) {
                                eElement.getElementsByTagName(columnName).item(0).setTextContent(newColumnValue);
                                isUpdated = true;
                            }
                        }
                    }
                }

                if(isUpdated) {
                    System.out.println("Column '" + columnName + "' successfully updated with value " + newColumnValue);
                } else {
                    System.out.println("Invalid column name or value");
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File not in the database");
        }
    }

    public void selectNode(String openedFile, String columnName, String columnValue) {
        String filePath = openedFile + ".xml";
        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                for(int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if(!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();
                            if(cElement.equals(columnValue)) {
                                NodeList configList = doc.getElementsByTagName("config");

                                for (int j = 0; j < configList.getLength(); j++) {
                                    Node configNode = configList.item(j);
                                    if (configNode.getNodeType() == configNode.ELEMENT_NODE) {
                                        Element conElement = (Element) configNode;
                                        NodeList configElement = conElement.getElementsByTagName("configElement");
                                        for(int k = 0; k < configElement.getLength(); k++) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File not in the database");
        }
    }

    public void count(String openedFile, String columnName, String columnValue) {
        String filePath = openedFile + ".xml";
        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            int count = 0;

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();

                for(int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if(!node.getNodeName().equals("config")) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) node;
                            String cElement =  eElement.getElementsByTagName(columnName).item(0).getTextContent();
                            if(cElement.equals(columnValue)) {
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
        else {
            System.out.println("File not in the database");
        }
    }

    public void addColumn(String openedFile, String columnName, String columnType){
        String filePath = openedFile + ".xml";

        if(catalogOperations.queryCatalog(filePath)) {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                //check if column is already added to the table
                int flag = 0;
                NodeList nList = doc.getElementsByTagName("config");

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    if (nNode.getNodeType() == nNode.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        NodeList configElement = eElement.getElementsByTagName("configElement");
                        for(int j = 0; j < configElement.getLength(); j++) {
                            Node node1 = configElement.item(j);
                            if (node1.getNodeType() == node1.ELEMENT_NODE) {
                                Element el = (Element) node1;
                                if(el.getAttribute("columnName").equalsIgnoreCase(columnName)) {
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                    }
                }

                Element root = doc.getDocumentElement();
                NodeList childNodes = root.getChildNodes();
                if(flag==0) {
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
                                    //the new column does not have a value in the already existing nodes
                                    elem.setTextContent(" ");

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
                    if(isAdded) {
                        System.out.println("Column successfully added");
                    }
                }
                else {
                    System.out.println("Column already in the table");
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File not in the database");
        }
    }
}
