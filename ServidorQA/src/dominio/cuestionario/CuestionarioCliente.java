/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.cuestionario;

import java.util.ArrayList;

/**
 *
 * @author Carlos Onorio
 */
public class CuestionarioCliente {
    
    private String nombre;
    private ArrayList<PreguntaCliente> preguntas;

    public CuestionarioCliente(String nombre, ArrayList<PreguntaCliente> preguntas) {
        this.nombre = nombre;
        this.preguntas = preguntas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<PreguntaCliente> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(ArrayList<PreguntaCliente> preguntas) {
        this.preguntas = preguntas;
    }
    
}
