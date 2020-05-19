import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class java_creation {

    private static String databasename="";
    private static final String PATH=GetPath.getPath()+"/src";

    public static ArrayList<String> getDatafiles() throws ParserConfigurationException, IOException, SAXException {

        ArrayList<String> ddfiles= new ArrayList<>();
        File inputFile = new File(PATH+"/dd-files.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("file");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String filepath=PATH+eElement.getAttribute("path");
                ddfiles.add(filepath);

            }

        }
        return ddfiles;
    }


    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, SQLException, ClassNotFoundException {

        ArrayList<String> paths=getDatafiles();
        for (int temp = 0; temp < paths.size(); temp++) {

            ArrayList<HashMap<String,String>> table=xmlFilesParser(paths.get(temp));

            createTable obj1=new createTable();
            obj1.tablecreate(table);
        }

    }


    private static ArrayList<HashMap<String,String>> xmlFilesParser(String xmlpath) throws ParserConfigurationException, IOException, SAXException {

        ArrayList<HashMap<String,String>> xmlTablesCreate=new ArrayList<>();

        File inputFile = new File(xmlpath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        databasename=doc.getDocumentElement().getAttribute("name");
        NodeList tableList = doc.getElementsByTagName("table");

        for (int temp = 0; temp < tableList.getLength(); temp++) {
            Node tableNode = tableList.item(temp);
            HashMap<String, String> table = new HashMap<>();
            table.put("tablename",tableNode.getAttributes().getNamedItem("name").getNodeValue());
            Element tableElement = (Element) tableNode;
            Element columns=(Element) tableElement.getElementsByTagName("columns").item(0);
            NodeList columnList=columns.getElementsByTagName("column");
            table.put("columns",String.valueOf(columnList.getLength()));
            for(int temp2=0;temp2<columnList.getLength();temp2++){

                Node columnNode = columnList.item(temp2);
                Element columnElement = (Element) columnNode;
                table.put("col-name"+(temp2+1),columnElement.getAttribute("name"));
                table.put("d-t"+(temp2+1),columnElement.getElementsByTagName("data-type").item(0).getTextContent());
                NodeList autoincrement=columnElement.getElementsByTagName("auto-increment");
                if(autoincrement.getLength()>0){
                    table.put("a-i"+(temp2+1),autoincrement.item(0).getTextContent());
                }
                NodeList maxsize=columnElement.getElementsByTagName("max-size");
                if(maxsize.getLength()>0){
                    table.put("m-s"+(temp2+1),maxsize.item(0).getTextContent());
                }
                NodeList nullable=columnElement.getElementsByTagName("nullable");
                if(nullable.getLength()>0){
                    table.put("nullable"+(temp2+1),nullable.item(0).getTextContent());
                }

            }

            Element pk=(Element) tableElement.getElementsByTagName("primary-key").item(0);
            table.put("pk-name",pk.getAttribute("name"));
            table.put("pk-column",pk.getTextContent());

            NodeList fks=tableElement.getElementsByTagName("foreign-keys");
            if(fks.getLength()>0){
                table.put("fk-avail","true");
                Element fkElement=(Element) fks.item(0);
                NodeList fkList=fkElement.getElementsByTagName("foreign-key");
                table.put("fks",String.valueOf(fkList.getLength()));
                if(fkList.getLength()>0){
                    for(int temp3=0;temp3<fkList.getLength();temp3++){
                        Node foreignNode = fkList.item(temp3);
                        Element foreignElement = (Element) foreignNode;
                        table.put("fkreftable"+(temp3+1),foreignElement.getAttribute("reference-table-name"));
                        table.put("fkname"+(temp3+1),foreignElement.getAttribute("name"));

                        table.put("fklocalcol"+(temp3+1),foreignElement.getElementsByTagName("fk-local-column").item(0).getTextContent());
                        table.put("fkrefcol"+(temp3+1),foreignElement.getElementsByTagName("fk-reference-column").item(0).getTextContent());
                        NodeList fkconstraints=foreignElement.getElementsByTagName("fk-constraints");
                        if(fkconstraints.getLength()>0) {
                            table.put("fkconstraints" + (temp3 + 1), foreignElement.getElementsByTagName("fk-constraints").item(0).getTextContent());
                        }
                    }
                }
            }
            else{
                table.put("fk-avail","false");
            }

            NodeList uks=tableElement.getElementsByTagName("unique-keys");
            table.put("uk-name",uks.item(0).getAttributes().getNamedItem("name").getNodeValue());
            if(uks.getLength()>0) {
                table.put("uk-avail","true");
                Element ukElement=(Element) uks.item(0);
                NodeList ukList=ukElement.getElementsByTagName("unique-key-column");
                table.put("uks",String.valueOf(ukList.getLength()));
                for(int temp4=0;temp4<ukList.getLength();temp4++){
                    Node uniqueNode = ukList.item(temp4);
                    Element uniqueElement = (Element) uniqueNode;
                    table.put("uk-col"+(temp4+1),uniqueElement.getTextContent());
                }
            }

            else{
                table.put("uk-avail","false");
            }

            xmlTablesCreate.add(table);

        }

        return xmlTablesCreate;
    }
}