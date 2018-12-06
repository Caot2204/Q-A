/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.sesion;

/**
 *
 * @author Carlos Onorio
 */
public class UsuarioConectadoAServidor {
    
    private String nombre;
    private String idSocket;

    public UsuarioConectadoAServidor(String nombre, String idSocket) {
        this.nombre = nombre;
        this.idSocket = idSocket;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdSocket() {
        return idSocket;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
}
