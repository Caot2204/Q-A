/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.interfazgrafica.adaptadortableview;

import javafx.beans.property.SimpleStringProperty;

/**
 * Detalles de un Cuestionario para tableView
 * 
 * @version 1.0 27 Oct 2018
 * @author Carlos Onorio
 */
public class AdaptadorCuestionario {
    
    private final SimpleStringProperty nombreCuestionario;
    private final SimpleStringProperty cantidadPreguntas;
    private final SimpleStringProperty vecesJugado;
    private final SimpleStringProperty ultimoGanador;

    public AdaptadorCuestionario() {
        this.nombreCuestionario = new SimpleStringProperty();
        this.cantidadPreguntas = new SimpleStringProperty();
        this.vecesJugado = new SimpleStringProperty();
        this.ultimoGanador = new SimpleStringProperty();
    }
    
    public String getNombreCuestionario() {
        return nombreCuestionario.get();
    }
    
    public void setNombreCuestionario(String nombreCuestionario) {
        this.nombreCuestionario.set(nombreCuestionario);
    }
    
    public int getCantidadPreguntas() {
        return Integer.parseInt(cantidadPreguntas.get());
    }
    
    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas.set(Integer.toString(cantidadPreguntas));
    }
    
    public int getVecesJugado() {
        return Integer.parseInt(vecesJugado.get());
    }
    
    public void setVecesJugado(int vecesJugado) {
        this.vecesJugado.set(Integer.toString(vecesJugado));
    }
    
    public String getUltimoGanador() {
        return ultimoGanador.get();
    }
    
    public void setUltimoGanador(String ultimoGanador) {
        this.ultimoGanador.set(ultimoGanador);
    }
    
}
