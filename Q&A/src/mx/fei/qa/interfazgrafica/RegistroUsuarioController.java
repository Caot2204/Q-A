/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

/**
 * FXML Controller class
 *
 * @author beto
 */
public class RegistroUsuarioController implements Initializable {

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

    /**
     * Carga una imagen para la foto de perfil del usuario.
     */
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
                UtileriaInterfazUsuario.mostrarMensajeError("key.archivoInvalido",
                        "key.soloImagen", "key.escojaImagenCorrecta");
            }
        }
    }

    /**
     * Almacena el usuario registrado si los campos son válidos.
     */
    public void finalizarRegistro() {
        UsuarioCliente usuario;

        if (validarCampos()) {
            usuario = new UsuarioCliente();
            usuario.setNombre(nombreUsuario);
            usuario.setCorreo(correo);
            usuario.setContrasenia(contrasenia);
            if (archivoFotoPerfil != null) {
                try {
                    usuario.setFotoPerfil(Files.readAllBytes(archivoFotoPerfil.toPath()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistroUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                usuario.setFotoPerfil(null);
            }
            try {
                ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
                Registry registro = LocateRegistry.getRegistry(propiedadesCliente.getString("key.ipServidor1"));
                CuentaUsuarioInterface stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
                AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
                stub.guardarUsuario(usuario);
                administradorSesion.setSesionUsuario(stub.iniciarSesion(nombreUsuario, contrasenia));
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard",
                        "DashboardQA.fxml", textFieldNombreUsuario);

            } catch (IllegalArgumentException excepcion) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.errorGuardarDatos",
                        "key.errorCrearCuenta", "key.nombreUsuarioExistente");
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(RegistroUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Descarta los datos ingresados y despliega la pantalla Principal.
     */
    public void cancelarRegistro() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.principal",
                "Principal.fxml", textFieldNombreUsuario);
    }

    /**
     * Verifica que se han ingresados todos los campos y que estos sean válidos.
     *
     * @return True si los campos han sido ingresados y son válidos, False si no
     * es así
     */
    private boolean validarCampos() {
        boolean camposValidos = true;
        nombreUsuario = textFieldNombreUsuario.getText();
        correo = textFieldCorreo.getText();
        contrasenia = textFieldContrasenia.getText();

        try {
            UtileriaCadena.validarCadena(nombreUsuario, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos",
                        "key.modifiqueNombreUsuario",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos",
                        "key.modifiqueNombreUsuario", excepcion.getMessage());
            }
            textFieldNombreUsuario.requestFocus();
            camposValidos = false;
        }

        try {
            UtileriaCadena.validarCadena(correo, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos",
                        "key.modifiqueEmail",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos",
                        "key.modifiqueEmail", excepcion.getMessage());
            }
            textFieldCorreo.requestFocus();
            camposValidos = false;
        }

        try {
            UtileriaCadena.validarCadena(contrasenia, 1, 100);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos",
                        "key.modifiqueContrasenia",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 100));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.datosInvalidos",
                        "key.modifiqueContrasenia", excepcion.getMessage());
            }
            textFieldContrasenia.requestFocus();
            camposValidos = false;
        }

        return camposValidos;
    }

}
