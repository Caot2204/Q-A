/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URISyntaxException;
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
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaCadena;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * Controlador FXML de la IU VIniciarSesion
 *
 * @author Carlos Onorio
 */
public class InicioSesionController implements Initializable {

    @FXML
    private TextField textFieldNombreUsuario;

    @FXML
    private PasswordField passwordFieldContrasenia;

    private String usuario;
    private String contrasenia;
    
    private static final String KEY_DATOS_INVALIDOS = "key.datosInvalidos";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    /**
     * Permite al cliente iniciar sesión y desplegar el Dashboard de cuenta
     * iniciada
     */
    @FXML
    public void iniciarSesion() {
        if (validarCampos()) {
            try {
                ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
                Registry registro = LocateRegistry.getRegistry(propiedadesCliente.getString("key.ipServidor1"));
                CuentaUsuarioInterface stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
                AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
                administradorSesion.setSesionUsuario(stub.iniciarSesion(usuario, contrasenia));
                UtileriaInterfazUsuario.mostrarVentana(getClass(),
                        "key.dashboard", "DashboardQA.fxml", textFieldNombreUsuario);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(InicioSesionController.class.getName()).log(Level.SEVERE, null, ex);
                UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion",
                        "key.errorAlConectar", "key.problemaConexion");
            } catch (IllegalArgumentException excepcion) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.falloInicioSesion",
                        "key.credencialesInvalidas", "key.usuarioContraseniaIncorrectos");
            } catch (URISyntaxException ex) {
                Logger.getLogger(InicioSesionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Valida que los campos de la IU Iniciar sesión hayan sido ingresados
     * correctamente
     *
     * @return True si los campos fueron llenados correctamente, False si no fué
     * así
     */
    private boolean validarCampos() {
        boolean camposValidos = true;
        usuario = textFieldNombreUsuario.getText();
        contrasenia = passwordFieldContrasenia.getText();

        try {
            UtileriaCadena.validarCadena(usuario, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS, "key.modifiqueNombreUsuario", 
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS, 
                        "key.modifiqueNombreUsuario", excepcion.getMessage());
            }
            textFieldNombreUsuario.requestFocus();
            textFieldNombreUsuario.requestFocus();
            camposValidos = false;
        }

        try {
            UtileriaCadena.validarCadena(contrasenia, 1, 100);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS, "key.modifiqueContrasenia", 
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 100));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS, 
                        "key.modifiqueContrasenia", excepcion.getMessage());
            }
            passwordFieldContrasenia.requestFocus();
            camposValidos = false;
        }

        return camposValidos;
    }

}
