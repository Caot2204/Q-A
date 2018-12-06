/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.mapeoclienteservidor;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mx.fei.qa.accesoadatos.controller.CuestionarioJpaController;
import mx.fei.qa.accesoadatos.controller.UsuarioJpaController;
import mx.fei.qa.accesoadatos.entity.Cuestionario;
import mx.fei.qa.accesoadatos.entity.Pregunta;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;

/**
 * Clase encargada de convertir un Cuestionario que viene del cliente a una entidad
 * Cuestionario que puede ser almacenada en la base de datos
 *
 * @version 1.0 26 Oct 2018
 * @author Carlos Onorio
 */
public class MapeadorCuestionario {
    
    /**
     * Notifica que es una clase de utilidades y no puede ser instanciada.
     */
    private MapeadorCuestionario() {
        throw new IllegalStateException("Clase de utilidades para cuestionario");
    }
    
    /**
     * Convierte un CuestionarioCliente proveniente del cliente a una entidad Respuesta
     * en el servidor
     * @param cuestionarioCliente Cuestionario que proviene desde el cliente
     * @return Entidad JPA Cuestionario
     */
    public static Cuestionario mapearCuestionario(CuestionarioCliente cuestionarioCliente) {
        Cuestionario cuestionarioEntity = new Cuestionario();
        EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        UsuarioJpaController controladorUsuario = new UsuarioJpaController(fabricaEntityManager);
        
        cuestionarioEntity.setNombre(cuestionarioCliente.getNombre());
        cuestionarioEntity.setVecesJugado(cuestionarioCliente.getVecesJugado());
        cuestionarioEntity.setUltimoGanador(cuestionarioCliente.getUltimoGanador());
        cuestionarioEntity.setAutor(controladorUsuario.findUsuario(cuestionarioCliente.getAutor()));
        
        return cuestionarioEntity;
    }
    
    /**
     * Genera un CuestionarioCliente con los datos de las entidades JPA Cuestionario, Pregunta
     * y Respuesta recuperadas de la base de datos
     * @param controladorCuestionario CuestionarioJpaController para recuperar información de la base de datos
     * @param cuestionario Entidad JPA Cuestionario a mapear
     * @return CuestionarioCliente compatible con el cliente del servidor
     */
    public static CuestionarioCliente mapearACuestionarioCliente(CuestionarioJpaController controladorCuestionario, Cuestionario cuestionario) {
        CuestionarioCliente cuestionarioCliente = new CuestionarioCliente();
        long idCuestionario = cuestionario.getId();
        
        cuestionarioCliente.setNombre(cuestionario.getNombre());
        cuestionarioCliente.setVecesJugado(cuestionario.getVecesJugado());
        cuestionarioCliente.setUltimoGanador(cuestionario.getUltimoGanador());
        cuestionarioCliente.setAutor(cuestionario.getAutor().getNombre());
        
        List<Pregunta> preguntasEntity = controladorCuestionario.getControladorPregunta().getPreguntasDeCuestionario(idCuestionario);
        cuestionarioCliente.setPreguntas(MapeadorPregunta.mapearAPreguntasCliente(idCuestionario, 
                                         preguntasEntity, controladorCuestionario.getControladorRespuesta()));
        
        return cuestionarioCliente;
    }
    
}
