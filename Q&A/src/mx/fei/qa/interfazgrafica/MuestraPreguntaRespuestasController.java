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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

/**
 * FXML Controller class
 *
 * @author Carlos Onorio
 */
public class MuestraPreguntaRespuestasController implements Initializable {

    @FXML
    private Label labelPregunta;

    @FXML
    private ImageView imageViewPregunta;

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

    @FXML
    private Label labelContador;

    private int contador;
    private Timer temporizador;
    private RespuestaCliente respuestaA;
    private RespuestaCliente respuestaB;
    private RespuestaCliente respuestaC;
    private RespuestaCliente respuestaD;
    private JugadorPartida jugadorPartida;
    
    private static final String FILE = "file:";
    private static final String KEY_A_JUGAR = "key.aJugarQA";
    private static final String SALA_ESPERA = "SalaEspera.fxml";

    /**
     * Inhabilida las acciones al dar clic a una respuesta si se detecta que el
     * usuario es monitor de la partida.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contador = 20;
        if (MonitorPartida.existeMonitorPartida()) {
            PreguntaCliente preguntaActual = MonitorPartida.obtenerInstancia()
                    .getPreguntaActual();
            labelRespuestaA.setOnMouseClicked(null);
            imageViewRespuestaA.setOnMouseClicked(null);
            labelRespuestaB.setOnMouseClicked(null);
            imageViewRespuestaB.setOnMouseClicked(null);
            labelRespuestaC.setOnMouseClicked(null);
            imageViewRespuestaC.setOnMouseClicked(null);
            labelRespuestaD.setOnMouseClicked(null);
            imageViewRespuestaD.setOnMouseClicked(null);
            desplegarPreguntaYRespuestas(preguntaActual);
        } else {
            jugadorPartida = JugadorPartida.obtenerInstancia();
        }
        contarSegundos();
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

        labelPregunta.setText(pregunta.getDescripcion());
        labelPregunta.setAlignment(Pos.CENTER);
        if (pregunta.getImagen() != null) {
            try {
                File imagenPerfil = new File("imagenPregunta");
                FileOutputStream fileOutputStream = new FileOutputStream(imagenPerfil);
                fileOutputStream.write(pregunta.getImagen());
                fileOutputStream.close();

                Image imagen = new Image(FILE + imagenPerfil.getAbsolutePath());
                imageViewPregunta.setImage(imagen);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MuestraPreguntaRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
     * Inicia la cuenta regresiva para mostrar las respuestas.
     */
    private void contarSegundos() {
        temporizador = FxTimer.runPeriodically(Duration.ofMillis(1000), () -> {
            actualizarContador();
        });
    }

    /**
     * Lleva el control del contador de la IU.
     */
    private void actualizarContador() {
        contador--;
        labelContador.setText(Integer.toString(contador));
        labelContador.setAlignment(Pos.CENTER);
        if (contador == 0) {
            if (MonitorPartida.existeMonitorPartida()) {
                UtileriaInterfazUsuario.mostrarVentana(getClass(), KEY_A_JUGAR,
                        "GraficaRespuestas.fxml", labelContador);
            } else {
                temporizador.stop();
                UtileriaInterfazUsuario.mostrarVentana(getClass(), KEY_A_JUGAR,
                        SALA_ESPERA, labelContador);
            }
        }
    }

    /**
     * Permite al jugador responder a la pregunta con la respuesta A, si es
     * correcta se calcula su puntaje y se notifica al monitor.
     */
    public void responderA() {
        if (respuestaA.isEsCorrecta()) {
            calcularPuntajeGanado();
            mostrarMensajeRespuestaCorrecta();
        } else {
            mostrarMensajeRespuestaIncorrecta();
        }
        jugadorPartida.enviarRespuesta('A');
        UtileriaInterfazUsuario.mostrarVentana(getClass(), KEY_A_JUGAR,
                SALA_ESPERA, labelContador);
    }

    /**
     * Permite al jugador responder a la pregunta con la respuesta B, si es
     * correcta se calcula su puntaje y se notifica al monitor.
     */
    public void responderB() {
        if (respuestaB.isEsCorrecta()) {
            calcularPuntajeGanado();
            mostrarMensajeRespuestaCorrecta();
        } else {
            mostrarMensajeRespuestaIncorrecta();
        }
        jugadorPartida.enviarRespuesta('B');
        UtileriaInterfazUsuario.mostrarVentana(getClass(), KEY_A_JUGAR,
                SALA_ESPERA, labelContador);
    }

    /**
     * Permite al jugador responder a la pregunta con la respuesta C, si es
     * correcta se calcula su puntaje y se notifica al monitor.
     */
    public void responderC() {
        if (respuestaC.isEsCorrecta()) {
            calcularPuntajeGanado();
            mostrarMensajeRespuestaCorrecta();
        } else {
            mostrarMensajeRespuestaIncorrecta();
        }
        jugadorPartida.enviarRespuesta('C');
        UtileriaInterfazUsuario.mostrarVentana(getClass(), KEY_A_JUGAR,
                SALA_ESPERA, labelContador);
    }

    /**
     * Permite al jugador responder a la pregunta con la respuesta D, si es
     * correcta se calcula su puntaje y se notifica al monitor.
     */
    public void responderD() {
        if (respuestaD.isEsCorrecta()) {
            calcularPuntajeGanado();
            mostrarMensajeRespuestaCorrecta();
        } else {
            mostrarMensajeRespuestaIncorrecta();
        }
        jugadorPartida.enviarRespuesta('D');
        UtileriaInterfazUsuario.mostrarVentana(getClass(), KEY_A_JUGAR,
                SALA_ESPERA, labelContador);
    }

    /**
     * Calcula y aumenta el puntaje de un jugador al responder una pregunta.
     */
    private void calcularPuntajeGanado() {
        int puntajeObtenido = 1000 + (contador * 20);
        jugadorPartida.getPuntajeJugador().aumentarPuntaje(puntajeObtenido);
    }

    /**
     * Muestra una notificación indicando que el jugador respondió correctamente
     * a la pregunta.
     */
    public void mostrarMensajeRespuestaCorrecta() {
        UtileriaInterfazUsuario.mostrarMensajeExito("key.respuestaCorrecta",
                "key.bienJugado", "key.respondioCorrectamente");
    }

    /**
     * Muestra una notificación indicando que el jugador se equivocó de
     * respuesta.
     */
    public void mostrarMensajeRespuestaIncorrecta() {
        Locale locale = Locale.getDefault();
        ResourceBundle recurso = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
        String respuestaCorrecta = "";
        if (respuestaA.isEsCorrecta()) {
            respuestaCorrecta = "A";
        } else if (respuestaB.isEsCorrecta()) {
            respuestaCorrecta = "B";
        } else if (respuestaC.isEsCorrecta()) {
            respuestaCorrecta = "C";
        } else if (respuestaD.isEsCorrecta()) {
            respuestaCorrecta = "D";
        }
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(recurso.getString("key.respuestaIncorrecta"));
        alerta.setHeaderText(recurso.getString("key.malaSuerte"));
        alerta.setContentText(recurso.getString("key.laRespuestaCorrectaEs") + respuestaCorrecta);
        alerta.showAndWait();
    }

}
