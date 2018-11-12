/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
        RespuestaCliente respuestaA = pregunta.getRespuestas().get(0);
        RespuestaCliente respuestaB = pregunta.getRespuestas().get(1);
        RespuestaCliente respuestaC = pregunta.getRespuestas().get(2);
        RespuestaCliente respuestaD = pregunta.getRespuestas().get(3);

        labelRespuestaA.setText(respuestaA.getDescripcion());
        labelRespuestaB.setText(respuestaB.getDescripcion());
        labelRespuestaC.setText(respuestaC.getDescripcion());
        labelRespuestaD.setText(respuestaD.getDescripcion());
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
