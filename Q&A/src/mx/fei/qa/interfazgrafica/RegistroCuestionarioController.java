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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.manejocuestionario.AdministradorCuestionario;
import mx.fei.qa.utileria.UtileriaCadena;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author beto
 */
public class RegistroCuestionarioController implements Initializable {

    @FXML
    private Label labelPreguntasRegistradas;

    @FXML
    private ListView listViewPreguntasRegistradas;

    @FXML
    private TextField textFieldNombreCuestionario;

    @FXML
    private TextField textFieldPregunta;

    @FXML
    private TextField textFieldRespuestaA;

    @FXML
    private TextField textFieldRespuestaB;

    @FXML
    private TextField textFieldRespuestaC;

    @FXML
    private TextField textFieldRespuestaD;

    @FXML
    private ImageView imageViewPregunta;

    @FXML
    private ImageView imageViewRespuestaA;

    @FXML
    private ImageView imageViewRespuestaB;

    @FXML
    private ImageView imageViewRespuestaC;

    @FXML
    private ImageView imageViewRespuestaD;

    @FXML
    private RadioButton radioButtonRespuestaA;

    @FXML
    private RadioButton radioButtonRespuestaB;

    @FXML
    private RadioButton radioButtonRespuestaC;

    @FXML
    private RadioButton radioButtonRespuestaD;

    private List<RespuestaCliente> respuestas;
    private int preguntaEnEdicion;
    private AdministradorCuestionario administradorCuestionario;
    private File archivoImagenPregunta;
    private File archivoImagenRespuestaA;
    private File archivoImagenRespuestaB;
    private File archivoImagenRespuestaC;
    private File archivoImagenRespuestaD;
    private ResourceBundle recursoIdioma;
    
    private static final String KEY_MENSAJE_SISTEMA = "key.mensajeDeSistema";
    private static final String KEY_DATOS_INVALIDOS = "key.datosInvalidos";
    private static final String KEY_SELECCIONAR_ARCHIVO = "key.seleccionarOtroArchivo";
    private static final String KEY_ARCHIVO_INVALIDO = "key.archivoInvalido";
    private static final String LONGITUD_INVALIDA = "Longitud invalida";
    private static final String FILE = "file:";

