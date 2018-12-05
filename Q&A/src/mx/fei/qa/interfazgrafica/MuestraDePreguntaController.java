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
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

/**
 * Controlador de la IU MuestraDePregunta.fxml
 *
 * @version 1.0 10 Nov 2018
 * @author Carlos Onorio
 */
public class MuestraDePreguntaController implements Initializable {

    @FXML
    private Label labelDescripcionPregunta;

    @FXML
    private ImageView imageViewPregunta;

    @FXML
    private Label labelContador;

    private int contador;
    private Timer temporizador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contador = 10;
    }

    /**
     * Despliega los datos de la pregunta actual para ser visualizada por los
     * jugadores
     *
     * @param pregunta Pregunta a ser mostrada
     */
    public void desplegarPregunta(PreguntaCliente pregunta) {
        labelDescripcionPregunta.setText(pregunta.getDescripcion());
        labelDescripcionPregunta.setAlignment(Pos.CENTER);
        if (pregunta.getImagen() != null) {
            try {
                File imagenPerfil = new File("imagenPregunta");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenPerfil);
                fileOutputStream.write(pregunta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image("file:" + imagenPerfil.getAbsolutePath());
                imageViewPregunta.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MuestraDePreguntaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MuestraDePreguntaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        contarSegundos();
    }

    /**
     * Inicia la cuenta regresiva para mostrar las respuestas
     */
    private void contarSegundos() {
        temporizador = FxTimer.runPeriodically(Duration.ofMillis(1000), () -> {
            actualizarContador();
        });
    }

    /**
     * Lleva el control del contador de la IU
     */
    private void actualizarContador() {
        contador--;
        labelContador.setText(Integer.toString(contador));
        labelContador.setAlignment(Pos.CENTER);
        if (contador == 0) {
            if (MonitorPartida.existeMonitorPartida()) {
                MonitorPartida.obtenerInstancia().enviarRespuestasAJugadores();
                UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.aJugarQA",
                        "MuestraPreguntaRespuestas.fxml", labelContador);
            } else {
                temporizador.stop();
                UtileriaInterfazUsuario.cerrarVentanaMedianteElementoFX(labelContador);
            }
        }
    }

}
