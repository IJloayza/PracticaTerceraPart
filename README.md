# AppEncarrecs - Sistema de Gestión de Pedidos

AppEncarrecs es una aplicación de consola en Java para gestionar pedidos en una tienda, permitiendo al usuario agregar, mostrar y editar encargos de productos. La aplicación permite al usuario interactuar mediante comandos sencillos, almacenar los pedidos en archivos XML, y poderlos leer.

## Ejecución

javac AppEncarrecs/Interface.java
java AppEncarrecs.Interface

## Flujo de la aplicación

En la aplicación existen 5 posibles acciones:

Help:
    Despliega un listado de ayuda con todas las acciones realizadas por el usuario.
Agregar:
    Realiza el respectivo formulario para crear un archivo XML
Show:
    Pregunta el tipo de encargo que deseas leer y muestra por pantalla el contenido de aquel encargo.
Finalizar:
    Una vez realizadas las acciones que querías se puede usar esta acción para salir de la aplicación.

Todas estas acciones tienen la constante c o Cancelar en todos los puntos del programa que requieren un input para poder salir libremente del flujo del programa al paso anterior.

## Actualizaciones

En esta nueva actualización de nuestro anterior código generaremos los archivos en formato XML usando DOM y también podremos hacer la lectura de estos utilizando DOM y SAX.

Se han añadido los siguientes métodos en la clase Fichero:

### crearXML(List<Encarrec> listaEncarrecs):

Este método usa el DocumentBuilderFactory para crear el documento de los encargos ordenados alfabéticamente en formato XML. Recorre la lista de encargos y va montando el XML, la raíz será el encargo que tendrá como atributo el id y contendrá como hijos los valores del encargo, nombre, teléfono, fecha y total. 

Dentro de cada encargo añadiremos el nodo artículo y este contendrá su información básica: el nombre, la cantidad, el tipo de unidad y su precio.

### LlegirDOMXML(int pos):

Este método permite leer un archivo XML utilizando el DocumentBuilderFactory con DOM. Se accede al archivo XML seleccionado por el usuario. Una vez se obtiene el documento XML, se accede al nodo raíz encarrecs. Luego, recorre cada nodo encarrec para leer la información del encargo. 

Dentro de cada encargo, también recorre los nodos article para obtener la información de cada artículo. 

Finalmente, muestra toda esta información de manera estructurada en la consola.

### printEncarrecs(ArrayList<Encarrecs>):
Este método esta destinado a formatear correctamente los Encarrecs añadidos y realizar un print en la pantalla legible por el usuario.

## Clases

### Article

En la clase Article al igual que en la anterior práctica se genera un constructor con sus respectivos métodos getters y setters, que llegan a generar un Article, el cual tiene un nombre(String), cantidad(Float), unidad(Unitat), preu(Float).

### Encarrec

Esta clase destinada a generar encarrecs tiene sus respectivos getters y setters junto con dos constructores, uno que es utilizado por el programa al momento de crear el fichero con los encargosya que designa el id y preu total sin necesidad de interacción con el usuario, en cambio el otro es utilizado para la lectura de los ficheros, ya que al momento de generar los encargos al momento de leer se crean encragos nuevos es necesario designar un id y preu total sino será generado automáticamente siendo diferente al que aparece en los archivos, entonces he decidido que es mejor generarlo con un setId. 

### Fichero

El fichero está destinado a controlar y gestionar los métodos usados sobre los File tanto escribir como leer los ficheros.

### Interface

La interface es el main y toda la interacción el usuario se encuentra en este archivo, genera un flujo por el cual pasa el usuario y desde este se definen todos los valores que contendrá, editará o leera el archivo.

### Std

Esta clase es un BufferedReader para los inputs de usuario que esta encargado de gestionar las excepciones y genera un RunTimeException para no tener que controlar de ninguna las excepciones que forma el BufferedReader.

### Unitat

Es un Enum en el cual se definen los tipos de Unitat que se puede crear todos con su respectivo String que es el que se utiliza para los prints y comparaciones, al igual que un fromString que permite crear una Unitat a partir de un String y asi crear un Article que es el que lo utiliza en su creación. 

### Utilitats

Las Utilitats son usadas como un reconocimiento de valores adecuados en distintas partes del programa tales como la fecha, los teléfonos, os nombres, por tanto los comparadores de estos se encuentran en Utilitats.

### UtilitatsConfirmacio

Este archivo está especialmente usado para verificar si la respeusta del usuario es positiva o negativa retorna un boolean y difiere de Utilitats debido a que no es una función de match por regex sino de reconocer la respuesta comparandolo con posibles equivalentes creados por mi mismo, tales como "si", "s", "yes", etc

