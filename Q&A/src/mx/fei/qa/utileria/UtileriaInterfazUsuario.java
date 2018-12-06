/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.utileria;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 * Proporciona un conjunto de métodos comúnes para que el sistema se comunique
 * con el usuario
 *
 * @version 1.0 26 Oct 2018
 * @author Carlos Onorio
 */
public class UtileriaInterfazUsuario {

    private static ResourceBundle recurso;
    private static final String KEY_TAMANIO_VALIDO = "key.tamanioValidoCampo";
    
    /**
     * Notifica que es una clase de utilidades y no puede ser instanciada.
     */
    private UtileriaInterfazUsuario() {
        throw new IllegalStateException("Clase de utilidades para IU");
    }

    /**
     * Cierra la IU actual y despliega la IU especificada en los parámetros.
     *
     * @param clase Clase actual desde donde se manda a llamar este método
     * @param llaveTitulo Llave para archivo de propiedades que contiene el
     * titulo de la ventana en el idioma actual
     * @param nombreFXML Nombre del archivo .fxml
     * @param elementoInterfaz Elemento IU de JavaFX para hacer referencia a la
     * ventana actual
     */
    public static void mostrarVentana(Class clase, String llaveTitulo, String nombreFXML, Control elementoInterfaz) {
        recurso = recuperarRecursoIdiomaCliente();
        String titulo = recurso.getString(llaveTitulo);
        try {
            Parent root = FXMLLoader.load(clase.getResource(nombreFXML), recurso);
            Stage escenario = new Stage();
            Scene scene = new Scene(root);
            escenario.setScene(scene);
            escenario.setTitle(titulo);
            escenario.show();

            if (elementoInterfaz != null) {
                Stage escenarioActual = (Stage) elementoInterfaz.getScene().getWindow();
                escenarioActual.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(UtileriaInterfazUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recupera los recursos del idioma que el usuario eligió para su cliente.
     *
     * @return ResourceBundle con los recursos del idioma definido por el
     * usuario
     */
    public static ResourceBundle recuperarRecursoIdiomaCliente() {
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
        String idiomaJuego = propiedadesCliente.getString("key.idioma");
        Locale locale = null;
        if (idiomaJuego.equals("Español")) {
            locale = new Locale("es", "MX");
        } else if (idiomaJuego.equals("English")) {
            locale = new Locale("en", "US");
        }
        return ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
    }

    /**
     * Despliega la IU especificada en los parámetros.
     *
     * @param clase Clase actual desde donde se manda a llamar este método
     * @param llaveTitulo Llave para archivo de propiedades que contiene el
     * titulo de la ventana en el idioma actual
     * @param nombreFXML Nombre del archivo .fxml
     */
    public static void mostrarVentanaSinCerrarActual(Class clase, String llaveTitulo, String nombreFXML) {
        recurso = recuperarRecursoIdiomaCliente();
        String titulo = recurso.getString(llaveTitulo);
        try {
            Parent root = FXMLLoader.load(clase.getResource(nombreFXML), recurso);
            Stage escenario = new Stage();
            Scene scene = new Scene(root);
            escenario.setScene(scene);
            escenario.setTitle(titulo);
            escenario.show();
        } catch (IOException ex) {
            Logger.getLogger(UtileriaInterfazUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Despliega un mensaje de error al usuario.
     *
     * @param llaveTitulo Llave para archivo de propiedades que contiene el
     * Titulo del mensaje en el idioma actual
     * @param llaveEncabezado Llave para archivo de propiedades que contiene el
     * Encabezado de la ventana del mensaje
     * @param llaveMensaje Llave para archivo de propiedades que contiene el
     * Mensaje que se desea mostrar al usuario
     */
    public static void mostrarMensajeError(String llaveTitulo, String llaveEncabezado, String llaveMensaje) {
        String titulo = recurso.getString(llaveTitulo);
        String encabezado = recurso.getString(llaveEncabezado);
        String mensaje;
        if (llaveMensaje.contains(recurso.getString(KEY_TAMANIO_VALIDO))) {
            mensaje = llaveMensaje;
        } else {
            mensaje = recurso.getString(llaveMensaje);
        }

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Despliega un mensaje de éxito al usuario para notificar que alguna acción
     * se realizó correctamente en el sistema.
     *
     * @param llaveTitulo Llave para archivo de propiedades que contiene el
     * Titulo del mensaje en el idioma actual
     * @param llaveEncabezado Llave para archivo de propiedades que contiene el
     * Encabezado de la ventana del mensaje
     * @param llaveMensaje Llave para archivo de propiedades que contiene el
     * Mensaje que se desea mostrar al usuario
     */
    public static void mostrarMensajeExito(String llaveTitulo, String llaveEncabezado, String llaveMensaje) {
        String titulo = recurso.getString(llaveTitulo);
        String encabezado = recurso.getString(llaveEncabezado);
        String mensaje;
        if (llaveMensaje.contains(recurso.getString(KEY_TAMANIO_VALIDO))) {
            mensaje = llaveMensaje;
        } else {
            mensaje = recurso.getString(llaveMensaje);
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Genera un mensaje de error para el usuario, notificando que la cadena
     * esta dentro del rango permitido de caracteres.
     *
     * @param minimo Mínimo de caracteres
     * @param maximo Máximo de caracteres
     * @return Cadena con el mensaje de error generado
     */
    public static String generarCadenaRangoInvalidoParaMensaje(int minimo, int maximo) {
        String cadena;
        String primerParte = recurso.getString(KEY_TAMANIO_VALIDO);
        String segundaParte = recurso.getString("key.hasta");
        String terceraParte = recurso.getString("key.caracteres");

        cadena = primerParte + " " + Integer.toString(minimo) + " " + segundaParte + " "
                + Integer.toString(maximo) + " " + terceraParte;

        return cadena;
    }

    /**
     * Cierra la ventana en donde se encuentre el elementoInterfaz.
     *
     * @param elementoInterfaz Elemento JavaFX
     */
    public static void cerrarVentanaMedianteElementoFX(Control elementoInterfaz) {
        Stage escenarioActual = (Stage) elementoInterfaz.getScene().getWindow();
        escenarioActual.close();
    }

}
