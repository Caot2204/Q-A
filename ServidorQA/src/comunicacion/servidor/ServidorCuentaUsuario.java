/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacion.servidor;

import accesoadatos.controller.UsuarioJpaController;
import accesoadatos.controller.exceptions.PreexistingEntityException;
import accesoadatos.entity.Usuario;
import comunicacion.interfaz.CuentaUsuarioInterface;
import dominio.actores.UsuarioCliente;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import sesion.AdministradorSesionUsuario;
import sesion.SesionUsuario;
import utileria.UtileriaCadena;

/**
 * Proceso servidor encargado de la administración de las Cuentas de Usuario así
 * como la autenticación del mismo
 *
 * @author Carlos Onorio
 */
public class ServidorCuentaUsuario implements CuentaUsuarioInterface {

    private AdministradorSesionUsuario administradorUsuarios;

    /**
     * Registra una nueva cuenta de usuario en el sistema, validando los datos que
     * provienen desde el cliente.
     * 
     * @param usuario Datos de la cuenta de usuario a registrar
     * @return True si los datos fueron validos y se guardo exitosamente los datos del usuario, false si fueron invalidos o no se pudieron guardar correctamente
     * @throws RemoteException Lanzada si ocurrió algún problema en la conexión cliente-servidor
     * @throws IllegalArgumentException Lanzada si el nombre de usuario ya existe en el sistema
     */
    @Override
    public boolean guardarUsuario(UsuarioCliente usuario) throws RemoteException {
        Usuario usuarioNuevo;
        boolean guardadoExitoso = false;
        String nombre = usuario.getNombre();
        String correo = usuario.getCorreo();
        String contrasenia = usuario.getContrasenia();
        byte[] fotoPerfil = usuario.getFotoPerfil();
        
        if (UtileriaCadena.validarCadena(nombre, 1, 150)) {
            if (UtileriaCadena.validarCadena(contrasenia, 1, 100)) {
                if (UtileriaCadena.validarCadena(correo, 1, 150)) {
                    usuarioNuevo = new Usuario();
                    usuarioNuevo.setNombre(nombre);
                    usuarioNuevo.setPassword(contrasenia);
                    usuarioNuevo.setCorreo(correo);
                    if (fotoPerfil != null) {
                        usuarioNuevo.setFotoPerfil(fotoPerfil);
                    } else {
                        usuarioNuevo.setFotoPerfil(null);                        
                    }
                    EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
                    UsuarioJpaController controladorUsuario = new UsuarioJpaController(fabricaEntityManager);
                    try {
                        controladorUsuario.create(usuarioNuevo);
                        guardadoExitoso = true;
                    } catch (PreexistingEntityException ex) {
                        throw new IllegalArgumentException();
                    } catch (Exception ex) {
                        Logger.getLogger(ServidorCuentaUsuario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        return guardadoExitoso;
    }

    @Override
    public boolean editarUsuario(UsuarioCliente usuario) throws RemoteException {
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
     * @throws IllegalArgumentException Lanzada si el nombre de usuario y contraseña no coinciden con alguna cuenta en el sistema
     */
    @Override
    public SesionUsuario iniciarSesion(String nombre, String contrasenia) throws RemoteException {
        SesionUsuario sesion;
        administradorUsuarios = AdministradorSesionUsuario.obtenerInstancia();

        if (administradorUsuarios.iniciarSesion(nombre, contrasenia)) {
            sesion = administradorUsuarios.obtenerDatosSesionUsuario(nombre);
            return sesion;
        } else {
            throw new IllegalArgumentException("Credenciales invalidas");
        }
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
