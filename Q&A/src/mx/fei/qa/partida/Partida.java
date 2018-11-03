/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.partida;

import java.io.Serializable;
import java.util.ArrayList;
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
    private final ArrayList<PuntajeJugador> marcador;
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
     * @param puntajeJugador Puntaje obtenido de un jugador al responder a una
     * pregunta
     */
    public void actualizarMarcador(PuntajeJugador puntajeJugador) {
        for (int a = 0; a < marcador.size(); a++) {
            String nombrePuntajeActual = marcador.get(a).getJugador().getNombre();
            String nombreJugadorParametro = puntajeJugador.getJugador().getNombre();
            if (nombrePuntajeActual.equals(nombreJugadorParametro)) {
                marcador.set(a, puntajeJugador);
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
     * Genera un código de invitación aleatorio para la partida
     *
     */
    public void generarCodigoInvitacion() {
        Random random = new Random();
        this.codigoInvitacion = (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    public short getCodigoInvitacion() {
        return codigoInvitacion;
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
    public ArrayList<RespuestaCliente> obtenerRespuestasPreguntaActual() {
        return cuestionario.getPreguntas().get(preguntaActual).getRespuestas();
    }

    public ArrayList<PuntajeJugador> getMarcador() {
        return marcador;
    }

    public GraficaRespuestas getGraficaPreguntaActual() {
        return graficaPreguntaActual;
    }

}
