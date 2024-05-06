import java.io.File

/**
 * Interfaz que define operaciones de manejo de ficheros y directorios utilizados en el juego de bingo.
 * Incluye métodos para verificar existencias, escribir, leer, crear archivos y directorios.
 */
interface IFiles {

    /**
     * Verifica si un directorio existe en la ruta especificada.
     *
     * @param ruta La ruta del directorio a verificar.
     * @return Verdadero si el directorio existe, falso de lo contrario.
     */
    fun existeDir(ruta: String): String

    /**
     * Verifica si un archivo existe en la ruta especificada.
     *
     * @param ruta La ruta del archivo a verificar.
     * @return Verdadero si el archivo existe, falso de lo contrario.
     */
    fun existeFic(ruta: String): String

    /**
     * Escribe información en un fichero especificado. Si el fichero no existe, no se crea automáticamente.
     *
     * @param fichero El fichero en el que se desea escribir.
     * @param info La información a escribir en el fichero.
     * @return Verdadero si la escritura fue exitosa, falso de lo contrario.
     */
    fun escribir(fichero: File, info: String): String

    /**
     * Lee el contenido de un fichero y retorna una lista de strings, donde cada elemento representa una línea del fichero.
     *
     * @param fichero El fichero a leer.
     * @return Lista de strings con el contenido del fichero, o null si hubo un error al leer.
     */
    fun leer(fichero: File): List<String>?

    /**
     * Crea un directorio en la ruta especificada.
     *
     * @param ruta La ruta donde se desea crear el directorio.
     * @return Verdadero si la creación fue exitosa, falso si el directorio ya existe o si la creación falló.
     */
    fun crearDir(ruta: String): String

    /**
     * Crea un fichero en la ruta especificada, con la posibilidad de incluir información inicial y de sobreescribir un fichero existente.
     *
     * @param ruta La ruta donde se desea crear el fichero.
     * @param info Información inicial para escribir en el fichero al crearlo. Por defecto está vacío.
     * @param sobreescribir Si es verdadero, sobrescribe el fichero si ya existe; de lo contrario, no modifica el fichero existente.
     * @return El fichero creado, o null si la creación falló o si el fichero ya existe y no se eligió sobreescribir.
     */
    fun crearFic(ruta: String, info: String = "", sobreescribir: Boolean = true): File?

    /**
     * Busca en el directorio proporcionado el archivo más recientemente modificado cuyo nombre
     * comienza con el prefijo especificado.
     *
     * @param directorio El directorio [File] donde se realizará la búsqueda de archivos.
     * @param nombreFicheroInicio El prefijo del nombre del archivo a buscar.
     * @return El [File] del archivo más recientemente modificado que coincide con el criterio,
     *         o `null` si no se encuentra ningún archivo que cumpla con la condición.
     */
    fun buscarUltimoFicheroEmpiezaPor(directorio: File, nombreFicheroInicio: String): File?

    /**
     * Busca en el directorio proporcionado el archivo más recientemente modificado cuyo nombre
     * comienza con el prefijo y sufijo especificado.
     *
     * @param directorio El directorio [File] donde se realizará la búsqueda de archivos.
     * @param empiezaPor El prefijo del nombre del archivo a buscar.
     * @param terminaPor El sufijo del nombre del archivo a buscar.
     * @return El [File] del archivo más recientemente modificado que coincide con el criterio,
     *         o `null` si no se encuentra ningún archivo que cumpla con la condición.
     */
    fun buscarUltimoFicheroEmpiezaFinalizaPor(directorio: File, empiezaPor: String, terminaPor: String): File?

    fun buscarInfoFichero(fichero: File, info: String) : String?
}