/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacion.interfaz;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import sesion.SesionUsuario;

/**
 *
 * @author Carlos Onorio
 */
public interface CuentaUsuarioInterface extends Remote{
    
    public boolean crearUsuario(String nombre, String contrasenia, String correo, File fotoPerfil) throws RemoteException;
    public boolean editarUsuario(String nombre, String contrasenia, String correo, File fotoPerfil) throws RemoteException;
    public SesionUsuario iniciarSesion(String nombre, String contrasenia) throws RemoteException;
    public boolean cerrarSesion(String nombre) throws RemoteException;
        
}
