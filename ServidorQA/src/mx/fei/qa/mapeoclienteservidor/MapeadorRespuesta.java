/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.mapeoclienteservidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.qa.accesoadatos.entity.Respuesta;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.utileria.UtileriaCadena;

/**
 * Clase encargada de convertir una Respuesta que viene del cliente a una
 * entidad Respuesta que puede ser almacenada en la base de datos
 *
 * @version 1.0 26 Oct 2018
 * @author Carlos Onorio
 */
public class MapeadorRespuesta {

    /**
     * Notifica que es una clase de utilidades y no puede ser instanciada.
     */
    private MapeadorRespuesta() {
        throw new IllegalStateException("Clase de utilidades para respuesta");
    }
    
    /**
     * Convierte un objeto RespuestaCliente proveniente del cliente a una
     * entidad Respuesta en el servidor
     *
     * @param idCuestionario Identificador del Cuestionario al cual pertenece la
     * respuesta
     * @param numeroPregunta Número de pregunta a la cual está asociada la
     * respuesta
     * @param respuestaCliente Respuesta a una pregunta proveniente desde el
     * cliente
     * @return Entidad JPA Respuesta
     * @throws IOException Lanzada si ocurre un error al leer los bytes de la
     * imágen
     */
    public static Respuesta mapearRespuesta(long idCuestionario, int numeroPregunta, RespuestaCliente respuestaCliente) throws IOException {
        Respuesta respuestaEntity = new Respuesta(idCuestionario, numeroPregunta, respuestaCliente.getLetra());

        if (respuestaCliente.getDescripcion() != null && UtileriaCadena.validarCadena(respuestaCliente.getDescripcion(), 1, 300)) {
            respuestaEntity.setDescripcion(respuestaCliente.getDescripcion());
        }

        if (respuestaCliente.getImagen() != null) {
            respuestaEntity.setImagen(respuestaCliente.getImagen());
        }

        respuestaEntity.setCorrecta(respuestaCliente.isEsCorrecta());

        return respuestaEntity;
    }

    /**
     * Convierte un conjunto de Respuestas provenientes del cliente a un
     * conjunto de entidades Respuesta que pueden ser almacenadas en la base de
     * datos
     *
     * @param idCuestionario Identificador del Cuestionario al cual pertenece la
     * respuesta
     * @param numeroPregunta Número de pregunta a la cual está asociada la
     * respuesta
     * @param respuestasDePregunta Respuestas de una Pregunta
     * @return Lista de Entidades JPA Respuesta
     */
    public static List<Respuesta> mapearRespuestasDePregunta(long idCuestionario, int numeroPregunta, List<RespuestaCliente> respuestasDePregunta) {
        List<Respuesta> respuestasEntity = new ArrayList<>();

        respuestasDePregunta.forEach(respuestaCliente -> {
            try {
                respuestasEntity.add(mapearRespuesta(idCuestionario, numeroPregunta, respuestaCliente));
            } catch (IOException ex) {
                Logger.getLogger(MapeadorRespuesta.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        return respuestasEntity;
    }

    /**
     * Convierte un conjunto de entidades JPA Respuesta a un conjunto de
     * Respuestas compatibles para el cliente
     *
     * @param respuestasEntity Lista de Entidades JPA Respuesta recuperadas de
     * la base de datos
     * @return Lista de RespuestaCliente
     */
    public static List<RespuestaCliente> mapearARespuestasCliente(List<Respuesta> respuestasEntity) {
        ArrayList<RespuestaCliente> respuestasCliente = new ArrayList<>();

        for (Respuesta respuestaEntity : respuestasEntity) {
            RespuestaCliente respuestaCliente = new RespuestaCliente();
            respuestaCliente.setLetra(respuestaEntity.getRespuestaPK().getLetra());
            respuestaCliente.setDescripcion(respuestaEntity.getDescripcion());
            respuestaCliente.setImagen(respuestaEntity.getImagen());
            respuestaCliente.setEsCorrecta(respuestaEntity.getCorrecta());
            respuestasCliente.add(respuestaCliente);
        }

        return respuestasCliente;
    }

}
