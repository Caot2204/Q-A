/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mx.fei.qa.partida.MensajeChat;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaCadena;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class ChatController implements Initializable {

    @FXML
    private ListView listViewChat;

    @FXML
    private TextField textFieldMensaje;

    private MonitorPartida monitor;
    private JugadorPartida jugador;
    private ObservableList<String> chatParaListView;
    private String usuarioActual;

    /**
     * Inicializa el chat mostrando los mensajes actuales.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarChat();
        listViewChat.setItems(chatParaListView);
    }

    /**
     * Refresca el chat para mostrar los mensajes actuales en la interfaz de
     * usuario.
     */
    public void actualizarChat() {
        if (MonitorPartida.existeMonitorPartida()) {
            monitor = MonitorPartida.obtenerInstancia();
            usuarioActual = AdministradorSesionActual.obtenerAdministrador().getNombreUsuarioActual();
            List<String> chat = monitor.getChat();
            chatParaListView = FXCollections.observableArrayList(chat);
            listViewChat.refresh();
        } else {
            jugador = JugadorPartida.obtenerInstancia();
            usuarioActual = jugador.getPuntajeJugador().getJugador().getNombre();
            List<String> chat = jugador.getChat();
            chatParaListView = FXCollections.observableArrayList(chat);
            listViewChat.refresh();
        }
    }

    /**
     * Envía un mensaje al chat de la partida para que todos los jugadores los
     * vean.
     */
    public void enviarMensaje() {
        String mensaje = textFieldMensaje.getText();
        if (UtileriaCadena.validarCadena(mensaje, 1, Integer.MAX_VALUE)) {
            MensajeChat mensajeChat = new MensajeChat();
            mensajeChat.setNombreJugador(usuarioActual);
            mensajeChat.setMensaje(mensaje);

            if (MonitorPartida.existeMonitorPartida()) {
                monitor.enviarMensajeChat(mensajeChat);
            } else {
                jugador.enviarMensajeChat(mensajeChat);
            }
        }
        textFieldMensaje.clear();
    }

}
