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
import javafx.scene.control.Label;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class MedalleroController implements Initializable {

    @FXML
    private Label labelPrimerLugarNombre;

    @FXML
    private Label labelPrimerLugarPuntaje;

    @FXML
    private Label labelSegundoLugarNombre;

    @FXML
    private Label labelSegundoLugarPuntaje;

    @FXML
    private Label labelTercerLugarNombre;

    @FXML
    private Label labelTercerLugarPuntaje;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Muestra los 3 mejores jugadores de la partida.
     *
     * @param primerNombre Nombre del primer lugar
     * @param primerPuntaje Puntaje del primer lugar
     * @param segundoNombre Nombre del segudno lugar
     * @param segundoPuntaje Puntaje del segundo lugar
     * @param tercerNombre Nombre del tercer lugar
     * @param tercerPuntaje Puntaje del tercer lugar
     */
    public void mostrarGanadores(String primerNombre, String primerPuntaje,
            String segundoNombre, String segundoPuntaje,
            String tercerNombre, String tercerPuntaje) {

        labelPrimerLugarNombre.setText(primerNombre);
        labelPrimerLugarPuntaje.setText(primerPuntaje);
        labelSegundoLugarNombre.setText(segundoNombre);
        labelSegundoLugarPuntaje.setText(segundoPuntaje);
        labelTercerLugarNombre.setText(tercerNombre);
        labelTercerLugarPuntaje.setText(tercerPuntaje);
    }

    /**
     * Termina la partida actual.
     */
    public void finalizarJuego() {
        if (MonitorPartida.existeMonitorPartida()) {
            MonitorPartida.obtenerInstancia().terminarJuego();
            UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard", 
                "DashboardQA.fxml", labelSegundoLugarNombre);
        } else {
            UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.principal", 
                "Principal.fxml", labelSegundoLugarNombre);
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
