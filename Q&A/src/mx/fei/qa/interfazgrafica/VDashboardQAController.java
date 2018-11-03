/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.comunicacion.interfaz.CuestionarioInterface;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.interfazgrafica.adaptadortableview.AdaptadorCuestionario;
import mx.fei.qa.partida.AdministradorPartida;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class VDashboardQAController implements Initializable {

    @FXML
    private Label labelNombreUsuario;

    @FXML
    private ImageView imageViewFotoPerfil;

    @FXML
    private TableView tableViewCuestionarios;

    @FXML
    private TableColumn tableColumnNombreCuestionario;

    @FXML
    private TableColumn tableColumnCantidadPreguntas;

    @FXML
    private TableColumn tableColumnVecesJugado;

    @FXML
    private TableColumn tableColumnUltimoGanador;

    private AdministradorSesionActual administradorSesion;
    private CuentaUsuarioInterface stubCuentaUsuario;
    private CuestionarioInterface stubCuestionario;
    private ArrayList<CuestionarioCliente> cuestionariosRegistrados;
    private ObservableList<AdaptadorCuestionario> cuestionariosParaTableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        administradorSesion = AdministradorSesionActual.obtenerAdministrador();
        try {
            Registry registro = LocateRegistry.getRegistry();
            stubCuentaUsuario = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
            stubCuestionario = (CuestionarioInterface) registro.lookup("servidorCuestionarios");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
        }

        UsuarioCliente usuarioActual = administradorSesion.getSesionUsuario().getUsuario();
        Image fotoPerfil;

        labelNombreUsuario.setText(usuarioActual.getNombre());
        /*if (usuarioActual.getFotoPerfil() != null) {
            try {
                File file = File.createTempFile("fto", ".tmp");
                FileOutputStream stream = new FileOutputStream(file.getAbsolutePath());
                stream.write(usuarioActual.getFotoPerfil());
                stream.close();
                fotoPerfil = new Image("file:" + file.getAbsolutePath());
                imageViewFotoPerfil.setImage(fotoPerfil);
            } catch (IOException ex) {
                Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

        mostrarCuestionarios();
    }

    /**
     * Inicializa una nueva partida para un cuestionario para que el usuario la
     * monitore
     */
    public void jugarCuestionario() {
        int posicionCuestionarioSeleccionado = tableViewCuestionarios.getSelectionModel().getSelectedIndex();
        String nombreCuestionario = cuestionariosRegistrados.get(posicionCuestionarioSeleccionado).getNombre();
        AdministradorPartida administradorPartida = AdministradorPartida.obtenerInstancia();
        if (administradorPartida.iniciarPartida(nombreCuestionario)) {
            UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.invitacionParaJugar", "VInvitacionParaJugar.fxml", labelNombreUsuario);
        } else {
            UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.encabezadoError", "key.errorCrearPartida");
        }
    }

    public void editarCuentaUsuario() {

    }

    /**
     * Cierra la sesión del usuario actual, permitiendo a otro usuario iniciar
     * sesión o unirse a una partida mediante código de invitación
     */
    public void cerrarSesion() {
        try {
            if (stubCuentaUsuario.cerrarSesion(administradorSesion.getSesionUsuario().getUsuario().getNombre())) {
                administradorSesion.removerSesionActual();
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.principal", "VPrincipal.fxml", labelNombreUsuario);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion", "key.errorAlConectar", "key.problemaConexion");
        }
    }

    /**
     * Despliega en pantalla los cuestionarios que tiene registrado el usuario
     * que inicio sesión
     */
    private void mostrarCuestionarios() {
        tableColumnNombreCuestionario.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario, String>("nombreCuestionario"));
        tableColumnCantidadPreguntas.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario, String>("cantidadPreguntas"));
        tableColumnVecesJugado.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario, String>("vecesJugado"));
        tableColumnUltimoGanador.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario, String>("ultimoGanador"));
        cuestionariosParaTableView = FXCollections.observableArrayList();
        String nombreUsuario = administradorSesion.getSesionUsuario().getUsuario().getNombre();

        try {
            cuestionariosRegistrados = stubCuestionario.recuperarCuestionariosPorAutor(nombreUsuario);
            for (CuestionarioCliente cuestionario : cuestionariosRegistrados) {
                AdaptadorCuestionario adaptador = new AdaptadorCuestionario();
                adaptador.setNombreCuestionario(cuestionario.getNombre());
                adaptador.setCantidadPreguntas(cuestionario.getPreguntas().size());
                adaptador.setVecesJugado(cuestionario.getVecesJugado());
                adaptador.setUltimoGanador(cuestionario.getUltimoGanador());
                cuestionariosParaTableView.add(adaptador);
            }
            tableViewCuestionarios.setItems(cuestionariosParaTableView);
        } catch (RemoteException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion", "key.errorAlConectar", "key.problemaConexion");
        }
    }

}
