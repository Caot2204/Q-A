/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaCadena;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class PreferenciaJuegoController implements Initializable {

    @FXML
    private TextField textFieldNombreUsuario;

    @FXML
    private TextField textFieldCorreoElectronico;

    @FXML
    private TextField textFieldContrasenia;

    @FXML
    private ImageView imageViewFotoPerfil;

    @FXML
    private ComboBox comboBoxIdioma;

    private AdministradorSesionActual administradorSesion;
    private String correo;
    private String contrasenia;
    private File fotoPerfil;
    
    private static final String KEY_DATOS_INVALIDOS = "key.datosInvalidos";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        administradorSesion = AdministradorSesionActual.obtenerAdministrador();
        UsuarioCliente usuario = administradorSesion.getSesionUsuario().getUsuario();
        textFieldNombreUsuario.setText(usuario.getNombre());
        textFieldNombreUsuario.setDisable(true);
        textFieldCorreoElectronico.setText(usuario.getCorreo());
        textFieldContrasenia.setText(usuario.getContrasenia());
        if (usuario.getFotoPerfil() != null) {
            try {
                File imagenPerfil = new File("imagenPerfil");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenPerfil);
                fileOutputStream.write(usuario.getFotoPerfil());
                fileOutputStream.close();

                Image imagen = new Image("file:" + imagenPerfil.getAbsolutePath());
                imageViewFotoPerfil.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
        String espaniol = recursoIdioma.getString("key.espaniol");
        String ingles = recursoIdioma.getString("key.ingles");
        comboBoxIdioma.getItems().addAll(espaniol, ingles);

        ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
        String idiomaElegido = propiedadesCliente.getString("key.idioma");
        if (idiomaElegido.equals("Español")) {
            comboBoxIdioma.getSelectionModel().select(0);
        } else if (idiomaElegido.equals("English")) {
            comboBoxIdioma.getSelectionModel().select(1);
        }
    }

    /**
     * Carga una imagen para la foto de perfil del usuario.
     */
    public void subirFotoPerfil() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escoja una imagen");
        File archivoElegido = fileChooser.showOpenDialog(imageViewFotoPerfil.getScene().getWindow());
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                fotoPerfil = archivoElegido;
                Image fotoPerfilParaImageView = new Image("file:" + fotoPerfil.getAbsolutePath());
                imageViewFotoPerfil.setImage(fotoPerfilParaImageView);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.archivoInvalido",
                        "key.soloImagen", "key.escojaImagenCorrecta");
            }
        }
    }

    /**
     * Solicita al servidor almacenar los cambios de la cuenta de usuario y
     * registra el idioma seleccionado.
     */
    public void guardar() {
        if (validarCampos()) {
            UsuarioCliente usuarioActual = administradorSesion.getSesionUsuario().getUsuario();
            usuarioActual.setCorreo(correo);
            usuarioActual.setContrasenia(contrasenia);
            try {
                if (fotoPerfil != null) {
                    usuarioActual.setFotoPerfil(Files.readAllBytes(fotoPerfil.toPath()));
                }
            } catch (IOException ex) {
                Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
            }

            String nombreUsuario = usuarioActual.getNombre();
            try {
                ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
                Registry registro = LocateRegistry.getRegistry(propiedadesCliente.getString("key.ipServidor1"));
                CuentaUsuarioInterface stub = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
                stub.editarUsuario(usuarioActual);
                administradorSesion.removerSesionActual();
                administradorSesion.setSesionUsuario(stub.iniciarSesion(nombreUsuario, contrasenia));
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard",
                        "DashboardQA.fxml", textFieldNombreUsuario);
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
            }

            String idioma = comboBoxIdioma.getSelectionModel().getSelectedItem().toString();
            guardarPreferenciasDeIdioma(idioma);
        }
    }

    /**
     * Establece el idioma por defecto del cliente.
     *
     * @param idioma Idioma seleccionado por el usuario
     */
    private void guardarPreferenciasDeIdioma(String idioma) {
        Properties prop = new Properties();
        try {
            File propiedadesCliente = new File("src/mx/fei/qa/utileria/cliente.properties");
            InputStream in = new FileInputStream(propiedadesCliente);
            prop.load(in);
            prop.setProperty("key.idioma", idioma);
            prop.store(new FileOutputStream(propiedadesCliente), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreferenciaJuegoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Despliega la IU DashboardQA
     */
    public void cancelar() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard",
                "DashboardQA.fxml", comboBoxIdioma);
    }

    /**
     * Verifica que se han ingresados todos los campos y que estos sean válidos.
     *
     * @return True si los campos han sido ingresados y son válidos, False si no
     * es así
     */
    private boolean validarCampos() {
        boolean camposValidos = true;
        correo = textFieldCorreoElectronico.getText();
        contrasenia = textFieldContrasenia.getText();

        try {
            UtileriaCadena.validarCadena(correo, 1, 150);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueEmail",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueEmail", excepcion.getMessage());
            }
            textFieldCorreoElectronico.requestFocus();
            camposValidos = false;
        }

        try {
            UtileriaCadena.validarCadena(contrasenia, 1, 100);
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueContrasenia",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 100));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueContrasenia", excepcion.getMessage());
            }
            textFieldContrasenia.requestFocus();
            camposValidos = false;
        }

        return camposValidos;
    }

}
