/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.partida;

import java.io.Serializable;
import mx.fei.qa.dominio.actores.Jugador;

/**
 * Puntaje obtenido por un jugador al responder a una pregunta del cuestionario
 * de la partida
 *
 * @version 1.0 11 Nov 2018
 * @author Carlos Onorio
 */
public class PuntajeJugador implements Serializable {

    private Jugador jugador;
    private int puntaje;

    public PuntajeJugador(Jugador jugador) {
        this.jugador = jugador;
        this.puntaje = 0;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public int getPuntaje() {
        return puntaje;
    }
    
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void aumentarPuntaje(int puntaje) {
        this.puntaje += puntaje;
    }

}
