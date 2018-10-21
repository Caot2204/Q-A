/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazgrafica;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        mostrarVentana("IniciarSesion", "VIniciarSesion.fxml");
    }
    
    public void registrarUsuario() {
        
    }
    
    /**
     * Cierra la IU actual y despliega la IU especificada en los parámetros
     *
     * @param titulo Titulo de la ventana
     * @param nombreFXML Nombre del archivo .fxml
     */
    public void mostrarVentana(String titulo, String nombreFXML) {
        Locale locale = Locale.getDefault();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(nombreFXML), ResourceBundle.getBundle("lang.lang", locale));
            Stage escenario = new Stage();
            Scene scene = new Scene(root);
            escenario.setScene(scene);
            escenario.show();
            
            Stage escenarioActual = (Stage) textFieldCodigoInvitacion.getScene().getWindow();
            escenarioActual.close();
        } catch (IOException ex) {
            Logger.getLogger(VPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
