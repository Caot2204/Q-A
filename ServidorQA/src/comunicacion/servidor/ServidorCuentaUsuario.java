/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacion.servidor;

import comunicacion.interfaz.CuentaUsuarioInterface;
import java.io.File;
import java.rmi.RemoteException;
import sesion.AdministradorSesionUsuario;
import sesion.SesionUsuario;

/**
 * Proceso servidor encargado de la administración de las Cuentas de Usuario así
 * como la autenticación del mismo
 *
 * @author Carlos Onorio
 */
public class ServidorCuentaUsuario implements CuentaUsuarioInterface {

    private AdministradorSesionUsuario administradorUsuarios;

    @Override
    public boolean crearUsuario(String nombre, String contrasenia, String correo, File fotoPerfil) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean editarUsuario(String nombre, String contrasenia, String correo, File fotoPerfil) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Servicio que permite la autenticación de usuarios en el sistema. Si las
     * credenciales son válidas, devuelve un objeto SesionUsuario, si son
     * inválidas lanza una excepcion IllegalArgumentException
     *
     * @param nombre Nombre de usuario ingresado desde el cliente
     * @param contrasenia Contraseña ingresada desde el cliente
     * @return SesionUsuario con los datos del usuario autenticado
     * @throws RemoteException Lanzada si ocurre algun problema en el servidor
     */
    @Override
    public SesionUsuario iniciarSesion(String nombre, String contrasenia) throws RemoteException {
        SesionUsuario sesion = null;
        administradorUsuarios = AdministradorSesionUsuario.obtenerInstancia();

        if (administradorUsuarios.iniciarSesion(nombre, contrasenia)) {
            sesion = administradorUsuarios.obtenerDatosSesionUsuario(nombre);
        } else {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        return sesion;
    }

    /**
     * Servicio que permite cerrar la sesión actual de un usuario en el sistema
     * 
     * @param nombre Identificador de la sesión actual a cerrar
     * @return True si la sesión actual se cerró correctamente, False si no fue así
     * @throws RemoteException Lanzada si ocurre algun problema en el servidor
     */
    @Override
    public boolean cerrarSesion(String nombre) throws RemoteException {
        boolean sesionCerrada = false;
        administradorUsuarios = AdministradorSesionUsuario.obtenerInstancia();
        
        if (administradorUsuarios.cerrarSesion(nombre)) {
            sesionCerrada = true;
        } else {
            throw new IllegalArgumentException("El nombre no es válido");
        }
        
        return sesionCerrada;
    }

}
