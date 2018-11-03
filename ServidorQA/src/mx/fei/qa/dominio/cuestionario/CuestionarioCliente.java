/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.dominio.cuestionario;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Carlos Onorio
 */
public class CuestionarioCliente implements Serializable {
    
    private String nombre;
    private int vecesJugado;
    private String ultimoGanador;
    private String autor;
    private ArrayList<PreguntaCliente> preguntas;

    public CuestionarioCliente() {
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVecesJugado() {
        return vecesJugado;
    }

    public void setVecesJugado(int vecesJugado) {
        this.vecesJugado = vecesJugado;
    }

    public String getUltimoGanador() {
        return ultimoGanador;
    }

    public void setUltimoGanador(String ultimoGanador) {
        this.ultimoGanador = ultimoGanador;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public ArrayList<PreguntaCliente> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(ArrayList<PreguntaCliente> preguntas) {
        this.preguntas = preguntas;
    }
    
}
