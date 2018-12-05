/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.servidor;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mx.fei.qa.accesoadatos.controller.UsuarioJpaController;
import mx.fei.qa.accesoadatos.controller.exceptions.IllegalOrphanException;
import mx.fei.qa.accesoadatos.controller.exceptions.NonexistentEntityException;
import mx.fei.qa.accesoadatos.controller.exceptions.PreexistingEntityException;
import mx.fei.qa.accesoadatos.entity.Usuario;
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import mx.fei.qa.sesion.AdministradorSesionUsuario;
import mx.fei.qa.sesion.SesionUsuario;

/**
 * Proceso servidor encargado de la administración de las Cuentas de Usuario así
 * como la autenticación del mismo.
 *
 * @version 1.0 12 Nov 2018
 * @author Carlos Onorio
 */
public class ServidorCuentaUsuarioRMI implements CuentaUsuarioInterface {

    private AdministradorSesionUsuario administradorUsuarios;
    private final UsuarioJpaController controladorUsuario;

    /**
     * Registra una nueva cuenta de usuario en el sistema, validando los datos
     * que provienen desde el cliente.
     *
     * @param usuario Datos de la cuenta de usuario a registrar
     * @return True si los datos fueron validos y se guardo exitosamente los
     * datos del usuario, false si fueron invalidos o no se pudieron guardar
     * correctamente
     * @throws RemoteException Lanzada si ocurrió algún problema en la conexión
     * cliente-servidor
     * @throws IllegalArgumentException Lanzada si el nombre de usuario ya
     * existe en el sistema
     */
    @Override
    public boolean guardarUsuario(UsuarioCliente usuario) throws RemoteException {
        boolean guardadoExitoso = false;
        byte[] fotoPerfil = usuario.getFotoPerfil();

        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre(usuario.getNombre());
        usuarioNuevo.setPassword(usuario.getContrasenia());
        usuarioNuevo.setCorreo(usuario.getCorreo());
        
        if (fotoPerfil != null) {
            usuarioNuevo.setFotoPerfil(fotoPerfil);
        } else {
            usuarioNuevo.setFotoPerfil(null);
        }
        try {
            controladorUsuario.create(usuarioNuevo);
            guardadoExitoso = true;
        } catch (PreexistingEntityException ex) {
            throw new IllegalArgumentException();
        } catch (Exception ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return guardadoExitoso;
    }

    /**
     * Actualiza los datos de un usuario del sistema.
     *
     * @param usuario Datos actualizados del usuario
     * @return True si se actualizaron correctamente los datos, False si no fué
     * así
     * @throws RemoteException Lanzada si ocurre algún problema en la conexión
     * cliente-servidor
     */
    @Override
    public boolean editarUsuario(UsuarioCliente usuario) throws RemoteException {
        boolean editadoExitoso = false;
        if (usuario != null) {
            administradorUsuarios.cerrarSesion(usuario.getNombre());
            try {
                Usuario usuarioEntity = controladorUsuario.findUsuario(usuario.getNombre());
                usuarioEntity.setCorreo(usuario.getCorreo());
                usuarioEntity.setPassword(usuario.getContrasenia());
                usuarioEntity.setFotoPerfil(usuario.getFotoPerfil());
                controladorUsuario.edit(usuarioEntity);
                editadoExitoso = true;
            } catch (IllegalOrphanException | NonexistentEntityException ex) {
                Logger.getLogger(ServidorCuentaUsuarioRMI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ServidorCuentaUsuarioRMI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return editadoExitoso;
    }

    /**
     * Servicio que permite la autenticación de usuarios en el sistema. Si las
     * credenciales son válidas, devuelve un objeto SesionUsuario, si son
     * inválidas lanza una excepcion IllegalArgumentException.
     *
     * @param nombre Nombre de usuario ingresado desde el cliente
     * @param contrasenia Contraseña ingresada desde el cliente
     * @return SesionUsuario con los datos del usuario autenticado
     * @throws RemoteException Lanzada si ocurre algun problema en el servidor
     * @throws IllegalArgumentException Lanzada si el nombre de usuario y
     * contraseña no coinciden con alguna cuenta en el sistema
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
     * Servicio que permite cerrar la sesión actual de un usuario en el sistema.
     *
     * @param nombre Identificador de la sesión actual a cerrar
     * @return True si la sesión actual se cerró correctamente, False si no fue
     * así
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

    /**
     * Inicializa el ServidorCuentaUsuarioRMI.
     */
    public ServidorCuentaUsuarioRMI() {
        EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        controladorUsuario = new UsuarioJpaController(fabricaEntityManager);
    }

}
