/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeoclienteservidor;

import accesoadatos.entity.Cuestionario;
import dominio.cuestionario.CuestionarioCliente;

/**
 * Convierte un objeto CuestionarioCliente proveniente desde el cliente a un entidad Cuestionario
 * en el servidor
 * 
 * @version 1.0 23 Oct 2018
 * @author Carlos Onorio
 */
public class MapeadorCuestionario {
    
    public static Cuestionario mapearCuestionario(CuestionarioCliente cuestionarioCliente) {
        Cuestionario cuestionarioEntity = new Cuestionario();
        
        
        
        return cuestionarioEntity;
    }
    
    /**
     * Valida que una cadena no sea nula, no esté vacía y no sobrepase la cantidad máxima de 
     * caracteres validos para la cadena
     * 
     * @param cadena Cadena de caracteres a validar
     * @param longitudMaxima Longitud máxima de caracteres permitida
     * @return True si la cadena es valida, false si no lo es
     */
    private boolean validarCadena(String cadena, int longitudMaxima) {
        boolean camposValidos = false;
        if (cadena != null && !cadena.isEmpty() && cadena.length() <= longitudMaxima) {
            camposValidos = true;
        }
        return camposValidos;
    }
    
}
