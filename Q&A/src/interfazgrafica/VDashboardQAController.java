/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfazgrafica;

import comunicacion.interfaz.CuentaUsuarioInterface;
import comunicacion.interfaz.CuestionarioInterface;
import dominio.actores.UsuarioCliente;
import dominio.cuestionario.CuestionarioCliente;
import interfazgrafica.adaptadortableview.AdaptadorCuestionario;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sesion.AdministradorSesionActual;
import utileria.UtileriaInterfazUsuario;

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

    public void editarCuentaUsuario() {

    }

    public void cerrarSesion() {
        try {
            if (stubCuentaUsuario.cerrarSesion(administradorSesion.getSesionUsuario().getUsuario().getNombre())) {
                administradorSesion.removerSesionActual();
                mostrarVentana("Principal", "VPrincipal.fxml");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            UtileriaInterfazUsuario.mostrarMensajeError("key.errorDeConexion", "key.errorAlConectar", "key.problemaConexion");
        }
    }

    /**
     * Cierra la IU actual y despliega la IU especificada en los parámetros
     *
     * @param titulo Titulo de la ventana
     * @param nombreFXML Nombre del archivo .fxml
     */
    public void mostrarVentana(String titulo, String nombreFXML) {
        Locale locale = Locale.getDefault();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(nombreFXML), ResourceBundle.getBundle("lang.lang", locale));
            Stage escenario = new Stage();
            Scene scene = new Scene(root);
            escenario.setScene(scene);
            escenario.show();

            Stage escenarioActual = (Stage) labelNombreUsuario.getScene().getWindow();
            escenarioActual.close();
        } catch (IOException ex) {
            Logger.getLogger(VDashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mostrarCuestionarios() {
        tableColumnNombreCuestionario.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario,String>("nombreCuestionario"));
        tableColumnCantidadPreguntas.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario,String>("cantidadPreguntas"));
        tableColumnVecesJugado.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario,String>("vecesJugado"));
        tableColumnUltimoGanador.setCellValueFactory(new PropertyValueFactory<AdaptadorCuestionario,String>("ultimoGanador"));
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
