/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.sesion;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import mx.fei.qa.comunicacion.interfaz.PartidaInterface;
import mx.fei.qa.dominio.actores.Jugador;
import mx.fei.qa.interfazgrafica.JugadorPartida;
import mx.fei.qa.interfazgrafica.PrincipalController;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import org.controlsfx.control.Notifications;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Carlos Onorio
 */
public class AdministradorSesionActual {

    private static AdministradorSesionActual administradorSesion;
    private List<UsuarioConectadoAServidor> usuariosConectados;
    private SesionUsuario sesionUsuario;
    private Socket socket;

    /**
     * Obtiene una instancia de AdministradorSesionActual para manipular la
     * sesión iniciada en el cliente.
     *
     * @return Administrador de la sesión actual para obtener los datos de esta
     */
    public static AdministradorSesionActual obtenerAdministrador() {
        if (administradorSesion == null) {
            administradorSesion = new AdministradorSesionActual();
        }
        return administradorSesion;
    }

    private AdministradorSesionActual() {

    }

    /**
     * Verifica si existe una sesión de usuario iniciada en el sistema.
     *
     * @return True si hay una sesión iniciada, False si no la hay
     */
    public boolean existeSesionIniciada() {
        boolean existeSesion = false;
        if (sesionUsuario != null) {
            existeSesion = true;
        }
        return existeSesion;
    }

    /**
     *
     * @return Nombre del usuario de la sesión actual en el cliente
     */
    public String getNombreUsuarioActual() {
        return this.sesionUsuario.getUsuario().getNombre();
    }

    /**
     *
     * @return Correo del usuario de la sesión actual en el cliente
     */
    public String getCorreoUsuarioActual() {
        return this.sesionUsuario.getUsuario().getCorreo();
    }

    /**
     * Establece la sesión actual del usuario y registra su conexión al
     * servidor.
     *
     * @param sesionUsuario Sesion actual del usuario
     * @throws java.net.URISyntaxException Lanzada al error en conectar el socket
     */
    public void setSesionUsuario(SesionUsuario sesionUsuario) throws URISyntaxException {
        this.sesionUsuario = sesionUsuario;
        this.usuariosConectados = new ArrayList<>();
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
        String ipServidorEscogido = propiedadesCliente.getString("key.ipServidor1");
        socket = IO.socket("http://" + ipServidorEscogido + ":6000");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                socket.emit("registrarConexion", socket.id(), getNombreUsuarioActual());
            }
        }).on("recibirUsuariosConectados", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                usuariosConectados.clear();
                JSONArray usuariosRecibidos = (JSONArray) os[0];
                for (int a = 0; a < usuariosRecibidos.length(); a++) {
                    JSONObject usuarioJSON = usuariosRecibidos.getJSONObject(a);
                    String nombreUsuario = usuarioJSON.getString("nombre");
                    String idSocket = usuarioJSON.getString("idSocket");
                    if (!nombreUsuario.equals(getNombreUsuarioActual())) {
                        UsuarioConectadoAServidor usuarioConectado = new UsuarioConectadoAServidor(nombreUsuario, idSocket);
                        usuariosConectados.add(usuarioConectado);
                    }
                }
            }
        }).on("recibirInvitacion", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                String tituloCuestionario = (String) os[0];
                short codigoInvitacion = (short) os[1];
                String cuerpo = "Te invitaron a unirte a la partida, da clic sobre la notificación";
                Notifications notificacion = Notifications.create()
                        .title("Cuestionario: " + tituloCuestionario)
                        .text(cuerpo)
                        .hideAfter(Duration.seconds(10))
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                unirAPartida(codigoInvitacion);
                            }
                        });
                notificacion.show();
            }
        });

        socket.connect();
    }

    /**
     *
     * @return Sesión actual del usuario
     */
    public SesionUsuario getSesionUsuario() {
        return sesionUsuario;
    }

    /**
     * Elimina la sesión actual de usuario y lo desconecta del servidor.
     */
    public void removerSesionActual() {
        this.sesionUsuario = null;
        socket.disconnect();
    }

    /**
     * Recupera los usuarios conectados en el servidor QA.
     *
     * @return Usuarios conectados
     */
    public List<UsuarioConectadoAServidor> obtenerUsuariosConectados() {
        socket.emit("recuperarUsuariosConectados");
        return this.usuariosConectados;
    }

    /**
     * Une a una partida al usuario actual.
     *
     * @param codigoInvitacion Código de invitación a la partida que fué
     * invitado
     */
    private void unirAPartida(short codigoInvitacion) {
        try {
            ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
            Registry registro = LocateRegistry.getRegistry(propiedadesCliente.getString("key.ipServidor1"));
            PartidaInterface stubPartida = (PartidaInterface) registro.lookup("servidorPartidas");

            Jugador jugador = new Jugador();
            jugador.setNombre(getNombreUsuarioActual());
            jugador.setFotoPerfil(null);
            if (stubPartida.unirAPartida(codigoInvitacion, jugador)) {
                JugadorPartida jugadorPartida = JugadorPartida.obtenerInstancia();
                jugadorPartida.setDatosPartida(codigoInvitacion, jugador);
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.aJugarQA",
                        "SalaEspera.fxml", null);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema",
                        "key.encabezadoError", "key.noExistePartida");
            }

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envía notificacion a un determinado usuario.
     *
     * @param usuario Usuario al que le será enviada la notificacion
     * @param codigoInvitacion Código de invitación a una partida
     * @param nombreCuestionario Cuestionario al cual tratará la partida
     */
    public void enviarNotificacionesAUsuarios(String usuario, short codigoInvitacion, String nombreCuestionario) {
        socket.emit("enviarInvitacionAJugador", usuario, codigoInvitacion, nombreCuestionario);
    }

}
