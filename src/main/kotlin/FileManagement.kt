import java.io.File

/**
 * Implementación concreta de la interfaz IFiles para manejar ficheros de texto.
 * Provee funcionalidades para operar con ficheros y directorios, específicamente para el formato de texto.
 */
class FileManagement : IFiles {

    /**
     * Verifica si un directorio existe en la ruta especificada.
     *
     * @param ruta La ruta del directorio a verificar.
     * @return La cadena vacía si el directorio existe, el mensaje de error en caso contrario.
     */
    override fun existeDir(ruta: String): String {
        try {
            if (File(ruta).isDirectory) {
                return ""
            }
        } catch (e: SecurityException) {
            return "Error al comprobar si existe el directorio $ruta: ${e.message}"
        }
        return "No existe el fichero"
    }

    /**
     * Verifica si un archivo existe en la ruta especificada.
     *
     * @param ruta La ruta del archivo a verificar.
     * @return La cadena vacía si el archivo existe, el mensaje de error en caso contrario.
     */
    override fun existeFic(ruta: String): String {
        try {
            if (File(ruta).isFile) {
                return ""
            }
        } catch (e: SecurityException) {
            return "Error al comprobar si existe el fichero $ruta: ${e.message}"
        }
        return "No existe el fichero"
    }

    /**
     * Escribe información en un fichero especificado. Si el fichero no existe, no se crea automáticamente.
     *
     * @param fichero El fichero en el que se desea escribir.
     * @param info La información a escribir en el fichero.
     * @return La cadena vacía si la escritura fue exitosa, el mensaje de error en caso contrario.
     */
    override fun escribir(fichero: File, info: String): String {
        try {
            fichero.appendText(info)
        } catch (e: Exception) {
            return "Error al escribir en el archivo: ${e.message}"
        }
        return ""
    }

    /**
     * Lee el contenido de un fichero y retorna una lista de strings, donde cada elemento representa una línea del fichero.
     *
     * @param fichero El fichero a leer.
     * @return Lista de strings con el contenido del fichero, o null si hubo un error al leer.
     */
    override fun leer(fichero: File): List<String>? {
        val lista : List<String>
        try {
            lista = fichero.readLines()
        } catch (e: Exception) {
            return null
        }
        return lista
    }

    /**
     * Crea un directorio en la ruta especificada.
     *
     * @param ruta La ruta donde se desea crear el directorio.
     * @return La cadena vacía si la creación fue exitosa, el mensaje de error si el directorio ya existe o si la creación falló.
     */
    override fun crearDir(ruta: String): String {
        val dirRuta = File(ruta)
        try {
            if (!dirRuta.isDirectory) {
                if (!dirRuta.mkdirs()) {
                    return "No fue posible crear la ruta de directorios"
                }
            }
        } catch (e: Exception) {
            return "Error al crear el directorio $ruta: ${e.message}"
        }
        return ""
    }

    /**
     * Crea un fichero en la ruta especificada, con la posibilidad de incluir información inicial y de sobreescribir un fichero existente.
     *
     * @param ruta La ruta donde se desea crear el fichero.
     * @param info Información inicial para escribir en el fichero al crearlo. Por defecto está vacío.
     * @param sobreescribir Si es verdadero, sobrescribe el fichero si ya existe; de lo contrario, no modifica el fichero existente.
     * @return El fichero creado, o null si la creación falló o si el fichero ya existe y no se eligió sobreescribir.
     */
    override fun crearFic(ruta: String, info: String, sobreescribir: Boolean): File? {
        val fichero = File(ruta)
        crearDir(fichero.parent)
        try {
            if (sobreescribir) {
                fichero.writeText(info)
            } else {
                fichero.createNewFile()
                if (info.isNotEmpty()) {
                    fichero.appendText(info)
                }
            }
        } catch (e: Exception) {
            return null
        }
        return fichero
    }

    /**
     * Busca en el directorio proporcionado el archivo más recientemente modificado cuyo nombre
     * comienza con el prefijo especificado.
     *
     * @param directorio El directorio [File] donde se realizará la búsqueda de archivos.
     * @param nombreFicheroInicio El prefijo del nombre del archivo a buscar.
     * @return El [File] del archivo más recientemente modificado que coincide con el criterio,
     *         o `null` si no se encuentra ningún archivo que cumpla con la condición.
     */
    override fun buscarUltimoFicheroEmpiezaPor(directorio: File, nombreFicheroInicio: String): File? {
        val ficheros = directorio.listFiles { fichero ->
            fichero.isFile && fichero.name.startsWith(nombreFicheroInicio)
        }

        if (ficheros != null && ficheros.isNotEmpty()) {
            val ultimoModificado = ficheros.maxByOrNull { it.lastModified() }
            if (ultimoModificado != null) {
                return ultimoModificado
            }
        }

        return null
    }

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
    override fun buscarUltimoFicheroEmpiezaFinalizaPor(directorio: File, empiezaPor: String, terminaPor: String): File? {
        val ficheros = directorio.listFiles { fichero ->
            fichero.isFile && fichero.name.startsWith(empiezaPor) && fichero.name.endsWith(terminaPor)
        }

        if (ficheros != null && ficheros.isNotEmpty()) {
            val ultimoModificado = ficheros.maxByOrNull { it.lastModified() }
            if (ultimoModificado != null) {
                return ultimoModificado
            }
        }

        return null
    }

    override fun buscarInfoFichero(fichero: File, info: String) : String? {
        val lineas = leer(fichero)
        if (lineas != null) {
            for (linea in lineas) {
                if (linea == info.dropLast(1)) {
                    return ""
                }
            }
        }
        return null
    }

}