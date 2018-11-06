/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import mx.fei.qa.comunicacion.interfaz.PartidaInterface;
import mx.fei.qa.dominio.actores.Jugador;
import mx.fei.qa.utileria.UtileriaCadena;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

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
        // force the field to be numeric only
        textFieldCodigoInvitacion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textFieldCodigoInvitacion.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * Posterior a ingresar el código de invitación y un nombre de jugador, se
     * une al jugador a la partida que coincida con el código de invitación
     * ingresado
     */
    public void unirseAPartida() {
        String codigoIngresado = textFieldCodigoInvitacion.getText();
        try {
            UtileriaCadena.validarCadena(codigoIngresado, 1, 5);
            short codigoInvitacion = Short.parseShort(codigoIngresado);
            try {
                ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
                Registry registro = LocateRegistry.getRegistry(propiedadesCliente.getString("key.ipServidor1"));
                PartidaInterface stubPartida = (PartidaInterface) registro.lookup("servidorPartidas");

                try {
                    String nombreJugador = solicitarNombreJugador();
                    UtileriaCadena.validarCadena(nombreJugador, 1, 150);
                    Jugador jugador = new Jugador();
                    jugador.setNombre(codigoIngresado);
                    jugador.setFotoPerfil(null);
                    if (stubPartida.unirAPartida(codigoInvitacion, jugador)) {
                        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.aJugarQA", "AJugarQA.fxml", textFieldCodigoInvitacion);
                    } else {
                        UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.encabezadoError", "key.noExistePartida");
                    }

                } catch (IllegalArgumentException excepcion) {
                    if (excepcion.getMessage().equals("Longitud invalida")) {
                        UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.datosInvalidos", UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
                    } else {
                        UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.datosInvalidos", excepcion.getMessage());
                    }
                }

            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(VPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals("Longitud invalida")) {
                UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.datosInvalidos", UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 5));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.datosInvalidos", excepcion.getMessage());
            }
            textFieldCodigoInvitacion.requestFocus();
        }

    }

    /**
     * Despliega un cuadro de texto para ingresar el nombre del jugador actual
     *
     * @return Nombre de jugador ingresado
     */
    private String solicitarNombreJugador() {
        String nombre = "";
        Locale locale = Locale.getDefault();
        ResourceBundle recurso = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
        TextInputDialog solicitudNombre = new TextInputDialog();
        solicitudNombre.setTitle(recurso.getString("key.mensajeDeSistema"));
        solicitudNombre.setHeaderText(recurso.getString("key.ingresarNombreJugador"));
        solicitudNombre.setContentText(recurso.getString("key.nombreJugador"));

        Optional<String> nombreIngresado = solicitudNombre.showAndWait();
        if (nombreIngresado.isPresent()) {
            nombre = nombreIngresado.get();
        }
        return nombre;
    }

    /**
     * Permite al usuario actual ingresar los datos de su cuenta de usuario para
     * iniciar sesión
     */
    public void iniciarSesion() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.iniciarSesion", "VIniciarSesion.fxml", textFieldCodigoInvitacion);
    }

    /**
     * Permite a un usuario registrarse en el sistema
     */
    public void registrarUsuario() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.registroUsuario", "VRgistrarUsuario.fxml", textFieldCodigoInvitacion);
    }

}
