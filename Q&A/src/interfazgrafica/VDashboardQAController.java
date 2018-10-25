/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazgrafica;

import comunicacion.interfaz.CuentaUsuarioInterface;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sesion.AdministradorSesionActual;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class VDashboardQAController implements Initializable {
    
    @FXML
    private Label labelNombreUsuario;
    
    private AdministradorSesionActual administradorSesion;
    private CuentaUsuarioInterface stub;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        administradorSesion = AdministradorSesionActual.obtenerAdministrador();
        try {
            Registry registro = LocateRegistry.getRegistry();
            stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        labelNombreUsuario.setText(administradorSesion.getSesionUsuario().getUsuario().getNombre());
    }
    
    public void editarCuentaUsuario() {
        
    }
    
    public void cerrarSesion() {
        try {
            if (stub.cerrarSesion(administradorSesion.getSesionUsuario().getUsuario().getNombre())) {
                administradorSesion.removerSesionActual();
                mostrarVentana("Principal", "VPrincipal.fxml");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            
            Stage escenarioActual = (Stage) labelNombreUsuario.getScene().getWindow();
            escenarioActual.close();
        } catch (IOException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
