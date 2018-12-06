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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.xml.bind.DatatypeConverter;
import mx.fei.qa.dominio.actores.Jugador;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.partida.MensajeChat;
import mx.fei.qa.partida.PuntajeJugador;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Administra los datos y eventos de un jugador unido a una partida.
 *
 * @version 1.0 11 Nov 2018
 * @author Carlos Onorio
 */
public class JugadorPartida {

    private static JugadorPartida jugadorPartida;
    private PuntajeJugador puntajeJugador;
    private final ArrayList<MensajeChat> chat;
    private short codigoInvitacion;
    private Socket socket;
    private String idSocketMonitor;
    
    private static final String DESCRIPCION = "descripcion";
    private static final String IMAGEN = "imagen";
    private static final String KEY_A_JUGAR = "key.aJugarQA";

    /**
     * Devuelve la instancia del JugadorPartida necesaria para que un usuario no
     * monitor juegue una partida.
     *
     * @return JugadorPartida
     */
    public static JugadorPartida obtenerInstancia() {
        if (jugadorPartida == null) {
            try {
                jugadorPartida = new JugadorPartida();
            } catch (URISyntaxException ex) {
                Logger.getLogger(JugadorPartida.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jugadorPartida;
    }

    /**
     * Obtiene la conexión con el puerto que escucha los eventos para jugar una
     * partida.
     *
     * @throws URISyntaxException
     */
    private JugadorPartida() throws URISyntaxException {
        this.chat = new ArrayList<>();
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
        String ipServidorEscogido = propiedadesCliente.getString("key.ipServidor1");
        socket = IO.socket("http://" + ipServidorEscogido + ":5000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                socket.emit("unirAPartida", codigoInvitacion);
            }
        }).on("comenzarPartida", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject preguntaRecibida = (JSONObject) os[0];
                PreguntaCliente pregunta = new PreguntaCliente();
                pregunta.setDescripcion(preguntaRecibida.getString("descripcion"));
                try {
                    pregunta.setImagen((byte[]) preguntaRecibida.get("imagen"));
                } catch (JSONException excepcion) {
                    pregunta.setImagen(null);
                }
                idSocketMonitor = (String) os[1];
                desplegarPregunta(pregunta);
            }
        }).on("desplegarPregunta", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject preguntaRecibida = (JSONObject) os[0];
                PreguntaCliente pregunta = new PreguntaCliente();
                if (preguntaRecibida.has(DESCRIPCION)) {
                    pregunta.setDescripcion(preguntaRecibida.getString(DESCRIPCION));
                }
                if (preguntaRecibida.has(IMAGEN)) {
                    pregunta.setImagen(DatatypeConverter.parseBase64Binary(preguntaRecibida.getString(IMAGEN)));
                }
                desplegarPregunta(pregunta);
            }
        }).on("desplegarRespuestas", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject preguntaConRespuestasRecibida = (JSONObject) os[0];
                JSONObject preguntaRecibida = preguntaConRespuestasRecibida.getJSONObject("pregunta");
                PreguntaCliente pregunta = new PreguntaCliente();
                if (preguntaRecibida.has(DESCRIPCION)) {
                    pregunta.setDescripcion(preguntaRecibida.getString(DESCRIPCION));
                }
                if (preguntaRecibida.has(IMAGEN)) {
                    pregunta.setImagen(DatatypeConverter.parseBase64Binary(preguntaRecibida.getString(IMAGEN)));
                }

                JSONArray respuestasRecibidas = preguntaConRespuestasRecibida.getJSONArray("respuestas");
                ArrayList<RespuestaCliente> respuestas = new ArrayList<>();
                for (int a = 0; a < respuestasRecibidas.length(); a++) {
                    JSONObject respuestaRecibida = respuestasRecibidas.getJSONObject(a);
                    RespuestaCliente respuesta = new RespuestaCliente();
                    String letraRecibida = respuestaRecibida.getString("letra");
                    char letra = letraRecibida.charAt(0);
                    respuesta.setLetra(letra);
                    respuesta.setEsCorrecta(respuestaRecibida.getBoolean("esCorrecta"));
                    if (respuestaRecibida.has(DESCRIPCION)) {
                        respuesta.setDescripcion(respuestaRecibida.getString(DESCRIPCION));
                    }
                    if (respuestaRecibida.has(IMAGEN)) {
                        respuesta.setImagen(DatatypeConverter.parseBase64Binary(respuestaRecibida.getString(IMAGEN)));
                    }
                    respuestas.add(respuesta);
                }
                pregunta.setRespuestas(respuestas);

                desplegarPreguntaYRespuestas(pregunta);
            }
        }).on("desplegarMarcador", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONArray marcadorRecibido = (JSONArray) os[0];
                ArrayList<String> marcador = new ArrayList<>();
                for (int a = 0; a < marcadorRecibido.length(); a++) {
                    marcador.add(marcadorRecibido.getString(a));
                }
                desplegarMarcador(marcador);
            }
        }).on("desplegarMedallero", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject medallero = (JSONObject) os[0];
                JSONObject primerLugar = medallero.getJSONObject("primerLugar");
                JSONObject segundoLugar = medallero.getJSONObject("segundoLugar");
                JSONObject tercerLugar = medallero.getJSONObject("tercerLugar");
                desplegarMedallero(primerLugar, segundoLugar, tercerLugar);
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
     * Elimina los datos de la partida jugada para poder unirse a otra o iniciar
     * sesión.
     */
    public void terminarJuego() {
        socket.emit("desconectarDePartida", codigoInvitacion);
        socket.disconnect();
        jugadorPartida = null;
    }

    /**
     * Establece los datos pertenecientes a un jugador que se une a una partida.
     *
     * @param codigoInvitacion Código de invitación de la partida a la que se
     * unió el jugador
     * @param jugador Datos del jugador
     */
    public void setDatosPartida(short codigoInvitacion, Jugador jugador) {
        this.codigoInvitacion = codigoInvitacion;
        this.puntajeJugador = new PuntajeJugador(jugador);
    }

    /**
     *
     * @return Código de invitación de la partida a la que se unió el jugador.
     */
    public short getCodigoInvitacion() {
        return codigoInvitacion;
    }

    /**
     *
     * @return Puntaje actual del jugador
     */
    public PuntajeJugador getPuntajeJugador() {
        return puntajeJugador;
    }

    /**
     * Despliega la pantalla MuestraDePregunta, donde se desplegarán la
     * descripción e imagen de la pregunta recibida.
     *
     * @param pregunta Datos de la pregunta actual de la partida sin respuestas
     */
    private void desplegarPregunta(PreguntaCliente pregunta) {
        Platform.runLater(() -> {
            ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("MuestraDePregunta.fxml"), recursoIdioma);
            try {
                Parent padre = cargadorFXML.load();
                MuestraDePreguntaController pantallaPregunta = cargadorFXML.getController();
                pantallaPregunta.desplegarPregunta(pregunta);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString(KEY_A_JUGAR));
                escenario.setResizable(false);
                escenario.show();
            } catch (IOException ex) {
                Logger.getLogger(JugadorPartida.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Despliega la pantalla MuestraPreguntaRespuestas, donde se desplegarán la
     * descripción e imagen de la pregunta recibida junto con sus respuestas
     * para que el jugador responda a la pregunta.
     *
     * @param pregunta Datos de la pregunta actual de la partida con respuestas
     */
    private void desplegarPreguntaYRespuestas(PreguntaCliente pregunta) {
        Platform.runLater(() -> {
            ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("MuestraPreguntaRespuestas.fxml"), recursoIdioma);
            try {
                Parent padre = cargadorFXML.load();
                MuestraPreguntaRespuestasController pantallaResponder = cargadorFXML.getController();
                pantallaResponder.desplegarPreguntaYRespuestas(pregunta);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString(KEY_A_JUGAR));
                escenario.setResizable(false);
                escenario.show();
            } catch (IOException ex) {
                Logger.getLogger(JugadorPartida.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Despliega la pantalla MarcadorJugadores con el marcador actual de la
     * partida.
     *
     * @param marcador Puntajes de los jugadores ordenados descendentemente
     */
    private void desplegarMarcador(ArrayList<String> marcador) {
        Platform.runLater(() -> {
            ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("MarcadorJugadores.fxml"), recursoIdioma);
            try {
                Parent padre = cargadorFXML.load();
                MarcadorJugadoresController pantallaMarcador = cargadorFXML.getController();
                pantallaMarcador.desplegarMarcador(marcador);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString(KEY_A_JUGAR));
                escenario.setResizable(false);
                escenario.show();
            } catch (IOException ex) {
                Logger.getLogger(JugadorPartida.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Muestra los 3 primeros jugadores en el marcador actual de la partida.
     *
     * @param primerLugar Nombre y puntaje del actual primer lugar
     * @param segundoLugar Nombre y puntaje del actual segundo lugar
     * @param tercerLugar Nombre y puntaje del actual tercer lugar
     */
    public void desplegarMedallero(JSONObject primerLugar, JSONObject segundoLugar, JSONObject tercerLugar) {
        Platform.runLater(() -> {
            String primerNombre = primerLugar.getString("nombre");
            String primerPuntaje = primerLugar.getString("puntaje");
            String segundoNombre = segundoLugar.getString("nombre");
            String segundoPuntaje = segundoLugar.getString("puntaje");
            String tercerNombre = tercerLugar.getString("nombre");
            String tercerPuntaje = tercerLugar.getString("puntaje");

            ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("Medallero.fxml"), recursoIdioma);
            try {
                Parent padre = cargadorFXML.load();
                MedalleroController pantallaMedallero = cargadorFXML.getController();
                pantallaMedallero.mostrarGanadores(primerNombre, primerPuntaje,
                        segundoNombre, segundoPuntaje,
                        tercerNombre, tercerPuntaje);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString(KEY_A_JUGAR));
                escenario.setResizable(false);
                escenario.show();
            } catch (IOException ex) {
                Logger.getLogger(JugadorPartida.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Agrega un nuevo mensaje al chat de la partida.
     *
     * @param mensaje Mensaje recibido o enviado por un jugador o monitor
     */
    public void actualizarChat(MensajeChat mensaje) {
        chat.add(mensaje);
        ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
        FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("Chat.fxml"), recursoIdioma);
        try {
            ChatController pantallaChat = cargadorFXML.getController();
            if (pantallaChat != null) {
                pantallaChat.actualizarChat();
            } else {
                Parent padre = cargadorFXML.load();
                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString(KEY_A_JUGAR));
                escenario.setResizable(false);
                escenario.show();
            }
        } catch (IOException ex) {
            Logger.getLogger(JugadorPartida.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
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
        socket.emit("enviarMensajeChat", codigoInvitacion, mensajeAEnviar);
        actualizarChat(mensaje);
    }

    /**
     * Adapta los mensajes del chat de la partida para ser visualizados en la
     * interfaz de usuario.
     *
     * @return Chat adaptado para IU
     */
    public List<String> getChat() {
        ArrayList<String> chatParaIU = new ArrayList<>();
        for (int a = 0; a < chat.size(); a++) {
            MensajeChat mensaje = chat.get(a);
            chatParaIU.add(mensaje.getNombreJugador() + ": " + mensaje.getMensaje());
        }
        return chatParaIU;
    }

    /**
     * Envia la respuesta seleccionada por el jugador en una pregunta al monitor
     * de la partida.
     *
     * @param respuestaLetra Respuesta elegida
     */
    public void enviarRespuesta(char respuestaLetra) {
        JSONObject respuestaAPregunta = new JSONObject();
        respuestaAPregunta.put("respuesta", String.valueOf(respuestaLetra));
        respuestaAPregunta.put("nombreJugador", puntajeJugador.getJugador().getNombre());
        respuestaAPregunta.put("puntajeJugador", puntajeJugador.getPuntaje());
        socket.emit("responderPregunta", idSocketMonitor, respuestaAPregunta);
    }

}
