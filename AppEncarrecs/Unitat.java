package AppEncarrecs;

public enum Unitat {
    KG("kg"),
    GR("gr"),
    L("l"),
    ML("ml"),
    UD("ud");
    private String unidad; 
    Unitat(String unidad){
        this.unidad = unidad;
    }

    public String getUnitat(){
        return unidad;
    }

    public static Unitat fromString(String unidad){
        for(Unitat u: Unitat.values()){
            if(u.getUnitat().equalsIgnoreCase(unidad))return u;
        }
        throw new IllegalArgumentException("El tipo de unidad no es correcta, posibles unidades: kg, gr, l, ml, ud");
    }
}
