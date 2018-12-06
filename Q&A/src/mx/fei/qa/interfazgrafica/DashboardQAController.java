/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.comunicacion.interfaz.CuestionarioInterface;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.interfazgrafica.adaptadortableview.AdaptadorCuestionario;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class DashboardQAController implements Initializable {

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
    private List<CuestionarioCliente> cuestionariosRegistrados;
    
    private static final String KEY_MENSAJE_SISTEMA = "key.mensajeDeSistema";
    private static final String KEY_ERROR = "key.error";
    private static final String KEY_SELECCIONE_CUESTIONARIO = "key.seleccioneUnCuestionario";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        administradorSesion = AdministradorSesionActual.obtenerAdministrador();
        try {
            ResourceBundle propiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
            Registry registro = LocateRegistry.getRegistry(propiedadesCliente.getString("key.ipServidor1"));
            stubCuentaUsuario = (CuentaUsuarioInterface) registro.lookup("servidorCuentasUsuario");
            stubCuestionario = (CuestionarioInterface) registro.lookup("servidorCuestionarios");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
        }

        UsuarioCliente usuarioActual = administradorSesion.getSesionUsuario().getUsuario();

        labelNombreUsuario.setText(usuarioActual.getNombre());
        labelNombreUsuario.setAlignment(Pos.CENTER_RIGHT);
        if (usuarioActual.getFotoPerfil() != null) {
            try {
                File imagenPerfil = new File("imagenPerfil");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenPerfil);
                fileOutputStream.write(usuarioActual.getFotoPerfil());
                fileOutputStream.close();

                Image imagen = new Image("file:" + imagenPerfil.getAbsolutePath());
                imageViewFotoPerfil.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        mostrarCuestionarios();
    }

    /**
     * Muestra la ventana para registrar un nuevo cuestionario.
     */
    public void crearCuestionario() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.cuestionario",
                "RegistroCuestionario.fxml", labelNombreUsuario);
    }

    /**
     * Muestra la ventana para editar el cuestionario seleccionado.
     */
    public void editarCuestionario() {
        int cuestionarioSeleccionado = tableViewCuestionarios.getSelectionModel().getSelectedIndex();
        if (cuestionarioSeleccionado >= 0) {
            ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource("RegistroCuestionario.fxml"), recursoIdioma);
            try {
                Parent padre = cargadorFXML.load();
                RegistroCuestionarioController pantallaCuestionario = cargadorFXML.getController();
                pantallaCuestionario.establecerCuestionarioAEditar(cuestionariosRegistrados.get(cuestionarioSeleccionado));

                Stage escenario = new Stage();
                escenario.setScene(new Scene(padre));
                escenario.setTitle(recursoIdioma.getString("key.cuestionario"));
                escenario.setResizable(false);
                escenario.show();

                Stage escenarioActual = (Stage) tableViewCuestionarios.getScene().getWindow();
                escenarioActual.close();
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema",
                    "key.error", "key.seleccioneUnCuestionario");
        }

    }

    /**
     * Solicita al servidor eliminar el cuestionario seleccionado.
     */
    public void eliminarCuestionario() {
        int cuestionarioSeleccionado = tableViewCuestionarios.getSelectionModel().getSelectedIndex();
        if (cuestionarioSeleccionado >= 0) {
            String autor = administradorSesion.getNombreUsuarioActual();
            String nombreCuestionario = cuestionariosRegistrados.get(cuestionarioSeleccionado).getNombre();
            try {
                if (stubCuestionario.eliminarCuestionario(autor, nombreCuestionario)) {
                    mostrarCuestionarios();
                    UtileriaInterfazUsuario.mostrarMensajeExito(KEY_MENSAJE_SISTEMA,
                            "key.exitoEliminado", "key.cuestionarioEliminado");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                    KEY_ERROR, KEY_SELECCIONE_CUESTIONARIO);
        }
    }

    /**
     * Inicializa una nueva partida para un cuestionario para que el usuario la
     * monitoree.
     */
    public void jugarCuestionario() {
        int cuestionarioSeleccionado = tableViewCuestionarios.getSelectionModel().getSelectedIndex();
        if (cuestionarioSeleccionado >= 0) {
            String nombreCuestionario = cuestionariosRegistrados.get(cuestionarioSeleccionado).getNombre();
            MonitorPartida monitorPartida = MonitorPartida.obtenerInstancia();
            if (monitorPartida.iniciarPartida(nombreCuestionario)) {
                UtileriaInterfazUsuario.mostrarVentana(getClass(),
                        "key.invitacionParaJugar", "InvitacionParaJugar.fxml",
                        labelNombreUsuario);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                        "key.encabezadoError", "key.errorCrearPartida");
            }
        } else {
            UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                    KEY_ERROR, KEY_SELECCIONE_CUESTIONARIO);
        }
    }

    /**
     * Despliega la IU para editar los datos cuenta del usuario o el idioma del
     * cliente.
     */
    public void editarPreferencias() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.editarPreferencias",
                "PreferenciaJuego.fxml", labelNombreUsuario);
    }

    /**
     * Cierra la sesión del usuario actual, permitiendo a otro usuario iniciar
     * sesión o unirse a una partida mediante código de invitación
     */
    public void cerrarSesion() {
        try {
            if (stubCuentaUsuario.cerrarSesion(administradorSesion.getSesionUsuario().getUsuario().getNombre())) {
                administradorSesion.removerSesionActual();
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.principal",
                        "Principal.fxml", labelNombreUsuario);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion",
                    "key.errorAlConectar", "key.problemaConexion");
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
        ObservableList<Object> cuestionariosParaTableView = FXCollections.observableArrayList();
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
            Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion", "key.errorAlConectar", "key.problemaConexion");
        }
    }

}
