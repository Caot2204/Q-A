/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.interfaz;

import mx.fei.qa.dominio.actores.UsuarioCliente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import mx.fei.qa.sesion.SesionUsuario;

/**
 *
 * @author Carlos Onorio
 */
public interface CuentaUsuarioInterface extends Remote{
    public boolean guardarUsuario(UsuarioCliente usuario) throws RemoteException;
    public boolean editarUsuario(UsuarioCliente usuario) throws RemoteException;
    public SesionUsuario iniciarSesion(String nombre, String contrasenia) throws RemoteException;
    public boolean cerrarSesion(String nombre) throws RemoteException;
}
