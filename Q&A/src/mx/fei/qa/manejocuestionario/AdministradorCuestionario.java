/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.manejocuestionario;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.qa.comunicacion.interfaz.CuestionarioInterface;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 *
 * @author Carlos Onorio
 */
public class AdministradorCuestionario {

    private static AdministradorCuestionario administrador;
    private CuestionarioCliente cuestionario;
    private boolean actualizacionDeCuestionario;

    /**
     * Recupera al AdministradorCuestionario para la creación y/o edición de un
     * Cuestionario.
     *
     * @return Instancia del AdministradorCuestionario
     */
    public static AdministradorCuestionario obtenerInstancia() {
        if (administrador == null) {
            administrador = new AdministradorCuestionario();
        }
        return administrador;
    }

    /**
     * Inicializa un nuevo cuestionario para crear sus preguntas y respuestas.
     */
    public void crearNuevoCuestionario() {
        this.cuestionario = new CuestionarioCliente();
        this.actualizacionDeCuestionario = false;
    }

    /**
     * Establece los datos del cuestionario que serán editados.
     *
     * @param cuestionario Cuestionario a editar
     */
    public void editarCuestionario(CuestionarioCliente cuestionario) {
        this.cuestionario = cuestionario;
        this.actualizacionDeCuestionario = true;
    }

    /**
     * Establece el nombre y autor del cuestionario.
     *
     * @param nombreCuestionario Nombre escrito para el cuestionario
     */
    public void establecerDatosGenerales(String nombreCuestionario) {
        AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
        cuestionario.setNombre(nombreCuestionario);
        cuestionario.setAutor(administradorSesion.getSesionUsuario().getUsuario().getNombre());
    }

    /**
     * Agrega una pregunta temporalmente al cuestionario.
     *
     * @param pregunta Pregunta ingresada en la IU
     */
    public void agregarPregunta(PreguntaCliente pregunta) {
        pregunta.setNumero((cuestionario.getPreguntas().size() + 1));
        cuestionario.agregarPregunta(pregunta);
    }

    /**
     * Actualiza los datos de una pregunta modificada.
     *
     * @param pregunta Pregunta actualizada
     */
    public void actualizarPregunta(PreguntaCliente pregunta) {
        cuestionario.getPreguntas().set((pregunta.getNumero() - 1), pregunta);
    }

    /**
     * Elimina la pregunta seleccionada por el usuario.
     *
     * @param numeroPregunta Número de la pregunta que será eliminada.
     *
     * @return true si se eliminó la pregunta, false si no fué así
     */
    public boolean eliminarPregunta(int numeroPregunta) {
        boolean preguntaEliminada = false;
        cuestionario.getPreguntas().remove(numeroPregunta);
        for (int a = 0; a < cuestionario.getPreguntas().size(); a++) {
            PreguntaCliente pregunta = cuestionario.getPreguntas().get(a);
            pregunta.setNumero((a + 1));
        }
        preguntaEliminada = true;
        return preguntaEliminada;
    }

    /**
     * Guarda el cuestionario ya sea el nuevo o el actualizado.
     *
     * @return true si se guardó exitosamente, false si no fué así
     */
    public boolean guardarCuestionario() {
        boolean guardadoExitoso = false;

        try {
            ResourceBundle recursoPropiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
            Registry registro = LocateRegistry.getRegistry(recursoPropiedadesCliente.getString("key.ipServidor1"), 0);
            CuestionarioInterface stub = (CuestionarioInterface) registro.lookup("servidorCuestionarios");
            if (actualizacionDeCuestionario) {
                stub.editarCuestionario(cuestionario);
            } else {
                stub.guardarCuestionario(cuestionario);
            }
            guardadoExitoso = true;
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(AdministradorCuestionario.class.getName()).log(Level.SEVERE, null, ex);
            UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion", "key.errorAlConectar", "key.problemaConexion");
        }

        return guardadoExitoso;
    }

    /**
     *
     * @return Preguntas actuales del cuestionario
     */
    public List<PreguntaCliente> getPreguntasCuestionario() {
        return this.cuestionario.getPreguntas();
    }

}
