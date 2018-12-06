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
import mx.fei.qa.accesoadatos.controller.RespuestaJpaController;
import mx.fei.qa.accesoadatos.entity.Pregunta;
import mx.fei.qa.accesoadatos.entity.Respuesta;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.utileria.UtileriaCadena;

/**
 * Clase encargada de convertir una Pregunta que viene del cliente a una entidad
 * Pregunta que puede ser almacenada en la base de datos
 *
 * @version 1.0 26 Oct 2018
 * @author Carlos Onorio
 */
public class MapeadorPregunta {

    /**
     * Notifica que es una clase de utilidades y no puede ser instanciada.
     */
    private MapeadorPregunta() {
        throw new IllegalStateException("Clase de utilidades para pregunta");
    }

    /**
     * Convierte una PreguntaCliente proveniente del cliente a una entidad JPA
     * Respuesta en el servidor
     *
     * @param idCuestionario Identificador del Cuestionario al cual pertenece la
     * respuesta
     * @param preguntaCliente PreguntaCliente de un CuestionarioCliente
     * proveniente desde el cliente
     * @return Entidad JPA Pregunta
     * @throws IOException Lanzada si ocurre un error al leer los bytes de la
     * imágen
     */
    public static Pregunta mapearPregunta(long idCuestionario, PreguntaCliente preguntaCliente) throws IOException {
        Pregunta preguntaEntity = new Pregunta(idCuestionario, preguntaCliente.getNumero());

        if (preguntaCliente.getDescripcion() != null && UtileriaCadena.validarCadena(preguntaCliente.getDescripcion(), 1, 300)) {
            preguntaEntity.setDescripcion(preguntaCliente.getDescripcion());
        }

        if (preguntaCliente.getImagen() != null) {
            preguntaEntity.setImagen(preguntaCliente.getImagen());
        }

        return preguntaEntity;
    }

    /**
     * Convierte un conjunto de PreguntaCliente provenientes del cliente a un
     * conjunto de entidades JPA Pregunta que pueden ser almacenadas en la base
     * de datos
     *
     * @param idCuestionario Identificador del Cuestionario al cual pertenece la
     * respuesta
     * @param preguntasDeCuestionario Lista de PreguntaCliente de un
     * Cuestionario
     * @return Lista de Entidades JPA Pregunta
     */
    public static List<Pregunta> mapearPreguntas(long idCuestionario, List<PreguntaCliente> preguntasDeCuestionario) {
        List<Pregunta> preguntasEntity = new ArrayList<>();
        for (PreguntaCliente preguntaCliente : preguntasDeCuestionario) {
            try {
                preguntasEntity.add(mapearPregunta(idCuestionario, preguntaCliente));
            } catch (IOException ex) {
                Logger.getLogger(MapeadorPregunta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return preguntasEntity;
    }

    /**
     * Convierte un conjunto de Entidades JPA Pregunta a un conjunto de
     * PreguntaCliente compatibles con el cliente del servidor
     *
     * @param idCuestionario Identificador del Cuestionario al cual pertenece la
     * respuesta
     * @param preguntasEntity Lista de Entidades JPA Pregunta del Cuestionario a
     * mapear
     * @param controladorRespuesta RespuestaJpaController para la lectura de
     * Respuestas desde la base de datos
     * @return Lista de PreguntaCliente
     */
    public static List<PreguntaCliente> mapearAPreguntasCliente(long idCuestionario, List<Pregunta> preguntasEntity, RespuestaJpaController controladorRespuesta) {
        List<PreguntaCliente> preguntasCliente = new ArrayList<>();

        for (Pregunta preguntaEntity : preguntasEntity) {
            PreguntaCliente preguntaCliente = new PreguntaCliente();
            preguntaCliente.setNumero(preguntaEntity.getPreguntaPK().getNumero());
            preguntaCliente.setDescripcion(preguntaEntity.getDescripcion());
            preguntaCliente.setImagen(preguntaEntity.getImagen());

            List<Respuesta> respuestasEntity = controladorRespuesta.getRespuestasDePregunta(idCuestionario, preguntaEntity.getPreguntaPK().getNumero());
            preguntaCliente.setRespuestas(MapeadorRespuesta.mapearARespuestasCliente(respuestasEntity));
            preguntasCliente.add(preguntaCliente);
        }

        return preguntasCliente;
    }

}
