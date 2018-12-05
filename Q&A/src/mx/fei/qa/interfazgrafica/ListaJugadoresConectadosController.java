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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class ListaJugadoresConectadosController implements Initializable {

    @FXML
    private AnchorPane panel;

    @FXML
    private ListView listViewJugadoresConectados;

    @FXML
    private Button buttonEmpezarJuego;

    private MonitorPartida monitorPartida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (MonitorPartida.existeMonitorPartida()) {
            monitorPartida = MonitorPartida.obtenerInstancia();
        } else {
            buttonEmpezarJuego.setDisable(true);
            buttonEmpezarJuego.setVisible(false);
        }
    }

    /**
     * Comienza la partida actual.
     */
    public void empezarJuego() {
        monitorPartida.empezarJuego();
        UtileriaInterfazUsuario.cerrarVentanaMedianteElementoFX(listViewJugadoresConectados);
    }

}
