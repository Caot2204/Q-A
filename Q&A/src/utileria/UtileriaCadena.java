/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utileria;

/**
 * Proporciona un conjunto de métodos comúnes para utilizar en cadenas
 * 
 * @version 1.0 26 Oct 2018
 * @author Carlos Onorio
 */
public class UtileriaCadena {

    /**
     * Valida que una cadena no sea nula, no esté vacía y este dentro del rango
     * de caracteres validos para la cadena
     *
     * @param cadena Cadena de caracteres a validar
     * @param longitudMinima Longitud mínima de caracteres
     * @param longitudMaxima Longitud máxima de caracteres permitida
     * @return True si la cadena es valida, false si no lo es
     */
    public static boolean validarCadena(String cadena, int longitudMinima, int longitudMaxima) throws IllegalArgumentException {
        boolean camposValidos = false;
        if (cadena != null && !cadena.isEmpty()) {
            if (cadena.length() <= longitudMaxima && cadena.length() > longitudMinima) {
                String primerCaracter = String.valueOf(cadena.charAt(0));
                if (!primerCaracter.equals(" ")) {
                    camposValidos = true;
                } else {
                    throw new IllegalArgumentException("key.noEmpezarConEspacio");
                }
            } else {
                throw new IllegalArgumentException("Longitud invalida");
            }
        } else {
            throw new IllegalArgumentException("key.noDejarVacio");
        }
        return camposValidos;
    }

}
