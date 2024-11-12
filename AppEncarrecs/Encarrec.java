package AppEncarrecs;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Encarrec implements Serializable{
    // El cliente tiene nombre, telefono, se registra la fecha y hora en la que realiza el encargo, y una lista de articulos.

    private static final long serialVersionUID = 1L;

    private static int id = 0;
    private int idEncarrec = 0;
    private String nombre;
    private String telefono;
    private LocalDate data;
    private List<Article> lista;
    private float preuTotal;

    public Encarrec(int id, String nombre ,String telefono, LocalDate fecha, List<Article> lista, float total){
        this.idEncarrec = id;
        setNombre(nombre);
        setTelefon(telefono);
        this.data = fecha;
        this.lista = lista;
        setPreuTotal(total);
    }

    public Encarrec(String nombre ,String telefono, LocalDate fecha, List<Article> lista){
        setId();
        setIdEncarrec();
        setNombre(nombre);
        setTelefon(telefono);
        this.data = fecha;
        this.lista = lista;
        setPreuTotal();
    }

    public int getId(){
        return idEncarrec;
    }
    public String getNombre(){
        return this.nombre;
    }
    public String getData(){
        DateTimeFormatter formatEnv = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = this.data.format(formatEnv);
        return date;
    }
    public String getTelefono(){
        return this.telefono;
    }
    public List<Article> getArticles(){
        return this.lista;
    }
    public float getPreuTotal(){
        return this.preuTotal;
    }
    public void setNombre(String nombre){
        if(nombre.isBlank()){
            throw new IllegalArgumentException("Añadir un nombre es obligatorio");
        } else if(!Utilitats.valorAdecuadoNom(nombre)){
            throw new IllegalArgumentException("El nombre no puede contener numeros ni símbolos especiales");
        }
        //Colocar un regex de detección de letras sin numeros involucrados
        this.nombre = Utilitats.normalizaNom(nombre);
    }
    public void setTelefon(String tel){
        if(tel.isBlank()){
            throw new IllegalArgumentException("Añadir un teléfono es obligatorio");
        } else if(!Utilitats.telefonCorrect(tel)){
            throw new IllegalArgumentException("El teléfono es incorrecto puede ser: +34 XXXXXXXXX o XXXXXXXXX");
        }
        this.telefono = tel;
    }
    private void setId(){
        id += 1;
    }
     private void setIdEncarrec(){
        idEncarrec = id;
     }
    private void setPreuTotal(){
        for(Article pr: lista){
            preuTotal += pr.getPreu();
        }
    }

    public void setPreuTotal(float total){
        this.preuTotal = total;
    }
}
