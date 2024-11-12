package AppEncarrecs;

public class Utilitats {
    public static String normalizaNom(String envNom){
        String nombre = envNom.replaceAll("\s+", " ").strip();
        return nombre;
    }
    public static boolean valorAdecuadoNom(String envNom){
        //Regex para ver si el nombre solo contiene letras del alfabeto en general
        // \\p{L} alternativo a a-zA-záéíóúàèìòùÁÉÍÓÚÀÈÌÒÙüÜñÑ
        return envNom.matches("[\\p{L}\\s]*");
    }
    public static boolean telefonCorrect(String te){
        // Regex que mira si el telefono solo contiene 9 números o un +34 delante
        return te.matches("^\\+34\\s?\\d{9}$|^\\d{9}$");
    }

    public static boolean esNumero(String num){
        return num.matches("\\d+");
    }
}
