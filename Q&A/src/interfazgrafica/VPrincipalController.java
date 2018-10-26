/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazgrafica;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class VPrincipalController implements Initializable {

    @FXML
    private TextField textFieldCodigoInvitacion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void unirseAPartida() {
        
    }
    
    public void iniciarSesion() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.iniciarSesion", "VIniciarSesion.fxml", textFieldCodigoInvitacion);
    }
    
    public void registrarUsuario() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.registroUsuario", "VRgistrarUsuario.fxml", textFieldCodigoInvitacion);        
    }
    
}
