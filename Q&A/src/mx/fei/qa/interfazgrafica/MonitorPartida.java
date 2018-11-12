/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mx.fei.qa.comunicacion.interfaz.PartidaInterface;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.partida.GraficaRespuestas;
import mx.fei.qa.partida.MensajeChat;
import mx.fei.qa.partida.Partida;
import mx.fei.qa.partida.PuntajeJugador;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Administra los datos y eventos de un usuario que monitorea una partida.
 *
 * @version 1.0 11 Nov 2018
 * @author Carlos Onorio
 */
public class MonitorPartida {

    private static MonitorPartida monitor;
    private Socket socket;
    private Partida partida;
    private short codigoInvitacion;
    private PreguntaCliente preguntaActual;
    private Registry registro;
    private PartidaInterface stubPartida;

    /**
     * Devuelve la instancia del MonitorPartida para controlar los eventos que
     * rigen la partida actual.
     *
     * @return MonitorPartida
     */
    public static MonitorPartida obtenerInstancia() {
        if (monitor == null) {
            try {
                monitor = new MonitorPartida();
            } catch (URISyntaxException ex) {
                Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return monitor;
    }

    /**
     * Obtiene la conexión con el puerto que escucha los eventos para jugar una
     * partida.
     *
     * @throws URISyntaxException
     */
    private MonitorPartida() throws URISyntaxException {
        socket = IO.socket("http://localhost:5000");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                socket.emit("unirAPartida", codigoInvitacion);
                System.out.println("Monitor conectado" + codigoInvitacion);
            }
        }).on("recibirRespuesta", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject respuestaRecibida = (JSONObject) os[0];
                String letraRecibida = respuestaRecibida.getString("respuesta");
                char respuesta = letraRecibida.charAt(0);
                String jugador = respuestaRecibida.getString("nombreJugador");
                int puntaje = respuestaRecibida.getInt("puntajeJugador");

                partida.getGraficaPreguntaActual().actualizarGrafica(respuesta);
                partida.actualizarMarcador(jugador, puntaje);
            }
        }).on("recibirMensajeChat", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject mensajeRecibido = (JSONObject) os[0];
                MensajeChat mensaje = new MensajeChat();
                mensaje.setNombreJugador(mensajeRecibido.getString("jugador"));
                mensaje.setMensaje(mensajeRecibido.getString("mensaje"));
                actualizarChat(mensaje);
            }
        });

        socket.connect();
    }

    /**
     * Comprueba si el usuario actual es monitor o jugador de alguna partida.
     *
     * @return True si el usuario actual es el monitor, False si es jugador
     */
    public static boolean existeMonitorPartida() {
        boolean existe = false;
        if (monitor != null) {
            existe = true;
        }
        return existe;
    }

    /**
     *
     * @return Pregunta que se esta jugando actualmente en la partida
     */
    public PreguntaCliente getPreguntaActual() {
        return preguntaActual;
    }

    /**
     * Crea un nueva partida para que el usuario actual monitoree la partida y
     * otros jugadores puedan unirse mediante código de invitación.
     *
     * @param nombreCuestionario Nombre del cuestionario a jugar
     * @return True si se creó exitosamente la partida, False si no fue así
     */
    public boolean iniciarPartida(String nombreCuestionario) {
        boolean creacionExitosa = false;
        try {
            registro = LocateRegistry.getRegistry();
            stubPartida = (PartidaInterface) registro.lookup("servidorPartidas");
            AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
            String autorCuestionario = administradorSesion.getNombreUsuarioActual();
            codigoInvitacion = stubPartida.crearPartida(autorCuestionario, nombreCuestionario);
            if (codigoInvitacion >= 0) {
                creacionExitosa = true;
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return creacionExitosa;
    }

    /**
     * Envía todas las invitaciones a la partida.
     *
     * @param correos Correos electrónicos ingresados
     */
    public void enviarInvitaciones(ArrayList<String> correos) {
        Platform.runLater(() -> {
            for (int a = 0; a < correos.size(); a++) {
                enviarCorreo(correos.get(a));
            }
        });
    }

    /**
     * Envía el código de invitación al correoDestinario, a través del correo
     * del sistema.
     *
     * @param correoDestinatario Correo electrónico destinarario
     */
    private void enviarCorreo(String correoDestinatario) {
        ResourceBundle recursoPropiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
        String correoDelJuego = recursoPropiedadesCliente.getString("key.correoDelJuego");
        String contrasenia = recursoPropiedadesCliente.getString("key.contraseniaDelCorreo");

        Properties propiedadesCorreo = new Properties();
        propiedadesCorreo.put("mail.smtp.auth", "true");
        propiedadesCorreo.put("mail.smtp.starttls.enable", "true");
        propiedadesCorreo.put("mail.smtp.host", "smtp.gmail.com");
        propiedadesCorreo.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(propiedadesCorreo,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoDelJuego, contrasenia);
            }
        });

        try {
            Locale locale = Locale.getDefault();
            ResourceBundle recursoIdioma = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
            String asunto = recursoIdioma.getString("key.asuntoCorreo");
            String cuerpo = recursoIdioma.getString("key.cuerpoCorreo");
            Message correo = new MimeMessage(sesion);

            correo.setFrom(new InternetAddress(correoDelJuego));
            correo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestinatario));
            correo.setSubject(asunto);
            correo.setText(cuerpo + ":\n\n" + Short.toString(codigoInvitacion));

            Transport.send(correo);

        } catch (MessagingException ex) {
            UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema",
                    "key.encabezadoError", "key.errorEnviarCorreo");
        }
    }

    /**
     * Envía la primer pregunta a todos los jugadores unidos a la partida.
     */
    public void empezarJuego() {
        try {
            partida = stubPartida.recuperarPartida(codigoInvitacion);
            preguntaActual = partida.obtenerPrimerPregunta();

            JSONObject pregunta = new JSONObject();
            pregunta.put("descripcion", preguntaActual.getDescripcion());
            pregunta.put("imagen", preguntaActual.getImagen());
            socket.emit("comenzarPartida", codigoInvitacion, pregunta);

            Locale locale = Locale.getDefault();
            ResourceBundle recursoIdioma = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("MuestraDePregunta.fxml"));
            try {
                Parent padre = cargadorFXML.load();
                MuestraDePreguntaController pantallaPregunta = cargadorFXML.getController();
                pantallaPregunta.desplegarPregunta(preguntaActual);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString("key.aJugarQA"));
                escenario.setResizable(false);
                escenario.show();
            } catch (IOException ex) {
                Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Finaliza la partida actual y elimina todos sus datos.
     */
    public void terminarJuego() {
        try {
            socket.emit("desconectarDePartida", codigoInvitacion);
            socket.disconnect();
            stubPartida.finalizarPartida(codigoInvitacion);
            monitor = null;
        } catch (RemoteException ex) {
            Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envia una pregunta para que los jugadores unidos a la partida puedan
     * responderla.
     */
    public void enviarPreguntaAJugadores() throws IndexOutOfBoundsException {
        preguntaActual = partida.obtenerSiguientePregunta();
        JSONObject pregunta = new JSONObject();
        pregunta.put("descripcion", preguntaActual.getDescripcion());
        pregunta.put("imagen", preguntaActual.getImagen());
        socket.emit("enviarSiguientePregunta", codigoInvitacion, pregunta);

        Locale locale = Locale.getDefault();
        ResourceBundle recursoIdioma = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
        FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("MuestraDePregunta.fxml"), recursoIdioma);
        try {
            Parent padre = cargadorFXML.load();
            MuestraDePreguntaController pantallaPregunta = cargadorFXML.getController();
            pantallaPregunta.desplegarPregunta(preguntaActual);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(padre));
            escenario.setTitle(recursoIdioma.getString("key.aJugarQA"));
            escenario.setResizable(false);
            escenario.show();
        } catch (IOException ex) {
            Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envía las respuestas de la pregunta actual a los jugadores unidos a la
     * partida.
     */
    public void enviarRespuestasAJugadores() {
        ArrayList<RespuestaCliente> respuestasCliente = partida.obtenerRespuestasPreguntaActual();
        JSONObject preguntaConRespuestas = new JSONObject();
        JSONArray respuestas = new JSONArray();
        respuestasCliente.forEach((respuestaCliente) -> {
            JSONObject respuesta = new JSONObject();
            String letra = String.valueOf(respuestaCliente.getLetra());
            respuesta.put("letra", letra);
            respuesta.put("descripcion", respuestaCliente.getDescripcion());
            respuesta.put("imagen", respuestaCliente.getImagen());
            respuesta.put("esCorrecta", respuestaCliente.isEsCorrecta());
            respuestas.put(respuesta);
        });
        preguntaConRespuestas.put("descripcionPregunta", preguntaActual.getDescripcion());
        preguntaConRespuestas.put("imagenPregunta", preguntaActual.getImagen());
        preguntaConRespuestas.put("respuestas", respuestas);
        socket.emit("enviarRespuestasDePregunta", codigoInvitacion, preguntaConRespuestas);
    }

    /**
     *
     * @return Resultados de respuestas de la pregunta actual
     */
    public GraficaRespuestas obtenerGraficaPreguntaActual() {
        return partida.getGraficaPreguntaActual();
    }

    /**
     * Envía el marcador actual de la partida adaptado para ser mostrado en la
     * interfaz gráfica.
     */
    public void enviarMarcadorAJugadores() {
        ArrayList<PuntajeJugador> marcador = partida.getMarcador();
        ArrayList<String> marcadorString = new ArrayList<>();
        for (int a = 0; a < marcador.size(); a++) {
            PuntajeJugador puntaje = marcador.get(a);
            String cadena = Integer.toString((a + 1)) + ". "
                    + puntaje.getJugador().getNombre() + "\t"
                    + Integer.toString(puntaje.getPuntaje());
            marcadorString.add(cadena);
        }
        JSONArray marcadorAEnviar = new JSONArray(marcadorString);
        socket.emit("enviarMarcador", codigoInvitacion, marcadorAEnviar);

        Locale locale = Locale.getDefault();
        ResourceBundle recursoIdioma = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
        FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("MarcadorJugadores.fxml"), recursoIdioma);
        try {
            Parent padre = cargadorFXML.load();
            MarcadorJugadoresController pantallaMarcador = cargadorFXML.getController();
            pantallaMarcador.desplegarMarcador(marcadorString);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(padre));
            escenario.setTitle(recursoIdioma.getString("key.aJugarQA"));
            escenario.setResizable(false);
            escenario.show();
        } catch (IOException ex) {
            Logger.getLogger(MonitorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envía los 3 primeros lugares en el marcador de la partida a todos los
     * jugadores unidos a esta.
     */
    public void enviarMedalleroAJugadores() {
        JSONObject primerLugar = new JSONObject();
        primerLugar.put("nombre", partida.getMarcador().get(0).getJugador().getNombre());
        primerLugar.put("puntaje", Integer.toString(partida.getMarcador().get(0).getPuntaje()));

        JSONObject segundoLugar = new JSONObject();
        segundoLugar.put("nombre", partida.getMarcador().get(1).getJugador().getNombre());
        segundoLugar.put("puntaje", Integer.toString(partida.getMarcador().get(1).getPuntaje()));

        JSONObject tercerLugar = new JSONObject();
        tercerLugar.put("nombre", partida.getMarcador().get(2).getJugador().getNombre());
        tercerLugar.put("puntaje", Integer.toString(partida.getMarcador().get(2).getPuntaje()));

        JSONObject medallero = new JSONObject();
        medallero.put("primerLugar", primerLugar);
        medallero.put("segundoLugar", segundoLugar);
        medallero.put("tercerLugar", tercerLugar);

        socket.emit("enviarMedallero", codigoInvitacion, medallero);

        String primerNombre = primerLugar.getString("nombre");
        String primerPuntaje = primerLugar.getString("puntaje");
        String segundoNombre = segundoLugar.getString("nombre");
        String segundoPuntaje = segundoLugar.getString("puntaje");
        String tercerNombre = tercerLugar.getString("nombre");
        String tercerPuntaje = tercerLugar.getString("puntaje");

        Locale locale = Locale.getDefault();
        ResourceBundle recursoIdioma = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
        FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("Medallero.fxml"), recursoIdioma);
        try {
            Parent padre = cargadorFXML.load();
            MedalleroController pantallaMedallero = cargadorFXML.getController();
            pantallaMedallero.mostrarGanadores(primerNombre, primerPuntaje,
                    segundoNombre, segundoPuntaje,
                    tercerNombre, tercerPuntaje);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(padre));
            escenario.setTitle(recursoIdioma.getString("key.aJugarQA"));
            escenario.setResizable(false);
            escenario.show();
        } catch (IOException ex) {
            Logger.getLogger(JugadorPartida.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Agrega un nuevo mensaje al chat de la partida.
     *
     * @param mensaje Mensaje recibido o enviado por un jugador o monitor
     */
    public void actualizarChat(MensajeChat mensaje) {
        partida.actualizarChat(mensaje);
    }

    /**
     * Envía un mensaje a todos los jugadores de la partida.
     *
     * @param mensaje Mensaje a enviar
     */
    public void enviarMensajeChat(MensajeChat mensaje) {
        JSONObject mensajeAEnviar = new JSONObject();
        mensajeAEnviar.put("jugador", mensaje.getNombreJugador());
        mensajeAEnviar.put("mensaje", mensaje.getMensaje());
        socket.emit("enviarMensajeChat", mensajeAEnviar);
        actualizarChat(mensaje);
    }

    /**
     * Adapta los mensajes del chat de la partida para ser visualizados en la
     * interfaz de usuario.
     *
     * @return Chat adaptado para IU
     */
    public ArrayList<String> getChat() {
        ArrayList<String> chatParaIU = new ArrayList<>();
        ArrayList<MensajeChat> chatPartida = partida.getChat();
        for (int a = 0; a < chatPartida.size(); a++) {
            MensajeChat mensaje = chatPartida.get(a);
            chatParaIU.add(mensaje.getNombreJugador() + ": " + mensaje.getMensaje());
        }
        return chatParaIU;
    }

}