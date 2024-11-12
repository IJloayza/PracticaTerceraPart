package AppEncarrecs;

public class UtilitatsConfirmacio {
    /*
     * Donada una resposta textual, aquesta funció tradueix la resposta a
     * un booleà.
     * Considera true quan la resposta és, independentment de majúscules i
     * sense considerar espais a l'inici ni al final,
     * "sí", "s", "yes" o "y", i algunes variants amb errors ortogràfics.
     * Altrament considera false.
     */
    public static boolean respostaABoolean(String resposta) {
        if (null == resposta) {     // si la resposta és null, la donem com a false
            return false;
        }
        resposta = resposta.toLowerCase();
        if (resposta.equals("s") || resposta.equals("y")) {
            return true;
        }
        if (resposta.equals("sí") || resposta.equals("yes")) {
            return true;
        }
        if (resposta.equals("si") || resposta.equals("vale") || resposta.equals("yeah")) {
            return true;
        }
        return false;
    }
}

