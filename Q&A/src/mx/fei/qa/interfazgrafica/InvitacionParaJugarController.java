/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.sesion.UsuarioConectadoAServidor;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

/**
 * Controlador del archivo FXML InvitacionParaJugar.fxml
 *
 * @version 1.0 10 Nov 2018
 * @author Carlos Onorio
 */
public class InvitacionParaJugarController implements Initializable {

    @FXML
    private TextField textFieldCorreos;

    @FXML
    private ListView listViewUsuariosConectados;

    @FXML
    private ListView listViewUsuariosAInvitar;

    private List<String> correos;
    private List<String> usuariosAInvitar;
    private ObservableList<UsuarioConectadoAServidor> usuariosConectados;
    private ObservableList<UsuarioConectadoAServidor> usuariosElegidos;
    private AdministradorSesionActual administradorSesion;
    private MonitorPartida monitor;
    private List<UsuarioConectadoAServidor> usuariosRecibidos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monitor = MonitorPartida.obtenerInstancia();
        administradorSesion = AdministradorSesionActual.obtenerAdministrador();
        usuariosAInvitar = new ArrayList<>();
        usuariosElegidos = FXCollections.observableArrayList();
        listViewUsuariosAInvitar.setItems(usuariosElegidos);
        refrescarUsuariosConectados();
    }

    /**
     * Envia las invitaciones para jugar a los jugadores conectados que el
     * monitor selecciono en la IU y a los correos electrónicos ingresados.
     */
    public void enviarInvitaciones() {
        recuperarCorreosIngresados();
        monitor.enviarInvitaciones(correos, usuariosAInvitar);
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.aJugarQA",
                "ListaJugadoresConectados.fxml", textFieldCorreos);
    }

    /**
     * Obtiene los correos ingresados por el monitor de la partida.
     */
    private void recuperarCorreosIngresados() {
        correos = new ArrayList<>();
        String campoCorreos = textFieldCorreos.getText();
        if (campoCorreos != null && !campoCorreos.isEmpty()) {
            String[] correosIngresados = campoCorreos.split(",");
            correos.addAll(Arrays.asList(correosIngresados));
        }
    }

    /**
     * Muestra en la sección Usuarios para invitar el usuario seleccionado.
     */
    public void agregarUsuarioParaInvitar() {
        int usuario = listViewUsuariosConectados.getSelectionModel().getSelectedIndex();
        if (usuario >= 0) {
            usuariosAInvitar.add(usuariosRecibidos.get(usuario).getIdSocket());
            usuariosElegidos.add(usuariosRecibidos.get(usuario));
            listViewUsuariosAInvitar.refresh();
        }
    }

    /**
     * Quita de la sección Usuarios para invitar el usuario seleccionado.
     */
    public void quitarUsuarioParaInvitar() {
        int usuario = listViewUsuariosAInvitar.getSelectionModel().getSelectedIndex();
        if (usuario >= 0) {
            usuariosAInvitar.remove(usuario);
            usuariosElegidos.remove(usuario);
            listViewUsuariosAInvitar.refresh();
        }
    }

    /**
     * Refresca la pantalla con los usuarios conectados actualmente.
     */
    private void refrescarUsuariosConectados() {
        Timer temporizador = FxTimer.runPeriodically(Duration.ofMillis(1000), () -> {
            usuariosRecibidos = administradorSesion.obtenerUsuariosConectados();
            if (usuariosRecibidos != null) {
                usuariosConectados = FXCollections.observableArrayList(usuariosRecibidos);
            }
            listViewUsuariosConectados.setItems(usuariosConectados);
        });
    }

}
