/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.partida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mx.fei.qa.dominio.actores.Jugador;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;

/**
 * Datos de una partida creada para jugar un Cuestionario elaborado por un
 * Usuario
 *
 * @version 1.0 29 Oct 2018
 * @author Carlos Onorio
 */
public class Partida implements Serializable {

    private short codigoInvitacion;
    private int preguntaActual;
    private final CuestionarioCliente cuestionario;
    private final List<PuntajeJugador> marcador;
    private final List<MensajeChat> chat;
    private final GraficaRespuestas graficaPreguntaActual;

    /**
     * Inicializa las variables para comenzar una partida
     *
     * @param cuestionario Cuestionario del cual tratará la partida
     */
    public Partida(CuestionarioCliente cuestionario) {
        this.preguntaActual = 0;
        this.cuestionario = cuestionario;
        this.marcador = new ArrayList<>();
        this.chat = new ArrayList<>();
        this.graficaPreguntaActual = new GraficaRespuestas();
    }

    /**
     * Agrega un nuevo puntaje de jugador al marcador, inicializandolo con 0
     * puntos
     *
     * @param jugador Jugador que se unirá a la partida
     */
    public void agregarJugador(Jugador jugador) {
        PuntajeJugador puntaje = new PuntajeJugador(jugador);
        this.marcador.add(puntaje);
    }

    /**
     * Actualiza el marcador de jugadores en orden descendente con un puntaje
     * nuevo
     *
     * @param jugador Nombre del jugador a actualizar su puntaje
     * @param puntaje Puntos obtenidos al responder una pregunta
     */
    public void actualizarMarcador(String jugador, int puntaje) {
        for (int a = 0; a < marcador.size(); a++) {
            PuntajeJugador puntajeJugador = marcador.get(a);
            if (puntajeJugador.getJugador().getNombre().equals(jugador)) {
                puntajeJugador.setPuntaje(puntaje);
            }
        }

        for (int a = 0; a < marcador.size(); a++) {
            for (int b = 0; b < (marcador.size() - 1); b++) {
                if (marcador.get(b).getPuntaje() < marcador.get(b + 1).getPuntaje()) {
                    PuntajeJugador puntajeTemporal = marcador.get(b);
                    marcador.set(b, marcador.get(b + 1));
                    marcador.set((b + 1), puntajeTemporal);
                }
            }
        }
    }

    /**
     * Agrega un nuevo mensaje al chat enviado por un jugador o monitor de la
     * partida
     *
     * @param mensaje Mensaje de un jugador o monitor
     */
    public void actualizarChat(MensajeChat mensaje) {
        chat.add(mensaje);
    }

    /**
     *
     * @return Chat de la partida
     */
    public List<MensajeChat> getChat() {
        return chat;
    }

    /**
     * Genera un código de invitación aleatorio para la partida
     *
     */
    public void generarCodigoInvitacion() {
        Random random = new Random();
        this.codigoInvitacion = (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    /**
     *
     * @return Código de invitación de la partida
     */
    public short getCodigoInvitacion() {
        return codigoInvitacion;
    }
    
    /**
     * 
     * @return Nombre del cuestionario asociado a la partida
     */
    public String getNombreCuestionario() {
        return cuestionario.getNombre();
    }

    /**
     * Devuelve la primer pregunta del cuestionario para comenzar la partida
     *
     * @return Primer PreguntaCliente del Cuestionario
     */
    public PreguntaCliente obtenerPrimerPregunta() {
        return cuestionario.getPreguntas().get(0);
    }

    /**
     * Devuelve la siguiente pregunta del cuestionario
     *
     * @return Siguiente PreguntaCliente del Cuestionario
     */
    public PreguntaCliente obtenerSiguientePregunta() {
        preguntaActual++;
        return cuestionario.getPreguntas().get(preguntaActual);
    }

    /**
     * Devuelve las respuestas de la pregunta actual de la partida
     *
     * @return Conjunto de RespuestaCliente a la PreguntaCliente
     */
    public List<RespuestaCliente> obtenerRespuestasPreguntaActual() {
        return cuestionario.getPreguntas().get(preguntaActual).getRespuestas();
    }

    /**
     *
     * @return Marcador actual de la partida
     */
    public List<PuntajeJugador> getMarcador() {
        return marcador;
    }

    /**
     *
     * @return Gráfica de respuestas de la pregunta actual
     */
    public GraficaRespuestas getGraficaPreguntaActual() {
        return graficaPreguntaActual;
    }

}
