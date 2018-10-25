/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.cuestionario;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Carlos Onorio
 */
public class PreguntaCliente {
    
    private int numero;
    private String descripcion;
    private File imagen;
    private ArrayList<RespuestaCliente> respuestas;

    public PreguntaCliente(int numero, String descripcion, File imagen, ArrayList<RespuestaCliente> respuestas) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.respuestas = respuestas;
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

    public File getImagen() {
        return imagen;
    }

    public void setImagen(File imagen) {
        this.imagen = imagen;
    }

    public ArrayList<RespuestaCliente> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<RespuestaCliente> respuestas) {
        this.respuestas = respuestas;
    }
    
}
