/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio.cuestionario;

import java.io.File;

/**
 *
 * @author Carlos Onorio
 */
public class RespuestaCliente {
    
    private char letra;
    private String descripcion;
    private File imagen;
    private boolean esCorrecta;

    public RespuestaCliente(char letra, String descripcion, File imagen, boolean esCorrecta) {
        this.letra = letra;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.esCorrecta = esCorrecta;
    }

    public char getLetra() {
        return letra;
    }

    public void setLetra(char letra) {
        this.letra = letra;
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

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
    
}
