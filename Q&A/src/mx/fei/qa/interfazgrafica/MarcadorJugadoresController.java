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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class MarcadorJugadoresController implements Initializable {

    @FXML
    private ListView listViewMarcador;

    @FXML
    private Button buttonSiguiente;

    private MonitorPartida monitor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (MonitorPartida.existeMonitorPartida()) {
            monitor = MonitorPartida.obtenerInstancia();
        } else {
            buttonSiguiente.setOnAction(null);
            buttonSiguiente.setDisable(true);
            buttonSiguiente.setVisible(false);
        }
    }

    /**
     * Despliega el marcador actual de la partida a los jugadores y monitor.
     *
     * @param marcador Marcador actual
     */
    public void desplegarMarcador(List<String> marcador) {
        ObservableList<String> marcadorParaListView = FXCollections.observableArrayList(marcador);
        listViewMarcador.setItems(marcadorParaListView);
    }

    /**
     * Permite continuar con la partida enviando la siguiente pregunta a todos
     * los jugadores, si no hay más preguntas en el cuestionario, muestra el
     * medallero.
     */
    public void continuarJuego() {
        try {
            monitor.enviarPreguntaAJugadores();
            UtileriaInterfazUsuario.cerrarVentanaMedianteElementoFX(buttonSiguiente);
        } catch (IndexOutOfBoundsException excepcion) {
            monitor.enviarMedalleroAJugadores();
            UtileriaInterfazUsuario.cerrarVentanaMedianteElementoFX(listViewMarcador);
        }
    }

    /**
     * Despliega el chat actual de la partida.
     */
    public void mostrarChat() {
        UtileriaInterfazUsuario.mostrarVentanaSinCerrarActual(getClass(),
                "key.chat", "Chat.fxml");
    }

}
