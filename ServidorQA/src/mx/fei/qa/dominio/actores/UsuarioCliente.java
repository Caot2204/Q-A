/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.dominio.actores;

import java.io.Serializable;
import mx.fei.qa.utileria.UtileriaCadena;

/**
 *
 * @author Carlos Onorio
 */
public class UsuarioCliente implements Serializable {

    private String nombre;
    private String correo;
    private String contrasenia;
    private byte[] fotoPerfil;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (UtileriaCadena.validarCadena(nombre, 1, 150)) {
            this.nombre = nombre;
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (UtileriaCadena.validarCadena(correo, 1, 150)) {
            this.correo = correo;
        }
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        if (UtileriaCadena.validarCadena(contrasenia, 1, 100)) {
            this.contrasenia = contrasenia;
        }
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

}
