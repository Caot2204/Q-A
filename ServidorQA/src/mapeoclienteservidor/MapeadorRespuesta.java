/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeoclienteservidor;

import accesoadatos.entity.Respuesta;
import dominio.cuestionario.RespuestaCliente;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author Carlos Onorio
 */
public class MapeadorRespuesta {
    
    /**
     * Convierte un objeto RespuestaCliente proveniente del cliente a una entidad Respuesta
     * en el servidor
     * 
     * @param idCuestionario Identificador del Cuestionario al cual pertenece la respuesta
     * @param numeroPregunta Número de pregunta a la cual está asociada la respuesta
     * @param respuestaCliente Respuesta a una pregunta proveniente desde el cliente
     * @return Entidad JPA Respuesta 
     * @throws IOException Lanzada si ocurre un error al leer los bytes de la imágen 
     */
    public static Respuesta mapearRespuesta(long idCuestionario, int numeroPregunta, RespuestaCliente respuestaCliente) throws IOException {
        Respuesta respuestaEntity = new Respuesta();
        
        respuestaEntity.getRespuestaPK().setIdCuestionario(idCuestionario);
        respuestaEntity.getRespuestaPK().setNumeroPregunta(numeroPregunta);
        respuestaEntity.getRespuestaPK().setLetra(respuestaCliente.getLetra());
        
        if (validarCadena(respuestaCliente.getDescripcion(), 300)) {
            respuestaEntity.setDescripcion(respuestaCliente.getDescripcion());
        }
        
        if (respuestaCliente.getImagen() != null) {
            respuestaEntity.setImagen(Files.readAllBytes(respuestaCliente.getImagen().toPath()));
        }
        
        respuestaEntity.setCorrecta(respuestaCliente.isEsCorrecta());
        
        return respuestaEntity;
    }
    
    /**
     * Valida que una cadena no sea nula, no esté vacía y no sobrepase la cantidad máxima de 
     * caracteres validos para la cadena
     * 
     * @param cadena Cadena de caracteres a validar
     * @param longitudMaxima Longitud máxima de caracteres permitida
     * @return True si la cadena es valida, false si no lo es
     */
    private static boolean validarCadena(String cadena, int longitudMaxima) {
        boolean camposValidos = false;
        if (cadena != null && !cadena.isEmpty() && cadena.length() <= longitudMaxima) {
            camposValidos = true;
        }
        return camposValidos;
    }
    
}
