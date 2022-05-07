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
import java.util.Scanner;

public class TableOperations {

    public void insertNode(String openedFile){
        File xmlFile = new File(openedFile);
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
                            Element textNode = doc.createElement(el.getAttribute("type"));
                            int flag;
                            System.out.println(el.getAttribute("type") +" ("+ el.getTextContent() +"): ");

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

    public void describe(String openedFile) {
        File xmlFile = new File(openedFile);
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
                            System.out.println(el.getAttribute("type") + ": " + el.getTextContent());
                        }
                    }
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
