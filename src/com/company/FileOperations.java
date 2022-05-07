package com.company;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;

public class FileOperations {

    public File openFile(String pathName) {
        File file = new File(pathName);
        //if the file does not exist it creates a new file with the given name
        if(!file.exists()) {
            Scanner sc = new Scanner(System.in);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //the root element equals the name of the file without the extension (.xml)
                String fileNameWithoutExtension = pathName.substring(0, pathName.lastIndexOf('.'));
                Element rootElement = doc.createElement(fileNameWithoutExtension);
                doc.appendChild(rootElement);

                //creating a new file requires creating a database template
                //the template is used to later add new nodes to the file
                //this is not an actual database record
                Element tagName = doc.createElement("config");
                rootElement.appendChild(tagName);
                Attr attr = doc.createAttribute("elementTag");
                System.out.println("Enter the node tag used for every database record:");
                String enterAttrValue = sc.nextLine();
                attr.setValue(enterAttrValue);
                tagName.setAttributeNode(attr);

                String confirmValue;
                do {
                    System.out.println("Enter xml column:");
                    String enterElement = sc.nextLine();
                    System.out.println("Enter the data type of the column:");
                    String enterValue = sc.nextLine();
                    Element elementName = doc.createElement("configElement");
                    Attr attrType = doc.createAttribute("type");
                    attrType.setValue(enterElement);
                    elementName.setAttributeNode(attrType);
                    elementName.appendChild(doc.createTextNode(enterValue));
                    tagName.appendChild(elementName);
                    //users can choose how many columns they want in their database
                    do {
                        System.out.println("Another element? Type 'yes' to continue, 'no' to cancel");
                        confirmValue = sc.nextLine();
                        if(!(confirmValue.equalsIgnoreCase("yes") || confirmValue.equalsIgnoreCase("no"))) {
                            System.out.println("Incorrect data, type your choice again");
                        }
                    } while (!(confirmValue.equalsIgnoreCase("yes") || confirmValue.equalsIgnoreCase("no")));
                } while (confirmValue.equalsIgnoreCase("yes"));

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(pathName));
                //formatting the xml file so every tag is written in a new line
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully opened " + file + "\n");
        return file;
    }
}
