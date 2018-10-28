/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazgrafica;

import comunicacion.interfaz.CuentaUsuarioInterface;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sesion.AdministradorSesionActual;
import utileria.UtileriaCadena;
import utileria.UtileriaInterfazUsuario;

/**
 * Controlador FXML de la IU VIniciarSesion
 *
 * @author Carlos Onorio
 */
public class VIniciarSesionController implements Initializable {

    @FXML
    private TextField textFieldNombreUsuario;

    @FXML
    private PasswordField passwordFieldContrasenia;
    
    private String usuario;
    private String contrasenia;
    private ResourceBundle recurso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recurso = rb;
    }

    /**
     * Permite al cliente iniciar sesión y desplegar el Dashboard de cuenta iniciada
     */
    @FXML
    public void iniciarSesion() {
        if (validarCampos()) {
            try {
                Registry registro = LocateRegistry.getRegistry();
                CuentaUsuarioInterface stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
                AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
                administradorSesion.setSesionUsuario(stub.iniciarSesion(usuario, contrasenia));
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard", "VDashboardQA.fxml", textFieldNombreUsuario);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(VIniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
                UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion", "key.errorAlConectar", "key.problemaConexion");
            } catch (IllegalArgumentException excepcion) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.falloInicioSesion", "key.credencialesInvalidas", "key.usuarioContraseniaIncorrectos");
            }
        }        
    }
    
    /**
     * Valida que los campos de la IU Iniciar sesión hayan sido ingresados correctamente
     * 
     * @return True si los campos fueron llenados correctamente, False si no fué así
     */
    private boolean validarCampos() {
        boolean camposValidos = true;
        usuario = textFieldNombreUsuario.getText();
        contrasenia = passwordFieldContrasenia.getText();
        
        try {
            UtileriaCadena.validarCadena(usuario, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueNombreUsuario", UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueNombreUsuario", excepcion.getMessage());
            }
            textFieldNombreUsuario.requestFocus();
            textFieldNombreUsuario.requestFocus();
            camposValidos = false;            
        }
        
        try {
            UtileriaCadena.validarCadena(contrasenia, 1, 100);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueContrasenia", UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 100));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueContrasenia", excepcion.getMessage());
            }
            passwordFieldContrasenia.requestFocus();
            camposValidos = false;            
        }
        
        return camposValidos;
    }

}
