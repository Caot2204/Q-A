/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import mx.fei.qa.partida.AdministradorPartida;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class VInvitacionParaJugarController implements Initializable {

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

    public void enviarInvitaciones() {
        enviarCodigoInvitacionACorreos();        
    }
    
    private void enviarCodigoInvitacionACorreos() {
        String campoCorreos = textFieldCorreos.getText();
        
        if (campoCorreos != null && !campoCorreos.isEmpty()) {
            correos = new ArrayList<>();
            String[] correosIngresados = campoCorreos.split(",");
            correos.addAll(Arrays.asList(correosIngresados));
            AdministradorPartida administradorPartida = AdministradorPartida.obtenerInstancia();
            administradorPartida.enviarInvitaciones(correos);
            UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.aJugarQA", "AJugarQA.fxml", textFieldCorreos);
        }
    }

}
