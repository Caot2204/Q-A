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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sesion.AdministradorSesionActual;
import sesion.SesionUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class VIniciarSesionController implements Initializable {

    @FXML
    private TextField textFieldNombreUsuario;

    @FXML
    private TextField textFieldContrasenia;
    
    private String usuario;
    private String contrasenia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void iniciarSesion() {
        if (validarCampos()) {
            try {
                Registry registro = LocateRegistry.getRegistry();
                CuentaUsuarioInterface stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
                AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador(stub.iniciarSesion(usuario, contrasenia));
                mostrarVentana("Dashboard", "VDashboardQA.fxml");
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(VIniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException excepcion) {
                System.out.println(excepcion.getMessage());
            }
        }        
    }
    
    private boolean validarCampos() {
        boolean camposValidos = true;
        usuario = textFieldNombreUsuario.getText();
        contrasenia = textFieldContrasenia.getText();
        
        if (usuario == null || usuario.isEmpty() || usuario.length() >= 150) {
            textFieldNombreUsuario.requestFocus();
            camposValidos = false;
        } else if (contrasenia == null || contrasenia.isEmpty() || contrasenia.length() >= 150) {
            textFieldContrasenia.requestFocus();
            camposValidos = false;
        }
        
        return camposValidos;
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
            
            Stage escenarioActual = (Stage) textFieldContrasenia.getScene().getWindow();
            escenarioActual.close();
        } catch (IOException ex) {
            Logger.getLogger(VIniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
