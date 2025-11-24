# Proyecto 02: Butchery Manager

## Equipo: SQLazo

* [César Becerra Valencia (322064287)](#César)
* [Victor Abraham Sánchez Morgado (322606003)](#Victor)
* [José Luis Cortes Nava (322115437)](#Luis)

## Breve descripción:

Consiste en una consola para registro de ventas, control de inventario, manejo de impuestos y cálculo de precios de una carnicería.
Los patrones de diseño trabajados en esta práctica son: **State, Strategy, Observer, MVC, Singleton y Factory**.

## Dos maneras de compilar y ejecutar este proyecto:

### Con JDK

Prerrequisitos:

Este proyecto se puede compilar y ejecutar si se tiene instalado JDK (Java Development Kit) versión 17 o superior.

Cómo compilar y ejecutar:

Puedes compilar y ejecutar el programa directamente desde la terminal usando los comandos `javac` y `java`.

**Asegúrate de estar en el directorio raíz del proyecto (`Proyecto02_SQLazo/`) antes de ejecutar los siguientes comandos.**

1.  Compilación

El siguiente comando compilará todos los archivos `.java` que se encuentran en el directorio `src/` y dejará los archivos `.class` compilados en un nuevo directorio llamado `out/`.

```bash
javac -d out -sourcepath src src\mx\unam\ciencias\myp\butchery\Main.java
```

  * **`javac`**: Es el compilador de Java.
  * **`-d out`**: Le indica al compilador que coloque los archivos compilados (`.class`) en una carpeta llamada `out`.
  * **`src/main/java/mx/unam/ciencias/butchery/**/*.java`**: Indica la ruta donde se encuentran los archivos `.java` a compilar.

<!-- end list -->

2.  Ejecución

Una vez compilado, puedes ejecutar el programa con el siguiente comando (asumiendo que tu clase principal se llama `App` en el paquete raíz `mx.unam.ciencias.butchery`):

```bash
java -cp out mx.unam.ciencias.myp.butchery.Main
```

  * **`java`**: Es la Máquina Virtual de Java (JVM) que ejecuta el código.
  * **`-cp out`** (`-cp` es una abreviatura de `--class-path`): Le indica a la JVM que busque los archivos `.class` en el directorio `out`.
  * **`mx.unam.ciencias.butchery.Main`**: Es el nombre completamente calificado de la clase que contiene el método `main` que queremos ejecutar.

### Con Docker

Prerrequisitos:

Para ejecutar el programa de java con Docker es necesario tener instalado Docker Desktop y tener abierta la aplicación en todo momento.
Este es el link para instalarlo en Ubuntu: [https://docs.docker.com/desktop/setup/install/linux/ubuntu/](https://docs.docker.com/desktop/setup/install/linux/ubuntu/)
Este es el link para instalarlo en Windows: [https://docs.docker.com/desktop/windows/install/](https://docs.docker.com/desktop/windows/install/)
Este es el link para instalarlo en Mac: [https://docs.docker.com/desktop/mac/install/](https://docs.docker.com/desktop/mac/install/)

Cómo compilar y ejecutar:




1.  Descargar la imagen

El comando para descargar la imagen estando en el directorio raíz de la práctica es el siguiente:

```bash
docker build -t butchery .
```

  * **`docker build`**: Indica a Docker que debe construir una imagen en base al `Dockerfile` que se encuentra en la raíz de la práctica.
  * **`-t butchery`**: Etiqueta la imagen con el nombre `pumabank` para no tener que usar un ID predeterminado.
  * **`.`**: Indica a Docker que los archivos a copiar y el `Dockerfile` se encuentran en el directorio actual.

<!-- end list -->

2.  Ejecutar el contenedor

Este comando ejecuta un contenedor basado en la imagen que construimos en el paso anterior:

```bash
docker run --rm -it butchery
```

  * **`docker run`**: Da la instrucción a Docker de crear y ejecutar un contenedor.
  * **`bitchery`**: El nombre de la imagen en la que se basa el contenedor.
  * **`--rm`**: Borra el contenedor al terminar de ejecutarlo.
  * **`-it`**: Indica que el contenedor debe ser interactivo para que podamos interactuar con el programa en tiempo real.

### Con Maven

Prerrequisitos:

Este proyecto también se puede gestionar con Apache Maven. Debes tener **Maven instalado** en tu sistema para usar estos comandos.

Cómo limpiar y probar el proyecto:

Puedes usar Maven para limpiar los archivos compilados (`.class`) y para ejecutar la suite de pruebas automatizadas.

**Asegúrate de estar en el directorio raíz del proyecto (`Proyecto02_SQLazo/`) donde se encuentra el archivo `pom.xml`.**

1.   Limpiar el proyecto

El siguiente comando eliminará el directorio `target/` (donde Maven guarda los archivos compilados y los artefactos).

```bash
mvn clean
```

2.   Ejecutar las pruebas

Este comando compilará el código y ejecutará todas las pruebas unitarias que se encuentran en `src/test/java/`.

```bash
mvn test
```

## Problemática a resolver:

El dueño de una carnicería considera que sus empleados necesitan llevar un registro de las siguientes cosas:

* Tipo de carne comprada por el consumidor (por kilo o por pieza)
* Descuentos aplicados en ventas realizadas
* Las órdenes de productos que un cliente compraría y el estado en que se encuentra esta orden (cancelada, pendiente o pagada)
* Llevar un control del inventario disponible

Sin embargo, sin un sistema que automatice estas acciones podrían haber errores de cálculo y de existencia en el inventario, por lo que nos solicita hacer un sistema que lleve el control de los puntos anteriores. Además, nos solicita que el sistema tenga una estructura MVC y al menos cuatro patrones de diseño para que el código sea mantenible y extensible.

El sistema debe permitir las siguientes acciones:

* Registrar productos en el inventario: tipo de producto, unidad (kilo o pieza), cantidad
* Actualizar el inventario automáticamente
* Proporcionar el costo de un producto a partir de su costo base y su estrategia de descuento
* Vender productos en base a las estrategias de descuento configurables
* Notificar de cambios en el inventario y ventas a observadores

La implementación de esta problemática debe estar orientada a los trabajadores de la carnicería, no a los clientes.

La interfaz del sistema debe proveer al trabajador de la carnicería un menú con opciones como las que se muestran a continuación:

### Menú principal

1. Registrar productos en el inventario
2. Ver inventario
3. Calcular precio
4. Registrar venta (elegir estrategia de descuento)
5. Ver total de ventas
6. Salir

También, el dueño de la carnicería solicita que el sistema utilice persistencia de datos para que no se tenga que rellenar el inventario de nuevo cada vez que se vuelva a utilizar el programa, por lo que pide que se utilicen bases de datos para guardar los productos en el inventario. Por último, se pide que todos los cambios realizados en el inventario queden descritos en un archivo txt.

## Patrones de diseño a utilizar:

### MVC

Utilizamos MVC para separar la lógica del negocio y la presentación, lo cual nos permitiría cambiar de interfaz sin tener que cambiar el modelo (aumenta la extensibilidad de nuestro código). Los menús de consola y las validaciones de entradas no afectan la lógica interna de nuestro negocio gracias a este patrón. Los casos de uso como consultar el inventario o la venta de un producto se ven manejados por el apartado de Model. ConsoleView no conoce Inventory ni Sale, solo llama al Controller. ButcheryController solo usa el modelo y nunca habla directo con la vista.

Las clases relacionadas con este patrón son: ConsoleView, ButcheryController y ModelFacade.

### Strategy

Lo utilizamos para intercambiar modelos de descuento en tiempo de ejecución para no tener la necesidad de escribir una cadena de if-else gigante y poder agregar más estrategias de descuento si es necesario. Se utiliza la interfaz IDiscountStrategy y las clases concretas NoDiscount, FrequentCustomerDiscount y PercentageDiscount.

* NoDiscount: se utiliza si el producto a vender no tiene ningún tipo de descuento aplicable
* FrequentCustomerDiscount: a aquellos clientes frecuentes se les hace un tipo de descuento especial
* PercentageDiscount: se aplica si el producto a vender tiene un descuento fijo

Este patrón nos permite modificar el precio de un producto en proceso de ser vendido dependiendo de si el cliente gusta utilizar un descuento.

Las clases relacionadas con este patrón son: NoDiscount, FrequentCustomerDiscount y PercentageDiscount.

### Factory

Su propósito es encapsular la lógica de construcción de productos por peso o por unidad. Así el Controller no necesita conocer detalles de cada subtipo de producto. Gracias a este patrón podemos construir cualquier cantidad de productos que deseemos con una forma de medida dinámica, es decir, se pueden medir con respecto a su peso, por unidad, o se puede extender el código a que se utilice otro tipo de medida.

Las clases relacionadas con este patrón son: ProductFactory, ProductByWeight y ProductByUnit.

### Singleton

La razón de utilizar el patrón Singleton es que solamente se pueda tener una única instancia del inventario durante la ejecución. Evita inventarios paralelos y estados inconsistentes cuando varias partes del sistema lo consultan. Tiene un constructor privado y un método getInstance() público que nos garantiza una única instancia del inventario.

**Integración con la base de datos SQLite:**

Este patrón es fundamental para gestionar la persistencia de datos con SQLite. La clase `Inventory`, al ser un Singleton, se convierte en el **único punto de control** tanto para el estado del inventario en memoria como para su representación en la base de datos.

*   **Carga Inicial**: Cuando la aplicación arranca y se solicita la instancia de `Inventory` por primera vez a través de `getInstance()`, esta se encarga de cargar todos los productos desde la base de datos SQLite a la memoria. Esto asegura que el estado de la aplicación refleje los datos guardados de sesiones anteriores.
*   **Consistencia de Datos**: Cualquier modificación en el inventario (agregar un producto, actualizar el stock, cambiar un precio) se realiza a través de la única instancia de `Inventory`. Esta clase se responsabiliza de ejecutar la operación correspondiente (INSERT, UPDATE, DELETE) en la base de datos, garantizando que el estado en memoria y el estado persistente estén siempre sincronizados.
*   **Ventajas de SQLite**: Se eligió SQLite por ser una base de datos ligera, sin servidor y basada en un único archivo (`butchery.db`). Esto simplifica enormemente la configuración y el despliegue de la aplicación, ya que no requiere un servicio de base de datos externo y es ideal para aplicaciones de escritorio como esta. El Singleton `Inventory` encapsula toda la interacción con este archivo, ocultando los detalles de la base de datos del resto de la aplicación.

Las clases relacionadas con este patrón son: `Inventory`.

### Observer

El objetivo de usar Observer es notificar a aquellos interesados cuando cambie el inventario, sin tener que acoplar la clase Inventory a salidas concretas. Gracias a este patrón de diseño podemos hacer que se guarden en un archivo los cambios realizados en el inventario y que a la vez se impriman en consola.

Las clases relacionadas con este patrón son: FileNotifier, ListNotifier y ConsoleNotifier.

### State

Utilizamos el patrón State en especial para evitar usos incorrectos del sistema. Con esto evitamos condicionales y acciones como intentar cancelar una venta después de que haya sido pagada, lo cual no debería suceder. Cada estado tiene una cantidad fija de acciones que se pueden cumplir en ese instante, el resto regresan mensajes que indican que no se puede realizar esa acción concreta en ese estado. La importancia de este patrón está en que únicamente se puede cambiar internamente el estado del objeto en tiempo de ejecución (no se puede escoger en qué estado se encuentra); a diferencia del patrón Strategy, con el que buscamos que se pueda escoger el tipo de descuento a aplicar.

Las clases relacionadas con este patrón son: PendingState, PaidState y CanceledState.
