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
import javafx.scene.control.Label;
import sesion.AdministradorSesionActual;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class VDashboardQAController implements Initializable {
    
    @FXML
    private Label labelNombreUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador(null);
        labelNombreUsuario.setText(administradorSesion.obtenerSesionUsuario().getUsuario().getNombre());
    }

}
