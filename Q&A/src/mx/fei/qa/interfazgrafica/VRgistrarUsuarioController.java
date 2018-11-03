/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import java.io.File;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaCadena;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author beto
 */
public class VRgistrarUsuarioController implements Initializable {

    @FXML
    private ImageView imageViewFotoPerfil;

    @FXML
    private TextField textFieldNombreUsuario;

    @FXML
    private TextField textFieldCorreo;

    @FXML
    private TextField textFieldContrasenia;

    private String nombreUsuario;
    private String correo;
    private String contrasenia;
    private File archivoFotoPerfil;
    private Image fotoPerfilParaImageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void subirFotoPerfil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escoja una imagen");
        File archivoElegido = fileChooser.showOpenDialog(imageViewFotoPerfil.getScene().getWindow());;
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                archivoFotoPerfil = archivoElegido;
                fotoPerfilParaImageView = new Image("file:" + archivoFotoPerfil.getAbsolutePath());
                imageViewFotoPerfil.setImage(fotoPerfilParaImageView);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.archivoInvalido", "key.soloImagen", "key.escojaImagenCorrecta");
            }
        }
    }

    public void finalizarRegistro() {
        UsuarioCliente usuario;

        if (validarCampos()) {
            usuario = new UsuarioCliente();
            usuario.setNombre(nombreUsuario);
            usuario.setCorreo(correo);
            usuario.setContrasenia(contrasenia);
            if (archivoFotoPerfil != null) {
                usuario.setFotoPerfil(archivoFotoPerfil.getAbsolutePath().getBytes());
            } else {
                usuario.setFotoPerfil(null);
            }
            try {

                Registry registro = LocateRegistry.getRegistry();
                CuentaUsuarioInterface stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
                stub.guardarUsuario(usuario);

                AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
                administradorSesion.setSesionUsuario(stub.iniciarSesion(nombreUsuario, contrasenia));
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard", "VDashboardQA.fxml", textFieldNombreUsuario);

            } catch (IllegalArgumentException excepcion) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.errorGuardarDatos", "key.errorCrearCuenta", "key.nombreUsuarioExistente");
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(VRgistrarUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void cancelarRegistro() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.principal", "VPrincipal.fxml", textFieldNombreUsuario);
    }

    private boolean validarCampos() {
        boolean camposValidos = true;
        nombreUsuario = textFieldNombreUsuario.getText();
        correo = textFieldCorreo.getText();
        contrasenia = textFieldContrasenia.getText();

        try {
            UtileriaCadena.validarCadena(nombreUsuario, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueNombreUsuario", UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueNombreUsuario", excepcion.getMessage());
            }
            textFieldNombreUsuario.requestFocus();
            camposValidos = false;
        }

        try {
            UtileriaCadena.validarCadena(correo, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueEmail", UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos", "key.modifiqueEmail", excepcion.getMessage());
            }
            textFieldCorreo.requestFocus();
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
            textFieldContrasenia.requestFocus();
            camposValidos = false;
        }

        return camposValidos;
    }

}
