package AppEncarrecs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*; 
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class Fichero {
    public static File getArxiu(int pos){
        File nomArc = new File("");
        
        File getArc = new File("./");
        if (getArc.exists()) {
            File[] getFiles = getArc.listFiles();
            //Resto 1 al pos ya que si el usuario elige el 1 yo accedo al 0 del array
            nomArc = getFiles[pos +1];
        }else{
            System.out.println("Esta carpeta no existe por favor primero crealo desde agregar");
        }
        return nomArc;
    }

    public static int mostrarArchivos(){
        int count = 1;
        int medida = 0;
        File arc = new File("./");
        if(arc.exists()){
            File[] arcSerial = arc.listFiles();
            medida = arcSerial.length;
            for (File arxiu : arcSerial) {
                if(arxiu.isFile() && arxiu.getName().endsWith(".xml")){
                    System.out.println(String.format("%s (%d)",arxiu.getName(), count));
                    count++;
                }
            }
        }else{
            System.out.println("Esta carpeta no existe por favor primero crealo desde agregar");
        }
        return medida;
    }

    public static void crearXML(List<Encarrec> listaEncarrecs){
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            listaEncarrecs.sort(Comparator.comparing(Encarrec::getNombre));
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument (null,"encarrecs", null);
            document.setXmlVersion("1.0");
       
            for(Encarrec e: listaEncarrecs){
                int id = e.getId();
                String nombre = e.getNombre();
                String telefono = e.getTelefono();
                String fecha = e.getData();
                double total = e.getPreuTotal();
                List<Article> lista = e.getArticles();    
        
                Element arrel = document.createElement ("encarrec");
                arrel.setAttribute("id",Integer.toString(id));
                document.getDocumentElement().appendChild(arrel);

                CrearElement ("nombre",nombre.trim(), arrel, document);
                CrearElement ("telefono", telefono.trim(), arrel, document);
                CrearElement ("fecha", fecha.trim(),arrel, document);
                CrearElement ("total", Double.toString(total), arrel, document);

                for(Article a: lista){
                    String nomArt = a.getNombre();
                    float cantidad = a.getCantidad();
                    Unitat unidad = a.getUnidad();
                    float preu = a.getPreu();

                    Element tagArticle = document.createElement ("article");
                    arrel.appendChild(tagArticle);

                    CrearElement ("nomArt",nomArt.trim(), tagArticle, document);
                    CrearElement ("cantidad", Float.toString(cantidad), tagArticle, document);
                    CrearElement ("unidad", unidad.getUnitat(), tagArticle, document);
                    CrearElement ("preu", Float.toString(preu), tagArticle, document);
                }
            }
            // Crear orden por llave desde la API
            Source source = new DOMSource (document);

            String timeXML = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss"));
            Result result = new StreamResult (new FileWriter("encarrecs_" + timeXML + ".xml"));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            transformer.transform (source, result);
        
        } catch (Exception e ) { 
            System.err.println ("Error: " + e);}
    }
    
    public static void llegirDOMXML(int pos){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(getArxiu(pos));

                NodeList encarrecs = document.getElementsByTagName("encarrec");
        
                for (int i = 0; i < encarrecs.getLength(); i++) {
                    Node encarrec = encarrecs.item(i);
                    if (encarrec.getNodeType() == Node.ELEMENT_NODE){
                        
                        Element element = (Element) encarrec;
                        
                        System.out.printf("Id = %s %n",element.getAttribute("id"));
                        
                        System.out.printf(" * nombre cliente = %s %n", 
                        element.getElementsByTagName("nombre").item(0).getTextContent());
        
                        System.out.printf(" * fecha = %s %n", 
                        element.getElementsByTagName("fecha").item(0).getTextContent());
        
                        System.out.printf(" * telefono = %s %n",
                        element.getElementsByTagName("telefono").item(0).getTextContent());
                        
                        System.out.printf(" * total = %s %n",
                        element.getElementsByTagName("total").item(0).getTextContent());
                        
                        System.out.println("Artículos:");

                        NodeList articles = element.getElementsByTagName("article");

                        for(int j = 0; j < articles.getLength(); j++){
                            Node article = articles.item(j);

                            if(article.getNodeType() == Node.ELEMENT_NODE){
                                Element artElement = (Element) article;
                                System.out.printf(" * nombre de artículo = %s %n",
                                artElement.getElementsByTagName("nomArt").item(0).getTextContent());
                                
                                System.out.printf(" * cantidad = %s %n",
                                artElement.getElementsByTagName("cantidad").item(0).getTextContent());
                                
                                System.out.printf(" * unidad = %s %n",
                                artElement.getElementsByTagName("unidad").item(0).getTextContent());
                                
                                System.out.printf(" * preu = %s %n",
                                artElement.getElementsByTagName("preu").item(0).getTextContent());
                            }
                        }
                    }
                }
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void CrearElement (String dadaEmpleat, String valor, Element arrel, Document document) {
        Element elem = document.createElement (dadaEmpleat);
        Text text = document.createTextNode(valor);
        arrel.appendChild (elem);
        elem.appendChild (text);
    }

    public static void llegirSAX(int pos, String nomBuscado){
         try {
            // Crear el SAXParser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            EncarrecHandler handler = new EncarrecHandler();
            // Se modifica el nombre a buscarse que es enviado por el usuario desde interface
            handler.setTargetName(nomBuscado);
            // Procesar el archivo XML
            try {
                saxParser.parse(getArxiu(pos), handler);
            } catch (SAXException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

            // Obtener el resultado
            ArrayList<Encarrec> encargosEncontrado = handler.obtenerEncarrecs();
            if (encargosEncontrado != null) {
                printEncarrecs(encargosEncontrado);
            } else {
                System.out.println("Encargo no encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printEncarrecs(ArrayList<Encarrec> e) throws IOException{
        for (Encarrec c : e) {
            System.out.println("Id: " + c.getId());
            System.out.println("Nom del client: " + c.getNombre() + "\n");
            System.out.println("Telefon del client: " + c.getTelefono()+ "\n");
            System.out.println("Data de l'encarrec: " + c.getData() + "\n"); 
            System.out.println(String.format("%-15s%-15s%-15s%-15s\n", "Quantitat", "Unitats", "Article", "Preu"));
            System.out.println("=========================================================");
            for(Article a: c.getArticles()){
                System.out.println(String.format("%n%-15.2f%-15s%-15s%-15.2f%n",a.getCantidad(), a.getUnidad(),a.getNombre(),a.getPreu()));
            }
            System.out.println(String.format("%60s", ("Preu Total: " + c.getPreuTotal())));
        }
    }
}
