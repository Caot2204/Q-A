/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.cuestionario;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Carlos Onorio
 */
public class PreguntaCliente implements Serializable {
    
    private int numero;
    private String descripcion;
    private byte[] imagen;
    private ArrayList<RespuestaCliente> respuestas;

    public PreguntaCliente() {
        
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public ArrayList<RespuestaCliente> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<RespuestaCliente> respuestas) {
        this.respuestas = respuestas;
    }
    
}
