package AppEncarrecs;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*; 
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;

public class Fichero {
    public static File getArxiu(String tipo, int pos){
        String carpeta = "";
        File nomArc = new File("");
        if(tipo.equals("a"))carpeta = "Aleatorios";
        if(tipo.equals("s"))carpeta = "Serializados";
        File getArc = new File("./" + carpeta);
        if (getArc.exists()) {
            File[] getFiles = getArc.listFiles();
            //Resto 1 al pos ya que si el usuario elige el 1 yo accedo al 0 del array
            nomArc = getFiles[pos - 1];
        }else{
            System.out.println("Esta carpeta no existe por favor primero crealo desde agregar");
        }
        return nomArc;
    }

    public static int mostrarArchivos(String denom){
        String carpeta = "";
        int count = 1;
        int medida = 0;
        if(denom.equals("a"))carpeta = "Aleatorios";
        if(denom.equals("s"))carpeta = "Serializados";
        File arc = new File("./" + carpeta);
        if(arc.exists()){
            File[] arcSerial = arc.listFiles();
            medida = arcSerial.length;
            for (File arxiu : arcSerial) {
                if(arxiu.isFile()){
                    System.out.println(String.format("%s (%d)",arxiu.getName(), count));
                    count++;
                }
            }
        }else{
            System.out.println("Esta carpeta no existe por favor primero crealo desde agregar");
        }
        return medida;
    }

    public static void crearXML(ArrayList<Encarrec> listaEncarrecs){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
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
        
                Element arrel = document.createElement ("empleat");
                arrel.setAttribute("id",Integer.toString(id));
                document.getDocumentElement().appendChild(arrel);

                CrearElement ("nombre",nombre.trim(), arrel, document);
                CrearElement ("telef", telefono.trim(), arrel, document);
                CrearElement ("age", Integer.toString(age),arrel, document);
                CrearElement ("height", Double.toString(height), arrel, document);
                CrearElement ("job", job.trim(),arrel, document);
            }
        
            Source source = new DOMSource (document);
            Result result = new StreamResult (new FileWriter("empleats.xml"));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            transformer.transform (source, result);
        
        } catch (Exception e ) { 
            System.err.println ("Error: " + e);}
        }
        
        public static void CrearElement (String dadaEmpleat, String valor, Element arrel, Document document) {
            Element elem = document.createElement (dadaEmpleat);
            Text text = document.createTextNode(valor);
            arrel.appendChild (elem);
            elem.appendChild (text);
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
   
    /* public void ficBinari(Encarrec c, String fechaFic) throws IOException{
        //Crear la carpeta binarios dentro de la ruta brindada por el usuario si no existe
        File carpBin = new File(ruta + "\\Binarios");
        System.out.println(carpBin);
        //Si no existe es creado
        if(!carpBin.exists()){
            carpBin.mkdir();
        }
        //Una vez creado creo el fichero binario dentro de este directorio
        File ficBin = new File(carpBin +"\\"+ c.getNombre()+fechaFic+".bin");
        System.out.println(ficBin);
        if (!ficBin.exists()) {
            ficBin.createNewFile();
        }
        //El fichero creado se vuelve un FileOutputStream y lo introduzco en un DataOutputStream
        try {FileOutputStream f = new FileOutputStream(ficBin);
            DataOutputStream writer = new DataOutputStream(f); 
            //Construir un String con el contenido de el cliente en formato binario
            //Escribir todo el string dentro del archivo
            writer.writeUTF(c.getNombre());
            writer.writeUTF(c.getTelefono());
            writer.writeUTF(c.getData());
            for(Article a:c.getArticles()){
                writer.writeUTF("\n");
                writer.writeUTF(a.getNombre());
                writer.writeUTF(a.getUnidad().toString());
                writer.writeFloat(a.getCantidad());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void ficCSV(Encarrec c, String fechaFic) throws IOException{
        File carpBin = new File(ruta + "/CSV");
        //Si no existe es creado
        if(!carpBin.exists()){
            carpBin.mkdir();
        }
        //Una vez creado creo el fichero binario dentro de este directorio
        File ficCSV = new File(carpBin +"/"+ c.getNombre()+fechaFic+".csv");
        System.out.println(ficCSV);
        if (!ficCSV.exists()) {
            ficCSV.createNewFile();
        }
        //En el fichero creado se escribe la informacion del cliente
        try {BufferedWriter writer = new BufferedWriter(new FileWriter(ficCSV));
            writer.write(c.getNombre() +";");
            writer.write(c.getTelefono()+";");
            writer.write(c.getData()+";");
            for(Article a: c.getArticles()){
                writer.write(a.getNombre()+","+a.getCantidad()+","+a.getUnidad()+";");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    

    public Encarrec csvToClient() throws IOException{
        String linea;
        DateTimeFormatter formatCSV = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Encarrec cli = null;
        BufferedReader doc = new BufferedReader(new FileReader(ruta));   
        while((linea = doc.readLine()) != null){
            String[] datos = linea.split(";");
            String nombre = datos[0];
            String telefono = datos[1];
            LocalDate data = LocalDate.parse(datos[2], formatCSV);
            String preu = datos[3];
            float precio = Float.parseFloat(preu);
            ArrayList<Article> lista = new ArrayList<>();
            for(int i = 3; i < datos.length; i++){
                String[] datosArt = datos[i].split(",");
                Article artCSV =new Article(datosArt[0], (Float.parseFloat(datosArt[1])), datosArt[2].toLowerCase(), precio);
                lista.add(artCSV);
            }
            cli = new Encarrec(nombre, telefono, data, lista);
        }
        doc.close();
        return cli;
    }
    public void clientToShow(String format) throws IOException{
        Encarrec c = null;
        if(format.equals("csv")){
            c = csvToClient();
        }else if(format.equals("bin")){
            c = binToClient();
        }
            StringBuilder s = new StringBuilder(); 
            s.append("Nom del client: " + c.getNombre() + "\n");
            s.append("Telefon del client: " + c.getTelefono() + "\n");
            s.append("Data de l'encarrrec: " + c.getData() + "\n");
            s.append(String.format("%-15s%-15s%-15s\n", "Quantitat", "Unitats", "Article"));
            s.append("=============================================");
            for(Article a: c.getArticles()){
                s.append(String.format("%n%-15s%-15s%-15s",a.getCantidad(), a.getUnidad(),a.getNombre()));
            }
            System.out.println(s.toString());
    }

    public Encarrec binToClient() throws IOException{
        DateTimeFormatter formatBIN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Encarrec cli = null;
         try (DataInputStream ficBin = new DataInputStream(new FileInputStream(ruta))) {
            String nombre = ficBin.readUTF();
            String telefono = ficBin.readUTF();
            String fecha = ficBin.readUTF();
            LocalDate data = LocalDate.parse(fecha, formatBIN);
            System.out.println(nombre);
            System.out.println(telefono);
            System.out.println(fecha);
            ArrayList<Article> lista = new ArrayList<>();
            while (ficBin.available() > 0) {
                ficBin.readUTF(); // \n
                String nombrePr = ficBin.readUTF();
                String unidadPr = ficBin.readUTF();
                float cantidadPr = ficBin.readFloat();
                float precioPr = ficBin.readFloat();
                System.out.println(nombrePr + unidadPr + cantidadPr);
                Article articulo = new Article(nombrePr, cantidadPr, unidadPr,precioPr);
                lista.add(articulo);
            }
        cli = new Encarrec(nombre, telefono, data, lista);
        } catch (IOException e) {
            throw e;
        }
        return cli;
    } */
}
