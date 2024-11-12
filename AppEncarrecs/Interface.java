package AppEncarrecs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.File;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Interface {
    public static void main(String[] args) {
        System.out.println("BIENVENIDO A LA TIENDA");
        boolean seguir = true;
        while (seguir) {
            System.out.print("¿Qué deseas hacer?(h o Help para mostrar opciones): ");
            String acUser = Std.readLine();
            //Controlar las excepciones de la respuesta de usuario cuando la respuesta está en blanco
            //Modificar la respuesta a lower case para que sea más accesible
            acUser = acUser.toLowerCase();
            //administraEncargo(acUser);
            switch (acUser) {
                case "h","help":
                    showHelp();
                    break;
                case "a","agregar":
                    ArrayList<Encarrec> listaEnc = new ArrayList<Encarrec>();
                    pedirMuchosEncargos(listaEnc);
                    if(listaEnc.size() > 0){
                        System.out.println("Creando archivo XML...");
                        Fichero.crearXML(listaEnc);
                    }
                    break;
                case "s","show":
                    //Mientras la respuesta no sea correcta
                    boolean respCor = false;
                    while(!respCor){
                        try{
                            System.out.println("¿Qué tipo de archivo deseas leer?(Serializado[s], Aleatorio[a], Cancelar[c])");
                            String tipoArchivo = Std.readLine();
                            if(cancelarPedido(tipoArchivo))break;
                            switch (tipoArchivo.toLowerCase()) {
                                case "a":
                                    int quants = Fichero.mostrarArchivos("a");
                                    if(quants <= 0){
                                        throw new IllegalArgumentException("Has de crear algún elemento para editarlo, primero agregalo");
                                    }
                                    break;
                                case "s":
                                    int mida = Fichero.mostrarArchivos("s");
                                    if(mida <= 0){
                                        throw new IllegalArgumentException("Has de crear algún elemento para editarlo, primero agregalo");
                                    }
                                    break;
                                default:
                                    throw new IllegalArgumentException("Elige una opción válida");
                            }
                            System.out.println("Elige el número del archivo que deseas leer");
                            String archivNum = Std.readLine();
                            if(cancelarPedido(archivNum))break;
                            if(archivNum.isBlank()){
                                throw new IllegalArgumentException("Se debe especificar el número de archivo");
                            }else if(!Utilitats.esNumero(archivNum)){
                                throw new NumberFormatException("Se debe ingresar un número válido");
                            }
                            int numArchivo = Integer.parseInt(archivNum);
                            Fichero.llegirArxiu(tipoArchivo, numArchivo);
                        }catch(NumberFormatException e){
                            System.out.println(e.getMessage());
                        }catch(IllegalArgumentException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case "e","edit":
                    boolean respIs = false;
                    //Muestra un encargo en los archivos aleatorios o un fichero completo de serializado en 
                    try {
                            while(!respIs){
                            System.out.println("¿Qué archivo aleatorio deseas editar? Indica el número o c para cancelar");
                            int medida = Fichero.mostrarArchivos("a");
                            if(medida <= 0){
                                throw new IllegalArgumentException("Has de crear algún elemento para editarlo, primero agregalo");
                            }
                            String archivNum = Std.readLine();
                            if(cancelarPedido(archivNum))break;
                            if(archivNum.isBlank()){
                                throw new IllegalArgumentException("Se debe especificar el número de archivo");
                            }else if(!Utilitats.esNumero(archivNum)){
                                throw new NumberFormatException("Se debe ingresar un número válido");
                            }
                            int numArchivo = Integer.parseInt(archivNum);
                            Fichero.llegirArxiu("a", numArchivo);
                            System.out.println("Este es el contenido del archivo a editar");
                            System.out.println("Indica el número de id que deseas editar: ");
                            String encarrecNum = Std.readLine();
                            if(cancelarPedido(encarrecNum))break;
                            if(encarrecNum.isBlank()){
                                throw new IllegalArgumentException("Se debe especificar el número de encargo");
                            }else if(!Utilitats.esNumero(encarrecNum)){
                                throw new NumberFormatException("Se debe ingresar un número válido");
                            }
                            int numEncarrec = Integer.parseInt(encarrecNum);
                            System.out.println("¿Deseas cambiar el teléfono(t) o la fecha(f) de envío?");
                            String dataCambio = Std.readLine();
                            if(cancelarPedido(dataCambio))break;
                            if(dataCambio.isBlank()){
                                throw new IllegalArgumentException("Se debe especificar el dato a cambiar");
                            }
                            switch (dataCambio.toLowerCase()) {
                                case "fecha","f":
                                    System.out.println("¿Qué fecha deseas colocar?");
                                    LocalDate data = pideFecha();
                                    String fecha = data.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                    File fic = Fichero.getArxiu("a", numArchivo);
                                    Fichero.editarAleatori(fic, numEncarrec, dataCambio, fecha);
                                    break;
                                case "telefono","teléfono","t":
                                    System.out.println("¿Qué teléfono deseas colocar?");
                                    String tel = pideTelefono();
                                    fic = Fichero.getArxiu("a", numArchivo);
                                    Fichero.editarAleatori(fic, numEncarrec, dataCambio, tel);
                                    break;
                                default:
                                    System.out.println("Por favor selecciona una opción entre los datos editables");
                                    break;
                            }

                        }
                    }catch(NumberFormatException e){
                        System.out.println(e.getMessage());
                    }catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "f", "finalizar":
                    //Guardar el encargo en un archivo.csv y binario 
                    seguir = false;
                    break;
                default:
                    System.out.println("Por favor, introduce una orden disponible");
                    break;
            }
        }
    }

    public static String fechaFormatada(){
        LocalDateTime fechaEnv = LocalDateTime.now();
        DateTimeFormatter formatFic = DateTimeFormatter.ofPattern("dd-MM-yyyy HH mm ss");
        String fechaEnvio = fechaEnv.format(formatFic);
        return fechaEnvio;
    }

    public static boolean nulo(Object o){
        if(o == null){
            System.out.println("Se ha cancelado el pedido!");
            return true;
        }
        return false;
    }

    //Mostrar el mensaje de ayuda al usuario
    public static void showHelp(){
        System.out.println("h o Help: Para mostrar las opciones disponibles");
        System.out.println("a o Agregar: Para agregar algo al carrito");
        System.out.println("s o Show: Para mostrar un encargo");
        System.out.println("e o Editar: Para editar un encargo aleatorio");
        System.out.println("f o Finalizar: Para cancelar el encargo");
    }
    public static boolean cancelarPedido(String res){
        res = res.toLowerCase();
        return (res.equals("c")||res.equals("cancelar"));
    }

    
    public static LocalDate pideFecha(){
        boolean fechaValida = false;
        while (!fechaValida) {
            System.out.println("Introduce la fecha en la que deseas la entrega(c o cancelar para volver al inicio):");
            try {
                System.out.print("Dia: ");
                String diaStr = Std.readLine();
                if(cancelarPedido(diaStr))break;
                int dia = Integer.parseInt(diaStr);
                if(dia <= 0){
                    throw new IllegalArgumentException("El numero introducido debe ser positivo");
                }
                System.out.print("Mes: ");
                String mesStr = Std.readLine();
                if(cancelarPedido(mesStr))break;
                int mes = Integer.parseInt(mesStr);
                if(mes <= 0){
                    throw new IllegalArgumentException("El numero introducido debe ser positivo");
                }
                System.out.print("Año: ");
                String añoStr = Std.readLine();
                if(cancelarPedido(añoStr))break;
                int año = Integer.parseInt(añoStr);
                if(año <= 0){
                    throw new IllegalArgumentException("El numero introducido debe ser positivo");
                }
                LocalDate fechaIng = LocalDate.of(año, mes, dia);
                if(fechaIng.isBefore(LocalDate.now())){
                    throw new IllegalArgumentException("La fecha de entrega debe ser futura");
                }
                fechaValida = true;
                return fechaIng;
            } catch (NumberFormatException e) {
                //Imprime error en caso de no introducir un número
                System.out.println("Se debe introducir un número válido");
            } catch (IllegalArgumentException e){
                //Imprime el error de los numeros negativos y la fecha anterior a la actual
                System.out.println(e.getMessage());
            } catch(DateTimeException e){
                System.out.println("La fecha introducida no es correcta o no existe");
            } catch (Exception e){
                System.out.println("Error desconocido:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    } 

    public static String pideTelefono(){
        boolean telefonValido = false;
        while (!telefonValido) {
            try {
                System.out.print("Introduce tu teléfono(c o cancelar para volver al inicio): ");
                String telefono = Std.readLine();
                if(cancelarPedido(telefono))break;
                if(telefono.isBlank()){
                    throw new IllegalArgumentException("Añadir un teléfono es obligatorio");
                } else if(!Utilitats.telefonCorrect(telefono)){
                    throw new IllegalArgumentException("El teléfono es incorrecto puede ser: +34 XXXXXXXXX o XXXXXXXXX");
                }   
                telefonValido = true;
                return telefono;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("Error desconocido:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
        
    }

    public static String pideNombre(){
        boolean nomValido = false;
        while (!nomValido) {
            try {
                System.out.print("Introduce tu nombre(c o cancelar para volver al inicio): ");
                String nombre = Std.readLine();
                if(cancelarPedido(nombre))break;
                if(nombre.isBlank()){
                    throw new IllegalArgumentException("Añadir un nombre es obligatorio");
                } else if(!Utilitats.valorAdecuadoNom(nombre)){
                    throw new IllegalArgumentException("El nombre no puede contener numeros ni símbolos especiales");
                }   
                nomValido = true;
                return nombre;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("Error desconocido:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<Article> pideArticulos(){
        boolean noMasArt = false;
        ArrayList<Article> encargo = new ArrayList<Article>();
        while (!noMasArt) {
            try {
                System.out.println("Introduce los articulos que deseas(c o cancelar para finalizar)");
                System.out.print("Nombre del producto: ");
                String nomArt = Std.readLine();
                if(cancelarPedido(nomArt))break;
                System.out.print("Cantidad del producto: ");
                String cantStr = Std.readLine();
                if(cancelarPedido(cantStr))break;
                float cantidad = Float.parseFloat(cantStr);
                System.out.print("Unidad de medida del producto: ");
                String unidad = Std.readLine();
                if(cancelarPedido(cantStr))break;
                System.out.print("Precio unitario del producto: ");
                String preu = Std.readLine();
                if(cancelarPedido(cantStr))break;
                float precio = Float.parseFloat(preu);
                Article articulo = new Article(nomArt, cantidad, unidad, precio);
                if(encargo.add(articulo)){
                    System.out.println("Artículo añadido" + articulo.toString());
                }else{
                    throw new Exception("Problema añadiendo artículo");
                }
            } catch (NumberFormatException e) {
                System.out.println("Has de introducir un valor numérico válido");
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println("Error desconocido:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return encargo;
    }

    public static void pedirMuchosEncargos(ArrayList<Encarrec> listaEncarrecs){
        boolean respInc = true;
        while(respInc){
        
            String nom = pideNombre();
            if(nulo(nom))break;
            String telefono = pideTelefono();
            if(nulo(telefono))break;
            LocalDate fecha = pideFecha();
            if(nulo(fecha))break;
            List<Article> listaPr = pideArticulos();
            if(nulo(listaPr))break;

            Encarrec encarrec = new Encarrec(nom, telefono, fecha, listaPr);
            listaEncarrecs.add(encarrec);
            System.out.println("¿Deseas continuar añadiendo más encargos?");
            String resp = Std.readLine();
            if(!UtilitatsConfirmacio.respostaABoolean(resp)){
                // Crear los ficheros correspondientes preguntado primero
                respInc = false;
            }
        }
    }
}
