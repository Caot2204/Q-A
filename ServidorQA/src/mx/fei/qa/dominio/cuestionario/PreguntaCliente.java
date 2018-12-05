/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.dominio.cuestionario;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Carlos Onorio
 */
public class PreguntaCliente implements Serializable {
    
    private int numero;
    private String descripcion;
    private byte[] imagen;
    private List<RespuestaCliente> respuestas;

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

    public List<RespuestaCliente> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaCliente> respuestas) {
        this.respuestas = respuestas;
    }
    
}
