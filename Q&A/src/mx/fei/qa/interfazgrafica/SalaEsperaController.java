/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class SalaEsperaController implements Initializable {

    @FXML
    private Label labelNombreJugador;

    @FXML
    private Label labelPuntajeActual;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JugadorPartida jugadorPartida = JugadorPartida.obtenerInstancia();
        labelNombreJugador.setText(jugadorPartida.getPuntajeJugador().getJugador().getNombre());
        labelNombreJugador.setAlignment(Pos.CENTER);
        labelPuntajeActual.setText(Integer.toString(jugadorPartida.getPuntajeJugador().getPuntaje()));
        labelPuntajeActual.setAlignment(Pos.CENTER);
    }
    
    /**
     * Despliega el chat actual de la partida.
     */
    public void mostrarChat() {
        UtileriaInterfazUsuario.mostrarVentanaSinCerrarActual(getClass(),
                "key.chat", "Chat.fxml");
    }
}
