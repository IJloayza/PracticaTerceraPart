package AppEncarrecs;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EncarrecHandler extends DefaultHandler {
    private boolean isTarget = false; // Para saber si el encargo es el que buscamos
    private String currentElement = "";
    private String targetName = "";

    private Encarrec currentEncarrec;
    private ArrayList<Encarrec> encarrecs;
    private List<Article> articles;
    private Article actualArticle;

    private int id;
    private String nombre;
    private String telefono;
    private String fecha;
    private float total;

    public ArrayList<Encarrec> obtenerEncarrecs() {
        return isTarget ? encarrecs : null;
    }

    public void setTargetName(String nom){
        if(!Utilitats.valorAdecuadoNom(nom)){
            throw new IllegalArgumentException("Se debe introducir un nombre");
        }
        this.targetName = nom;
    }

    @Override
    public void startElement(String __, String ___, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;
        if (qName.equalsIgnoreCase("encarrec")) {
            id = Integer.parseInt(attributes.getValue("id"));
            encarrecs = new ArrayList<Encarrec>();
            articles = new ArrayList<Article>();
        } else if (qName.equalsIgnoreCase("article")) {
            actualArticle = new Article("", 0, "", 0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length).trim();
        if (content.isEmpty()) return;

        switch (currentElement) {
            case "nombre":
                nombre = content;
                if (nombre.equalsIgnoreCase(targetName)) {
                    isTarget = true; // Encontramos el encargo objetivo
                }
                break;
            case "telefono":
                telefono = content;
                break;
            case "fecha":
                fecha = content;
                break;
            case "total":
                total = Float.parseFloat(content);
                break;
            case "nomArt":
                actualArticle.setNombre(content);
                break;
            case "cantidad":
                actualArticle.setCantidad(Float.parseFloat(content));
                break;
            case "unidad":
                actualArticle.setUnidad(content);
                break;
            case "preu":
                actualArticle.setPreu(Float.parseFloat(content));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("article")) {
            articles.add(actualArticle);
        } else if (qName.equalsIgnoreCase("encarrec")) {
            if (isTarget) {
                DateTimeFormatter formatData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate data = LocalDate.parse(fecha, formatData);
                currentEncarrec = new Encarrec(id, nombre, telefono, data, articles, total);
                encarrecs.add(currentEncarrec);
            }
        }
        currentElement = "";
    }
}