    /**
     * Inicializa el controller
     *
     * @param url Url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recursoIdioma = rb;
        administradorCuestionario = AdministradorCuestionario.obtenerInstancia();
        if (textFieldNombreCuestionario.getText().isEmpty()) {
            administradorCuestionario.crearNuevoCuestionario();
        }
        labelPreguntasRegistradas.setAlignment(Pos.CENTER);
        preguntaEnEdicion = 0;
        mostrarPreguntasRegistradas();
    }

    /**
     * Almacena el cuestionario ingresado asociandolo a la cuenta del usuario.
     */
    public void guardarCuestionario() {
        establecerDatosGeneralesCuestionario();
        if (administradorCuestionario.guardarCuestionario()) {
            UtileriaInterfazUsuario.mostrarMensajeExito(KEY_MENSAJE_SISTEMA,
                    "key.encabezadoGuardado", "key.cuestionarioGuardado");
            UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard",
                    "DashboardQA.fxml", textFieldPregunta);
        }
    }

    /**
     * Muestra en la IU los datos del cuestionario a editar, iniciando con la
     * primera pregunta registrada.
     *
     * @param cuestionario Cuestionario a editar
     */
    public void establecerCuestionarioAEditar(CuestionarioCliente cuestionario) {
        administradorCuestionario = AdministradorCuestionario.obtenerInstancia();
        administradorCuestionario.editarCuestionario(cuestionario);

        textFieldNombreCuestionario.setText(cuestionario.getNombre());
        textFieldNombreCuestionario.setDisable(true);
        if (cuestionario.getPreguntas().size() >= 1) {
            establecerDatosPregunta(cuestionario.getPreguntas().get(0));
        }

        mostrarPreguntasRegistradas();
    }

    /**
     * Muestra en la IU los datos de la pregunta seleccionada con sus
     * respectivas respuestas.
     *
     * @param pregunta Pregunta a mostrar en la IU
     */
    public void establecerDatosPregunta(PreguntaCliente pregunta) {
        preguntaEnEdicion = pregunta.getNumero();
        textFieldPregunta.setText(pregunta.getDescripcion());
        if (pregunta.getImagen() != null) {
            try {
                archivoImagenPregunta = new File("imagenPregunta");
                FileOutputStream fileOutputStream = new FileOutputStream(archivoImagenPregunta);
                fileOutputStream.write(pregunta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + archivoImagenPregunta.getAbsolutePath());
                imageViewPregunta.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            archivoImagenPregunta = null;
            imageViewPregunta.setImage(null);
        }
        establecerDatosRespuestaA(pregunta.getRespuestas().get(0));
        establecerDatosRespuestaB(pregunta.getRespuestas().get(1));
        establecerDatosRespuestaC(pregunta.getRespuestas().get(2));
        establecerDatosRespuestaD(pregunta.getRespuestas().get(3));
    }

    /**
     * Muestra en la IU los datos de la respuesta A de la pregunta actual.
     *
     * @param respuesta RespuestaCliente a desplegar en la sección de la
     * respuesta A
     */
    private void establecerDatosRespuestaA(RespuestaCliente respuesta) {
        if (respuesta.getDescripcion() != null) {
            textFieldRespuestaA.setText(respuesta.getDescripcion());
        } else {
            textFieldRespuestaA.clear();
        }

        if (respuesta.getImagen() != null) {
            try {
                archivoImagenRespuestaA = new File("imagenA");
                FileOutputStream fileOutputStream = new FileOutputStream(archivoImagenRespuestaA);
                fileOutputStream.write(respuesta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + archivoImagenRespuestaA.getAbsolutePath());
                imageViewRespuestaA.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            archivoImagenRespuestaA = null;
            imageViewRespuestaA.setImage(null);
        }

        if (respuesta.isEsCorrecta()) {
            radioButtonRespuestaA.setSelected(true);
        } else {
            radioButtonRespuestaA.setSelected(false);
        }
    }

    /**
     * Muestra en la IU los datos de la respuesta B de la pregunta actual.
     *
     * @param respuesta RespuestaCliente a desplegar en la sección de la
     * respuesta B
     */
    private void establecerDatosRespuestaB(RespuestaCliente respuesta) {
        if (respuesta.getDescripcion() != null) {
            textFieldRespuestaB.setText(respuesta.getDescripcion());
        } else {
            textFieldRespuestaB.clear();
        }

        if (respuesta.getImagen() != null) {
            try {
                archivoImagenRespuestaB = new File("imagenB");
                FileOutputStream fileOutputStream = new FileOutputStream(archivoImagenRespuestaB);
                fileOutputStream.write(respuesta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + archivoImagenRespuestaB.getAbsolutePath());
                imageViewRespuestaB.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            archivoImagenRespuestaB = null;
            imageViewRespuestaB.setImage(null);
        }

        if (respuesta.isEsCorrecta()) {
            radioButtonRespuestaB.setSelected(true);
        } else {
            radioButtonRespuestaB.setSelected(false);
        }
    }

    /**
     * Muestra en la IU los datos de la respuesta C de la pregunta actual.
     *
     * @param respuesta RespuestaCliente a desplegar en la sección de la
     * respuesta C
     */
    private void establecerDatosRespuestaC(RespuestaCliente respuesta) {
        if (respuesta.getDescripcion() != null) {
            textFieldRespuestaC.setText(respuesta.getDescripcion());
        } else {
            textFieldRespuestaC.clear();
        }

        if (respuesta.getImagen() != null) {
            try {
                archivoImagenRespuestaC = new File("imagenC");
                FileOutputStream fileOutputStream = new FileOutputStream(archivoImagenRespuestaC);
                fileOutputStream.write(respuesta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + archivoImagenRespuestaC.getAbsolutePath());
                imageViewRespuestaC.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            archivoImagenRespuestaC = null;
            imageViewRespuestaC.setImage(null);
        }

        if (respuesta.isEsCorrecta()) {
            radioButtonRespuestaC.setSelected(true);
        } else {
            radioButtonRespuestaC.setSelected(false);
        }
    }

    /**
     * Muestra en la IU los datos de la respuesta D de la pregunta actual.
     *
     * @param respuesta RespuestaCliente a desplegar en la sección de la
     * respuesta D
     */
    private void establecerDatosRespuestaD(RespuestaCliente respuesta) {
        if (respuesta.getDescripcion() != null) {
            textFieldRespuestaD.setText(respuesta.getDescripcion());
        } else {
            textFieldRespuestaD.clear();
        }

        if (respuesta.getImagen() != null) {
            try {
                archivoImagenRespuestaD = new File("imagenD");
                FileOutputStream fileOutputStream = new FileOutputStream(archivoImagenRespuestaD);
                fileOutputStream.write(respuesta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + archivoImagenRespuestaD.getAbsolutePath());
                imageViewRespuestaD.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardQAController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            archivoImagenRespuestaD = null;
            imageViewRespuestaD.setImage(null);
        }

        if (respuesta.isEsCorrecta()) {
            radioButtonRespuestaD.setSelected(true);
        } else {
            radioButtonRespuestaD.setSelected(false);
        }
    }

    /**
     * Muestra en la IU la pregunta del cuestionario seleccionada por el
     * usuario.
     */
    public void editarPregunta() {
        int preguntaSeleccionada = listViewPreguntasRegistradas.getSelectionModel().getSelectedIndex();
        if (preguntaSeleccionada >= 0) {
            limpiarCampos();
            establecerDatosPregunta(administradorCuestionario.getPreguntasCuestionario().get(preguntaSeleccionada));
        }
    }

    /**
     * Elimina la pregunta actual del cuestionario.
     */
    public void eliminarPregunta() {
        int preguntaSeleccionada = listViewPreguntasRegistradas.getSelectionModel().getSelectedIndex();
        administradorCuestionario.eliminarPregunta(preguntaSeleccionada);
        limpiarCampos();
        mostrarPreguntasRegistradas();
    }

    /**
     * Despliega la pantalla dashboard.
     */
    public void cancelar() {
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.dashboard", 
                "DashboardQA.fxml", textFieldPregunta);
    }

    /**
     * Almacena temporalmente la pregunta ingresada en la IU.
     */
    public void guardarPregunta() {
        PreguntaCliente pregunta = obtenerPreguntaIngresada();
        if (pregunta != null) {
            if (preguntaEnEdicion > 0) {
                pregunta.setNumero(preguntaEnEdicion);
                administradorCuestionario.actualizarPregunta(pregunta);
                preguntaEnEdicion = 0;
            } else if (preguntaEnEdicion == 0) {
                administradorCuestionario.agregarPregunta(pregunta);
            }
            limpiarCampos();
            mostrarPreguntasRegistradas();
        }
    }

    /**
     * Establece el nombre y autor del cuestionario.
     */
    private void establecerDatosGeneralesCuestionario() {
        String nombreCuestionario = textFieldNombreCuestionario.getText();
        try {
            if (UtileriaCadena.validarCadena(nombreCuestionario, 1, 150)) {
                administradorCuestionario.establecerDatosGenerales(nombreCuestionario);
            }
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals(LONGITUD_INVALIDA)) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueNombreCuestionario",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 150));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueNombreCuestionario", excepcion.getMessage());
            }
            textFieldNombreCuestionario.requestFocus();
        }

    }

    /**
     * Recupera la pregunta ingresada junto con sus respuestas.
     *
     * @return PreguntaCliente
     */
    private PreguntaCliente obtenerPreguntaIngresada() {
        PreguntaCliente pregunta = null;
        String decripcion = textFieldPregunta.getText();
        if (obtenerRespuestasIngresadas()) {
            pregunta = new PreguntaCliente();
            pregunta.setRespuestas(respuestas);
            try {
                if (archivoImagenPregunta != null) {
                    pregunta.setImagen(Files.readAllBytes(archivoImagenPregunta.toPath()));
                }
                if (UtileriaCadena.validarCadena(decripcion, 1, 300)) {
                    pregunta.setDescripcion(decripcion);
                }
            } catch (IllegalArgumentException excepcion) {
                if (excepcion.getMessage().equals(LONGITUD_INVALIDA)) {
                    UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                            "key.modifiquePregunta",
                            UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 300));
                } else {
                    UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                            "key.modifiquePregunta", excepcion.getMessage());
                }
                textFieldPregunta.requestFocus();
            } catch (IOException ex) {
                Logger.getLogger(RegistroCuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pregunta;
    }

    /**
     *
     * @return true si se recuperaron las respuestas, false si no fué así
     */
    private boolean obtenerRespuestasIngresadas() {
        boolean respuestasRecuperadas = false;
        respuestas = new ArrayList();
        if (obtenerRespuestaA() && obtenerRespuestaB()
                && obtenerRespuestaC() && obtenerRespuestaD()) {
            respuestasRecuperadas = true;
        }
        return respuestasRecuperadas;
    }

    /**
     *
     * @return true si la respuesta A fué ingresada correctamente, false si no
     * es así
     */
    private boolean obtenerRespuestaA() {
        boolean valida = false;
        String descripcion = textFieldRespuestaA.getText();
        RespuestaCliente respuestaA = new RespuestaCliente();
        respuestaA.setLetra('A');
        respuestaA.setEsCorrecta(radioButtonRespuestaA.isSelected());
        try {
            if (archivoImagenRespuestaA != null) {
                respuestaA.setImagen(Files.readAllBytes(archivoImagenRespuestaA.toPath()));
            } else if (UtileriaCadena.validarCadena(descripcion, 1, 300)) {
                respuestaA.setDescripcion(descripcion);
            }
            respuestas.add(respuestaA);
            valida = true;
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals(LONGITUD_INVALIDA)) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaA",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 300));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaA", excepcion.getMessage());
            }
            textFieldRespuestaA.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(RegistroCuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    /**
     *
     * @return true si la respuesta B fué ingresada correctamente, false si no
     * es así
     */
    private boolean obtenerRespuestaB() {
        boolean valida = false;
        String descripcion = textFieldRespuestaB.getText();
        RespuestaCliente respuestaB = new RespuestaCliente();
        respuestaB.setLetra('B');
        respuestaB.setEsCorrecta(radioButtonRespuestaB.isSelected());
        try {
            if (archivoImagenRespuestaB != null) {
                respuestaB.setImagen(Files.readAllBytes(archivoImagenRespuestaB.toPath()));
            } else if (UtileriaCadena.validarCadena(descripcion, 1, 300)) {
                respuestaB.setDescripcion(descripcion);
            }
            respuestas.add(respuestaB);
            valida = true;
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals(LONGITUD_INVALIDA)) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaB",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 300));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaB", excepcion.getMessage());
            }
            textFieldRespuestaB.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(RegistroCuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    /**
     *
     * @return true si la respuesta C fué ingresada correctamente, false si no
     * es así
     */
    private boolean obtenerRespuestaC() {
        boolean valida = false;
        String descripcion = textFieldRespuestaC.getText();
        RespuestaCliente respuestaC = new RespuestaCliente();
        respuestaC.setLetra('C');
        respuestaC.setEsCorrecta(radioButtonRespuestaC.isSelected());
        try {
            if (archivoImagenRespuestaC != null) {
                respuestaC.setImagen(Files.readAllBytes(archivoImagenRespuestaC.toPath()));
            } else if (UtileriaCadena.validarCadena(descripcion, 1, 300)) {
                respuestaC.setDescripcion(descripcion);
            }
            respuestas.add(respuestaC);
            valida = true;
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals(LONGITUD_INVALIDA)) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaC",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 300));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaC", excepcion.getMessage());
            }
            textFieldRespuestaC.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(RegistroCuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    /**
     *
     * @return true si la respuesta D fué ingresada correctamente, false si no
     * es así
     */
    private boolean obtenerRespuestaD() {
        boolean valida = false;
        String descripcion = textFieldRespuestaD.getText();
        RespuestaCliente respuestaD = new RespuestaCliente();
        respuestaD.setLetra('D');
        respuestaD.setEsCorrecta(radioButtonRespuestaD.isSelected());
        try {
            if (archivoImagenRespuestaD != null) {
                respuestaD.setImagen(Files.readAllBytes(archivoImagenRespuestaD.toPath()));
            } else if (UtileriaCadena.validarCadena(descripcion, 1, 300)) {
                respuestaD.setDescripcion(descripcion);
            }
            respuestas.add(respuestaD);
            valida = true;
        } catch (IllegalArgumentException excepcion) {
            if (excepcion.getMessage().equals(LONGITUD_INVALIDA)) {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaD",
                        UtileriaInterfazUsuario.generarCadenaRangoInvalidoParaMensaje(1, 300));
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_DATOS_INVALIDOS,
                        "key.modifiqueRespuestaD", excepcion.getMessage());
            }
            textFieldRespuestaD.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(RegistroCuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    /**
     *
     * @return Archivo seleccionado
     */
    private File cargarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("");
        return fileChooser.showOpenDialog(textFieldPregunta.getScene().getWindow());
    }

    /**
     * Asigna una imagen a la pregunta actual.
     */
    public void seleccionarImagenPregunta() {
        File archivoElegido = cargarImagen();
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                archivoImagenPregunta = archivoElegido;
                Image imagen = new Image(FILE + archivoElegido.getAbsolutePath());
                imageViewPregunta.setImage(imagen);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                        KEY_ARCHIVO_INVALIDO, KEY_SELECCIONAR_ARCHIVO);
            }
        }
    }

    /**
     * Asigna una imagen a la respuesta A.
     */
    public void seleccionarImagenRespuestaA() {
        File archivoElegido = cargarImagen();
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                archivoImagenRespuestaA = archivoElegido;
                Image imagen = new Image(FILE + archivoElegido.getAbsolutePath());
                imageViewRespuestaA.setImage(imagen);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                        KEY_ARCHIVO_INVALIDO, KEY_SELECCIONAR_ARCHIVO);
            }
        }
    }

    /**
     * Asigna una imagen a la respuesta B.
     */
    public void seleccionarImagenRespuestaB() {
        File archivoElegido = cargarImagen();
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                archivoImagenRespuestaB = archivoElegido;
                Image imagen = new Image(FILE + archivoElegido.getAbsolutePath());
                imageViewRespuestaB.setImage(imagen);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                        KEY_ARCHIVO_INVALIDO, KEY_SELECCIONAR_ARCHIVO);
            }
        }
    }

    /**
     * Asigna una imagen a la respuesta C.
     */
    public void seleccionarImagenRespuestaC() {
        File archivoElegido = cargarImagen();
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                archivoImagenRespuestaC = archivoElegido;
                Image imagen = new Image(FILE + archivoElegido.getAbsolutePath());
                imageViewRespuestaC.setImage(imagen);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                        KEY_ARCHIVO_INVALIDO, KEY_SELECCIONAR_ARCHIVO);
            }
        }
    }

    /**
     * Asigna una imagen a la respuesta D.
     */
    public void seleccionarImagenRespuestaD() {
        File archivoElegido = cargarImagen();
        if (archivoElegido != null) {
            if (archivoElegido.getName().endsWith(".jpg") || archivoElegido.getName().endsWith(".png")) {
                archivoImagenRespuestaD = archivoElegido;
                Image imagen = new Image(FILE + archivoElegido.getAbsolutePath());
                imageViewRespuestaD.setImage(imagen);
            } else {
                UtileriaInterfazUsuario.mostrarMensajeError(KEY_MENSAJE_SISTEMA,
                        KEY_ARCHIVO_INVALIDO, KEY_SELECCIONAR_ARCHIVO);
            }
        }
    }

    /**
     * Muestra en la IU una lista de las preguntas registradas en el
     * cuestionario actualmente.
     */
    private void mostrarPreguntasRegistradas() {
        ArrayList<Object> preguntasRegistradas = new ArrayList<>();
        String sinDescripcion = recursoIdioma.getString("key.sinDescripcion");
        List<PreguntaCliente> preguntasCuestionario = administradorCuestionario.getPreguntasCuestionario();

        preguntasCuestionario.forEach(pregunta -> {
            int numero = pregunta.getNumero();
            String descripcion = sinDescripcion;
            if (pregunta.getDescripcion() != null) {
                descripcion = pregunta.getDescripcion();
            }
            String cadenaListView = Integer.toString(numero) + ": " + descripcion;
            preguntasRegistradas.add(cadenaListView);
        });

        ObservableList<Object> preguntasRegistradasParaListView = FXCollections.observableArrayList(preguntasRegistradas);
        listViewPreguntasRegistradas.setItems(preguntasRegistradasParaListView);
        listViewPreguntasRegistradas.getSelectionModel().clearSelection();
    }

    /**
     * Reestablece a sus valores por defecto las secciones de la pregunta y
     * respuestas.
     */
    public void limpiarCampos() {
        preguntaEnEdicion = 0;
        textFieldPregunta.clear();
        imageViewPregunta.setImage(null);
        archivoImagenPregunta = null;
        textFieldRespuestaA.clear();
        imageViewRespuestaA.setImage(null);
        archivoImagenRespuestaA = null;
        radioButtonRespuestaA.setSelected(false);
        textFieldRespuestaB.clear();
        imageViewRespuestaB.setImage(null);
        archivoImagenRespuestaB = null;
        radioButtonRespuestaB.setSelected(false);
        textFieldRespuestaC.clear();
        imageViewRespuestaC.setImage(null);
        archivoImagenRespuestaC = null;
        radioButtonRespuestaC.setSelected(false);
        textFieldRespuestaD.clear();
        imageViewRespuestaD.setImage(null);
        archivoImagenRespuestaD = null;
        radioButtonRespuestaD.setSelected(false);
    }

}
