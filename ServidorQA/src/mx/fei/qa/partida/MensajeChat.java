/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.partida;

import java.io.Serializable;

/**
 * Datos de un mensaje enviado por un jugador en una partida
 *
 * @version 1.0 1 Oct 2018
 * @author Carlos Onorio
 */
public class MensajeChat implements Serializable {

    private String nombreJugador;
    private String mensaje;

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
