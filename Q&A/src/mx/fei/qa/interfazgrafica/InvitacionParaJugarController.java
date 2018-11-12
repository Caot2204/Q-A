/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * Controlador del archivo FXML InvitacionParaJugar.fxml
 *
 * @version 1.0 10 Nov 2018
 * @author Carlos Onorio
 */
public class InvitacionParaJugarController implements Initializable {

    @FXML
    private TextField textFieldCorreos;

    private ArrayList<String> correos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Envia las invitaciones para jugar a los jugadores conectados que el
     * monitor selecciono en la IU y a los correos electrónicos ingresados
     */
    public void enviarInvitaciones() {
        recuperarCorreosIngresados();
        MonitorPartida administradorPartida = MonitorPartida.obtenerInstancia();
        administradorPartida.enviarInvitaciones(correos);
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.aJugarQA",
                "ListaJugadoresConectados.fxml", textFieldCorreos);
    }

    /**
     * Obtiene los correos ingresados por el monitor de la partida
     */
    private void recuperarCorreosIngresados() {
        correos = new ArrayList<>();
        String campoCorreos = textFieldCorreos.getText();
        if (campoCorreos != null && !campoCorreos.isEmpty()) {
            String[] correosIngresados = campoCorreos.split(",");
            correos.addAll(Arrays.asList(correosIngresados));
        }
    }

}
