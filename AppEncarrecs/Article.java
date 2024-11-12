package AppEncarrecs;
import java.io.Serializable;
public class Article implements Serializable{

    private static final long serialVersionUID = 1L;

    private String nombre;
    private float cantidad;
    private Unitat unidad;
    private float preu;

    public Article(String nombre, float cantidad, String unidad, float preu){
        setCantidad(cantidad);
        setNombre(nombre);
        setUnidad(unidad);
        setPreu(preu);
    }

    public String getNombre(){
        return this.nombre;
    }
    public Unitat getUnidad(){
        return this.unidad;
    }
    public float getCantidad(){
        return this.cantidad;
    }
    public float getPreu(){
        return this.preu;
    }
    public void setNombre(String nombre){
        if(nombre.isBlank()){
            throw new IllegalArgumentException("Añadir un nombre es obligatorio");
        } else if(!Utilitats.valorAdecuadoNom(nombre)){
            throw new IllegalArgumentException("El nombre no puede contener numeros ni símbolos especiales");
        }
        this.nombre = Utilitats.normalizaNom(nombre);    
    }
    public void setUnidad(String unidad){
        if(unidad.isBlank()){
            throw new IllegalArgumentException("Añadir una unidad es obligatorio");
        }
        Unitat u = Unitat.fromString(unidad);
        this.unidad = u;
    }
    public void setCantidad(float cantidad){
        if(cantidad <= 0){
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad;    
    }
    public void setPreu(float preu){
        if(preu <= 0){
            throw new IllegalArgumentException("El precio ha de ser positivo");
        }
        this.preu = preu;
    }
    @Override
    public String toString(){
        return (getNombre()+ " " + getCantidad() + " " + getUnidad());
    }
}