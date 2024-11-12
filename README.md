# AppEncarrecs - Sistema de Gestión de Pedidos

AppEncarrecs es una aplicación de consola en Java para gestionar pedidos en una tienda, permitiendo al usuario agregar, mostrar y editar encargos de productos. La aplicación permite al usuario interactuar mediante comandos sencillos, almacenar los pedidos en archivos de forma serializada o aleatoria, y gestionar información como fechas de entrega, artículos y contactos.

## Ejecución

javac AppEncarrecs/Interface.java
java AppEncarrecs.Interface

## Flujo de la aplicación

En la aplicación existen 5 posibles acciones:

Help:
    Despliega un listado de ayuda con todas las acciones realizadas por el usuario.
Agregar:
    Realiza el respectivo formulario para crear un archivo ya sea de tipo Serializado o Aleatorio.
Show:
    Pregunta el tipo de encargo que deseas leer y muestra por pantalla el contenido de aquel encargo.
Edit:
    Pregunta el encargo y dentro de este el id que deseas cambiar ya sea por el teléfono o por la fecha acto seguido muestra el nuevo archivo con sus cambios.
Finalizar:
    Una vez realizadas las acciones que querías se puede usar esta acción para salir de la aplicación.

Todas estas acciones tienen la constante c o Cancelar en todos los puntos del programa que requieren un input para poder salir libremente del flujo del programa al paso anterior.

## Actualizaciones

En esta nueva actualización de nuestro anterior código ya no utilizamos datos mediante csv o binario sino nos centramos en la creación y lectura de archivos Serializados y Aleatorios.

He decidido también utilizar las carpetas Serializados y Aleatorios que crean respectivamente cada archivo en su carpeta ahorrando la búsqueda y mejor distribución de archivos, estos se crean en el lugar donde es ejecutada la aplicación y acceden a los archivos solo de los que se encuentran dentro de Aleatorios o Serializados, esto ofrece una libertad asi mismo de crear listados de elección y seguridad al momento de la lectura de los archivos, que es lo que utilizao para ofrecer algunas opciones en mi aplicación.

Junto con esto he pensado y creado los métodos de Fichero:

### ficSerialitzat(ArrayList<Encarrec>):
 Este método recoge la lista de encargos que ha realizado el cliente y procede a crear un archivo cuyo nombre es la fecha y hora del momento en el que se hizo el archivo, este implementa las clases ObjectOutputStream y FileOutputStram para escribir dentro de un archivo toda la lista de ENcarrecs usando writeObject como único método y única línea necesaria para la escritura del mismo.

### llegirSerialitzat(File ruta):
 Para leer un archivo serializado se pide anteriormente al usuario que elija entre un listado de la carpeta Serializados uno de los archivos, este archivo elegido es pasado a este método y realiza un casting sobre todo el objeto recuperado por ObjectInputStream y muestra un print legible por pantalla del ArrayList de los Encarrecs recuperados.

### ficAleatori(ArrayList<Encarrec>):
 Este método utiliza RandomAccesFile como el escritor de archivo el cual difiere del otro por su mayor complejidad al momento de realizar la escritura de un archivo al necesitar medidas fijas para los archivos debido a que RandomAccesFile también permite una gran libertad para editar los datos o saltarlos, por lo cual si todo tiene una medida el salto entre muchos datos es posible lo que lo vuelve en ese sentido superior a Serializados, por ello también en este archivo aleatorio realizo la escritura de todos los datos usando String.format que me permite rellenar los Strings que escribo junto espacios y no valor nulos \0 que dificultan la interpretación de los datos.

### llegirAleatori(File ruta):
 En este caso realizo saltos utilizando el método seek y así saltar de encargo en encargo, una vez leído el encargo altero la medida de pos que se refiere a la posición en la que quiero mover el puntero y continúo leyendo hasta que el archivo acabe por cada Encarrec leído lo añado aun ArrayList y al finalizar todo el archivo hago un print de todo el ArrayList.

### editarAleatori(File ruta, int idBuscado, String datoQueSeDeseaCambiar, String Cambio):
 Anteriormente se habrá pedido la ruta desde un listado que se muestra al usuario, se le mostrará por pantalla todo le documento y se le preguntará el id de encargo que desea cambiar y por cual valor desea cambiarlo, en este caso utilizo primero la lectura de id para ubicarme en un encargo en específico si el id no es igual al id que el usuario busca entonces realizao un salto de bytes al siguiente encargo y vuelvo a comparar el id, si no se encuentra será liberado un EndOfFileException el cual gestiono para avisar al uuario que ha introducido un id inexistente, si encuentra el id realizo los cambios sobre los bytes que se desea cambiar y imprimo el documenot una vez cambiado para mostrar los cambios generados.

### leerString(RandomAccesFile archivoAleatorio, int longitud):
 Ya que en un RandomAccesFile lee los String mediante readChar y yo conozco desde el principio la longitud de los mismos, leo cada char y la introduzco dentro de un StringBuilder y retorno un String con los espacios eliminados.

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