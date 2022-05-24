package com.company;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileOperations {

    private File file;

    public void openFile(String fileName) {
        String filePath = fileName + ".xml";
        file = new File(filePath);

        //if the file does not exist it creates a new file with the given name
        if(!file.exists()) {
            Scanner sc = new Scanner(System.in);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                //the root element equals the name of the file without the extension (.xml)
                Element rootElement = doc.createElement(fileName);
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
                    System.out.println("Enter the data type of the column (String/Integer/Double):");
                    String enterValue = sc.nextLine();
                    Element elementName = doc.createElement("configElement");
                    Attr attrType = doc.createAttribute("columnName");
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

                //writing the content into the xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                //formatting the xml file so every tag is written in a new line
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(source, result);
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }

        //creating a temporary file to make changes before saving
        copyContent(filePath, "temp.xml");

        System.out.println("Successfully opened " + file);
    }

    public void saveFile() {
        copyContent("temp.xml", file.getName());
        System.out.println("Successfully saved " + file.getName());
    }

    public void close() {
        if(file != null) {
            System.out.println("Successfully closed " + file.getName());
            file = null;
            try {
                Files.deleteIfExists(Paths.get("temp.xml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("There is not an opened file!");
        }
    }

    public File getFile() {
        return file;
    }

    private void copyContent(String readFrom, String writeTo) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(readFrom), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeTo), StandardCharsets.UTF_8));

            String line;

            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
