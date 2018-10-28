/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacion.servidor;

import accesoadatos.controller.CuestionarioJpaController;
import accesoadatos.controller.PreguntaJpaController;
import accesoadatos.controller.RespuestaJpaController;
import accesoadatos.controller.UsuarioJpaController;
import accesoadatos.controller.exceptions.IllegalOrphanException;
import accesoadatos.controller.exceptions.NonexistentEntityException;
import accesoadatos.entity.Cuestionario;
import accesoadatos.entity.Pregunta;
import accesoadatos.entity.Respuesta;
import accesoadatos.entity.Usuario;
import comunicacion.interfaz.CuestionarioInterface;
import dominio.cuestionario.CuestionarioCliente;
import dominio.cuestionario.PreguntaCliente;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mapeoclienteservidor.MapeadorCuestionario;
import mapeoclienteservidor.MapeadorPregunta;
import mapeoclienteservidor.MapeadorRespuesta;

/**
 *
 * @author Carlos Onorio
 */
public class ServidorCuestionario implements CuestionarioInterface {

    private CuestionarioJpaController controladorCuestionario;
    private UsuarioJpaController controladorUsuario;

    @Override
    public boolean guardarCuestionario(CuestionarioCliente cuestionario) throws RemoteException {
        Cuestionario cuestionarioEntity;
        ArrayList<Pregunta> preguntasEntity;
        ArrayList<Respuesta> respuestasEntity;
        long idCuestionario;
        boolean guardadoExitoso = false;

        cuestionarioEntity = MapeadorCuestionario.mapearCuestionario(cuestionario);
        controladorCuestionario.create(cuestionarioEntity);
        idCuestionario = cuestionarioEntity.getId();
        try {
            ArrayList<PreguntaCliente> preguntasCliente = cuestionario.getPreguntas();
            preguntasEntity = MapeadorPregunta.mapearPreguntas(idCuestionario, preguntasCliente);
            for (int a = 0; a < preguntasCliente.size(); a++) {
                PreguntaCliente preguntaActual = preguntasCliente.get(a);
                respuestasEntity = MapeadorRespuesta.mapearRespuestasDePregunta(idCuestionario, preguntaActual.getNumero(), preguntaActual.getRespuestas());
                controladorCuestionario.getControladorPregunta().create(preguntasEntity.get(a));
                for (Respuesta respuestaEntity : respuestasEntity) {
                    controladorCuestionario.getControladorRespuesta().create(respuestaEntity);
                }
            }

            guardadoExitoso = true;

        } catch (Exception ex) {
            Logger.getLogger(ServidorCuestionario.class.getName()).log(Level.SEVERE, null, ex);
            try {
                controladorCuestionario.destroy(idCuestionario);
            } catch (IllegalOrphanException | NonexistentEntityException ex1) {
                Logger.getLogger(ServidorCuestionario.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return guardadoExitoso;
    }

    /**
     * Recupera un Cuestionario de la base de datos y lo devuelve al cliente
     * 
     * @param autor Nombre de usuario que registró el cuestionario
     * @param nombreCuestionario Nombre del cuestionario a recuperar
     * @return Cuestionario compatible con el cliente del servidor
     * @throws RemoteException Lanzada si ocurre un error en la conexión cliente-servidor
     */
    @Override
    public CuestionarioCliente recuperarCuestionario(String autor, String nombreCuestionario) throws RemoteException {
        Usuario usuario = controladorUsuario.findUsuario(autor);
        Cuestionario cuestionarioEntity = controladorCuestionario.getCuestionario(usuario, nombreCuestionario);
        CuestionarioCliente cuestionarioCliente = MapeadorCuestionario.mapearACuestionarioCliente(controladorCuestionario, cuestionarioEntity);
        return cuestionarioCliente;        
    }
    
    /**
     * Recupera los Cuestionarios de un usuario y los devuelve al cliente
     * 
     * @param autor Nombre de usuario que registró los cuestionarios
     * @return Lista de Cuestionarios compatible con el cliente del servidor
     * @throws RemoteException Lanzada si ocurre un error en la conexión cliente-servidor
     */
    @Override
    public ArrayList<CuestionarioCliente> recuperarCuestionariosPorAutor(String autor) throws RemoteException {
        ArrayList<CuestionarioCliente> cuestionariosCliente = new ArrayList<>();
        Usuario usuario = controladorUsuario.findUsuario(autor);
        List<Cuestionario> cuestionariosEntity = controladorCuestionario.getCuestionariosPorAutor(usuario);
        
        for (Cuestionario cuestionarioEntity : cuestionariosEntity) {
            CuestionarioCliente cuestionarioCliente = recuperarCuestionario(autor, cuestionarioEntity.getNombre());
            cuestionariosCliente.add(cuestionarioCliente);
        }
        
        return cuestionariosCliente;
    }

    @Override
    public boolean editarCuestionario(CuestionarioCliente cuestionario) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarCuestionario(long id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ServidorCuestionario() {
        EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        controladorCuestionario = new CuestionarioJpaController(fabricaEntityManager);
        controladorUsuario = new UsuarioJpaController(fabricaEntityManager);
    }

}
