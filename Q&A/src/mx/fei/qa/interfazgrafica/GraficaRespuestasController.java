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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.partida.GraficaRespuestas;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class GraficaRespuestasController implements Initializable {

    @FXML
    private Label labelCantidadA;

    @FXML
    private Label labelCantidadB;

    @FXML
    private Label labelCantidadC;

    @FXML
    private Label labelCantidadD;

    @FXML
    private Label labelRespuestaA;

    @FXML
    private ImageView imageViewRespuestaA;

    @FXML
    private Label labelRespuestaB;

    @FXML
    private ImageView imageViewRespuestaB;

    @FXML
    private Label labelRespuestaC;

    @FXML
    private ImageView imageViewRespuestaC;

    @FXML
    private Label labelRespuestaD;

    @FXML
    private ImageView imageViewRespuestaD;

    private MonitorPartida monitor;
    private RespuestaCliente respuestaA;
    private RespuestaCliente respuestaB;
    private RespuestaCliente respuestaC;
    private RespuestaCliente respuestaD;
    
    private static final String FILE = "file:";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monitor = MonitorPartida.obtenerInstancia();
        GraficaRespuestas grafica = monitor.obtenerGraficaPreguntaActual();
        labelCantidadA.setText(Integer.toString(grafica.getContadorA()));
        labelCantidadB.setText(Integer.toString(grafica.getContadorB()));
        labelCantidadC.setText(Integer.toString(grafica.getContadorC()));
        labelCantidadD.setText(Integer.toString(grafica.getContadorD()));
        desplegarPreguntaYRespuestas(monitor.getPreguntaActual());
    }

    /**
     * Despliega la pregunta actual junto a sus respectivas respuestas con/sin
     * su descripción y/o imágenes.
     *
     * @param pregunta Pregunta con respuesta a mostrar en la IU.
     */
    public void desplegarPreguntaYRespuestas(PreguntaCliente pregunta) {
        respuestaA = pregunta.getRespuestas().get(0);
        respuestaB = pregunta.getRespuestas().get(1);
        respuestaC = pregunta.getRespuestas().get(2);
        respuestaD = pregunta.getRespuestas().get(3);

        desplegarRespuestaA();
        desplegarRespuestaB();
        desplegarRespuestaC();
        desplegarRespuestaD();
    }

    /**
     * Muestra en la IU los datos de la respuesta A de la pregunta actual.
     */
    private void desplegarRespuestaA() {
        labelRespuestaA.setText(respuestaA.getDescripcion());
        if (respuestaA.getImagen() != null) {
            try {
                File imagenRespuestaA = new File("imagenRespuestaA");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenRespuestaA);
                fileOutputStream.write(respuestaA.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + imagenRespuestaA.getAbsolutePath());
                imageViewRespuestaA.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Muestra en la IU los datos de la respuesta B de la pregunta actual.
     */
    private void desplegarRespuestaB() {
        labelRespuestaB.setText(respuestaB.getDescripcion());
        if (respuestaB.getImagen() != null) {
            try {
                File imagenRespuestaB = new File("imagenRespuestaB");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenRespuestaB);
                fileOutputStream.write(respuestaB.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + imagenRespuestaB.getAbsolutePath());
                imageViewRespuestaB.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Muestra en la IU los datos de la respuesta C de la pregunta actual.
     */
    private void desplegarRespuestaC() {
        labelRespuestaC.setText(respuestaC.getDescripcion());
        if (respuestaC.getImagen() != null) {
            try {
                File imagenRespuestaC = new File("imagenRespuestaC");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenRespuestaC);
                fileOutputStream.write(respuestaC.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + imagenRespuestaC.getAbsolutePath());
                imageViewRespuestaC.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Muestra en la IU los datos de la respuesta D de la pregunta actual.
     */
    private void desplegarRespuestaD() {
        labelRespuestaD.setText(respuestaD.getDescripcion());
        if (respuestaD.getImagen() != null) {
            try {
                File imagenRespuestaD = new File("imagenRespuestaD");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenRespuestaD);
                fileOutputStream.write(respuestaD.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + imagenRespuestaD.getAbsolutePath());
                imageViewRespuestaD.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Despliega el chat actual de la partida.
     */
    public void mostrarChat() {
        UtileriaInterfazUsuario.mostrarVentanaSinCerrarActual(getClass(),
                "key.chat", "Chat.fxml");
    }

    /**
     * Continúa con la partida desplegando el marcador de jugadores actual.
     */
    public void desplegarMarcador() {
        monitor.enviarMarcadorAJugadores();
        UtileriaInterfazUsuario.cerrarVentanaMedianteElementoFX(labelCantidadA);
    }

}
