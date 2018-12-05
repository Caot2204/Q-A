/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.dominio.actores;

import java.io.Serializable;

/**
 * Jugador de una partida de un cuestionario
 * 
 * @version 1.0 29 Oct 2018
 * @author Carlos Onorio
 */
public class Jugador implements Serializable {
    
    private String nombre;
    private byte[] fotoPerfil;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
}
