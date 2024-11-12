package AppEncarrecs;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    public static void llegirArxiu(String tipo, int pos){
        File archivoALeer = getArxiu(tipo, pos);
        if(tipo.equals("a")){
            llegirAleatori(archivoALeer);
        }else if(tipo.equals("s")){
            llegirSerialitzat(archivoALeer);
        }
    }

    public static void ficSerialitzat(ArrayList<Encarrec> listaEncarrecs){
        File ruta = new File("./Serializados");
        if(!ruta.exists()){
            ruta.mkdir();
        }
        try {
            String localdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
            File escribirFile = new File(ruta + "/" + localdate + ".dat");
            if(!escribirFile.exists()){
                escribirFile.createNewFile();
            }
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(escribirFile));
            obj.writeObject(listaEncarrecs);
            obj.close();
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage()); 
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void llegirSerialitzat(File readFile){
        try{
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(readFile));
            @SuppressWarnings("unchecked")
            ArrayList<Encarrec> encarrecs = (ArrayList<Encarrec>)obj.readObject();
            printEncarrecs(encarrecs);
            obj.close();
        }catch(FileNotFoundException f){
            System.out.println(f.getMessage());
        }catch(ClassNotFoundException c){
            System.out.println(c.getMessage());
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void ficAleatorio(ArrayList<Encarrec> listaEncarrecs){
        File ruta = new File("./Aleatorios");
        if(!ruta.exists()){
            ruta.mkdir();
        }
        try {
            String localdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
            RandomAccessFile raf1 = new RandomAccessFile(ruta + "/" + localdate + ".dat", "rw");
            for (Encarrec encarrec : listaEncarrecs) {
                raf1.writeInt(encarrec.getId());
                raf1.writeInt(encarrec.getArticles().size());
                //System.out.println(encarrec.getId());
                raf1.writeChars(String.format("%-40s", encarrec.getNombre()));
                //System.out.println(encarrec.getNombre());
                raf1.writeChars(String.format("%-20s", encarrec.getData()));
                raf1.writeChars(String.format("%-12s", encarrec.getTelefono()));
                for (Article artic : encarrec.getArticles()) {
                    raf1.writeChars(String.format("%-20s", artic.getNombre()));
                    raf1.writeFloat(artic.getCantidad());
                    raf1.writeChars(String.format("%-10s", artic.getUnidad().getUnitat()));
                    raf1.writeFloat(artic.getPreu());
                }
                raf1.writeFloat(encarrec.getPreuTotal());
            }
            raf1.close();
        }catch(FileNotFoundException f){
            System.out.println(f.getMessage());
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static void llegirAleatori(File readFile){
        int pos = 0;
        ArrayList<Encarrec> encarrecsAleatori = new ArrayList<Encarrec>();
        try {
            RandomAccessFile llegirEnc =new RandomAccessFile(readFile, "r");
            while (llegirEnc.getFilePointer() != llegirEnc.length()) {
                llegirEnc.seek(pos);
                int id = llegirEnc.readInt();
                int medida = llegirEnc.readInt();
                String nomCli = leerString(llegirEnc, 40);
                String fecha = leerString(llegirEnc, 20);
                String telefono = leerString(llegirEnc, 12);
                List<Article> art = new ArrayList<Article>();
                for(int i = 0; i < medida; i++){
                    String nomArt = leerString(llegirEnc, 20);
                    float cantArt = llegirEnc.readFloat();
                    String unitArt = leerString(llegirEnc, 10);
                    float preuArt = llegirEnc.readFloat();
                    Article artic = new Article(nomArt, cantArt, unitArt, preuArt);
                    art.add(artic);
                }
                float preuTot = llegirEnc.readFloat();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate fechaObj = LocalDate.parse(fecha, format);
                Encarrec encarrecAleatori = new Encarrec(id, nomCli, telefono, fechaObj, art, preuTot);
                encarrecsAleatori.add(encarrecAleatori);
                pos = pos + (152 + (medida * 68) + 4);     
            }
            printEncarrecs(encarrecsAleatori);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    public static void editarAleatori(File readFile, int idSearch, String datoCambio, String canvi){
        
        try {
            RandomAccessFile llegirEnc =new RandomAccessFile(readFile, "rw");
            while (llegirEnc.getFilePointer() != llegirEnc.length()) {
                
                int id = llegirEnc.readInt();
                if(id != idSearch){
                    int medida = llegirEnc.readInt();
                    //La medida de mi encargo salta en caso de que el id no sea igual hasta el siguiente encargo
                    llegirEnc.skipBytes(144 + (medida * 68) + 4);     
                    continue;
                }else{
                    if(datoCambio.equals("t")||datoCambio.equals("telefono") || datoCambio.equals("teléfono")){
                        llegirEnc.readInt();
                        leerString(llegirEnc, 40);
                        leerString(llegirEnc, 20);
                        llegirEnc.writeChars(String.format("%-12s", canvi));
                        break;
                    }else if(datoCambio.equals("f")||datoCambio.equals("fecha")){
                        llegirEnc.readInt();
                        leerString(llegirEnc, 40);
                        llegirEnc.writeChars(String.format("%-20s", canvi));
                        break;
                    }
                }
            }
            System.out.println("Los datos actualizados son:");
            llegirAleatori(readFile);
        } catch(EOFException e){
            System.out.println("No se ha encontrado ningún encargo con el id introducido");
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String leerString(RandomAccessFile read, int lon) throws IOException{
        StringBuilder str = new StringBuilder(lon);
        for(int i = 0; i < lon; i++){
            str.append(read.readChar());
        }
        return str.toString().trim();
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
