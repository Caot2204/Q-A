/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.servidor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mx.fei.qa.accesoadatos.controller.CuestionarioJpaController;
import mx.fei.qa.accesoadatos.controller.UsuarioJpaController;
import mx.fei.qa.accesoadatos.controller.exceptions.IllegalOrphanException;
import mx.fei.qa.accesoadatos.controller.exceptions.NonexistentEntityException;
import mx.fei.qa.accesoadatos.entity.Cuestionario;
import mx.fei.qa.accesoadatos.entity.Pregunta;
import mx.fei.qa.accesoadatos.entity.Respuesta;
import mx.fei.qa.accesoadatos.entity.Usuario;
import mx.fei.qa.comunicacion.interfaz.CuestionarioInterface;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.mapeoclienteservidor.MapeadorCuestionario;
import mx.fei.qa.mapeoclienteservidor.MapeadorPregunta;
import mx.fei.qa.mapeoclienteservidor.MapeadorRespuesta;

/**
 * Proceso servidor encargado de la administración de los Cuestionarios
 * registrados por los usuarios
 *
 * @version 1.0 30 Oct 2018
 * @author Carlos Onorio
 */
public class ServidorCuestionarioRMI implements CuestionarioInterface {

    private final CuestionarioJpaController controladorCuestionario;
    private final UsuarioJpaController controladorUsuario;

    /**
     * Solicita a la clase MapeadorCuestionario que convierta un
     * CuestionarioCliente a una entidad Cuestionario, con sus respectivas
     * entidades Preguntas y entidades Respuestas para ser almacenados en la
     * base de datos.
     *
     * @param cuestionario Cuestionario que se desea guardar en la base de datos
     * @return True si el cuestionario fue mapeado y almacenado con éxito, False
     * si no fué así
     * @throws RemoteException Lanzada si ocurre al problema con la conexión
     * cliente-servidor
     */
    @Override
    public boolean guardarCuestionario(CuestionarioCliente cuestionario) throws RemoteException {
        boolean guardadoExitoso = false;

        Cuestionario cuestionarioEntity = MapeadorCuestionario.mapearCuestionario(cuestionario);
        controladorCuestionario.create(cuestionarioEntity);
        Usuario autor = controladorUsuario.findUsuario(cuestionario.getAutor());

        cuestionarioEntity = controladorCuestionario.getCuestionario(autor, cuestionario.getNombre());
        long idCuestionario = cuestionarioEntity.getId();

        try {
            List<PreguntaCliente> preguntasCliente = cuestionario.getPreguntas();
            List<Pregunta> preguntasEntity = MapeadorPregunta.mapearPreguntas(idCuestionario, preguntasCliente);
            for (int a = 0; a < preguntasCliente.size(); a++) {
                PreguntaCliente preguntaActual = preguntasCliente.get(a);
                List<Respuesta> respuestasEntity = MapeadorRespuesta.mapearRespuestasDePregunta(idCuestionario, preguntaActual.getNumero(), preguntaActual.getRespuestas());
                controladorCuestionario.getControladorPregunta().create(idCuestionario, preguntasEntity.get(a));
                for (Respuesta respuestaEntity : respuestasEntity) {
                    controladorCuestionario.getControladorRespuesta().create(respuestaEntity);
                }
            }

            guardadoExitoso = true;

        } catch (Exception ex) {
            Logger.getLogger(ServidorCuestionarioRMI.class.getName()).log(Level.SEVERE, null, ex);
            try {
                controladorCuestionario.destroy(idCuestionario);
            } catch (IllegalOrphanException | NonexistentEntityException ex1) {
                Logger.getLogger(ServidorCuestionarioRMI.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return guardadoExitoso;
    }

    /**
     * Recupera un Cuestionario de la base de datos y lo devuelve al cliente.
     *
     * @param autor Nombre de usuario que registró el cuestionario
     * @param nombreCuestionario Nombre del cuestionario a recuperar
     * @return Cuestionario compatible con el cliente del servidor
     * @throws RemoteException Lanzada si ocurre un error en la conexión
     * cliente-servidor
     */
    @Override
    public CuestionarioCliente recuperarCuestionario(String autor, String nombreCuestionario) throws RemoteException {
        Usuario usuario = controladorUsuario.findUsuario(autor);
        Cuestionario cuestionarioEntity = controladorCuestionario.getCuestionario(usuario, nombreCuestionario);
        return MapeadorCuestionario.mapearACuestionarioCliente(controladorCuestionario, cuestionarioEntity);
    }

    /**
     * Recupera los Cuestionarios de un usuario y los devuelve al cliente.
     *
     * @param autor Nombre de usuario que registró los cuestionarios
     * @return Lista de Cuestionarios compatible con el cliente del servidor
     * @throws RemoteException Lanzada si ocurre un error en la conexión
     * cliente-servidor
     */
    @Override
    public List<CuestionarioCliente> recuperarCuestionariosPorAutor(String autor) throws RemoteException {
        List<CuestionarioCliente> cuestionariosCliente = new ArrayList<>();
        Usuario usuario = controladorUsuario.findUsuario(autor);
        List<Cuestionario> cuestionariosEntity = controladorCuestionario.getCuestionariosPorAutor(usuario);

        for (Cuestionario cuestionarioEntity : cuestionariosEntity) {
            CuestionarioCliente cuestionarioCliente = recuperarCuestionario(autor, cuestionarioEntity.getNombre());
            cuestionariosCliente.add(cuestionarioCliente);
        }

        return cuestionariosCliente;
    }

    /**
     * Modifica los datos de un cuestionario registrado por un usuario con los
     * datos del cuestionario que viene en el parámetro.
     *
     * @param cuestionario Datos del cuestionario que fué editado en el cliente
     * @return True si de modifico con éxito el cuestionario, false si no fué
     * así
     * @throws RemoteException Lanzada si ocurre algún problema en la conexión
     * cliente-servidor
     */
    @Override
    public boolean editarCuestionario(CuestionarioCliente cuestionario) throws RemoteException {
        return eliminarCuestionario(cuestionario.getAutor(), cuestionario.getNombre())
               && guardarCuestionario(cuestionario);
    }

    /**
     * Elimina de la base de datos el cuestionario tenga como autor y nombre los
     * datos especificados en los parámetros.
     *
     * @param nombreAutor Nombre de usuario que registro el cuestionario
     * @param nombreCuestionario Nombre del cuestionario a eliminar
     * @return True si se eliminó con éxito el cuestionario, False si no fué así
     * @throws RemoteException Lanzada si ocurre algún error en la conexión
     * cliente-servidor
     */
    @Override
    public boolean eliminarCuestionario(String nombreAutor, String nombreCuestionario) throws RemoteException {
        boolean eliminadoExitoso = false;
        Usuario autor = controladorUsuario.findUsuario(nombreAutor);
        Cuestionario cuestionarioAEditar = controladorCuestionario.getCuestionario(autor, nombreCuestionario);
        if (controladorCuestionario.eliminarCuestionario(cuestionarioAEditar.getId())) {
            eliminadoExitoso = true;
        }
        return eliminadoExitoso;
    }

    /**
     * Inicializar las variables utilizadas en el ServidorCuestionario
     * obteniendo una referencia del persistence.xml y de los controladores JPA
     * utiliados.
     */
    public ServidorCuestionarioRMI() {
        EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        controladorCuestionario = new CuestionarioJpaController(fabricaEntityManager);
        controladorUsuario = new UsuarioJpaController(fabricaEntityManager);
    }

}
