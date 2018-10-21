/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sesion;

import dominio.actores.UsuarioCliente;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Carlos Onorio
 */
public class SesionUsuario implements Serializable{
    
    private Date fechaInicio;
    private UsuarioCliente usuario;
    
    public SesionUsuario(Date fechaInicio, UsuarioCliente usuario) {
        this.fechaInicio = fechaInicio;
        this.usuario = usuario;
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public UsuarioCliente getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioCliente usuario) {
        this.usuario = usuario;
    }
    
}
