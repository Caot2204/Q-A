/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.sesion;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mx.fei.qa.accesoadatos.controller.UsuarioJpaController;
import mx.fei.qa.accesoadatos.entity.Usuario;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import mx.fei.qa.utileria.UtileriaCadena;

/**
 * Permite controlar las sesiones vigentes de los usuarios iniciadas en el
 * sistema
 *
 * @author Carlos Onorio
 */
public class AdministradorSesionUsuario {

    private static AdministradorSesionUsuario administradorSesiones;
    private final ArrayList<SesionUsuario> sesionesActuales;

    /**
     *
     * @return Instancia del AdministradorSesionUsuario
     */
    public static AdministradorSesionUsuario obtenerInstancia() {
        if (administradorSesiones == null) {
            administradorSesiones = new AdministradorSesionUsuario();
        }
        return administradorSesiones;
    }

    private AdministradorSesionUsuario() {
        this.sesionesActuales = new ArrayList<>();
    }

    /**
     * Autentica las credenciales ingresadas desde el cliente y crea una
     * SesionUsuario en el servidor si las credenciales son válidas
     *
     * @param nombre Nombre del usuario ingresado en el cliente
     * @param contrasenia Contraseña del usuario ingresado en el cliente
     * @return True si la autenticación fue exitosa, False si no fue exitosa
     */
    public boolean iniciarSesion(String nombre, String contrasenia) {
        boolean autenticado = false;
        EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        UsuarioJpaController controladorUsuario = new UsuarioJpaController(fabricaEntityManager);
        Usuario usuario = controladorUsuario.findUsuario(nombre);

        if (usuario != null && nombre.equals(usuario.getNombre())
                && contrasenia.equals(usuario.getPassword())) {
            Date fechaActual = new Date();
            UsuarioCliente usuarioCliente = new UsuarioCliente();
            usuarioCliente.setNombre(usuario.getNombre());
            usuarioCliente.setCorreo(usuario.getCorreo());
            usuarioCliente.setContrasenia(usuario.getPassword());
            usuarioCliente.setFotoPerfil(usuario.getFotoPerfil());

            SesionUsuario sesion = new SesionUsuario(fechaActual, usuarioCliente);
            sesionesActuales.add(sesion);
            autenticado = true;
        }

        return autenticado;
    }

    /**
     * Elimina la sesión actual de un usuario en el sistema
     *
     * @param nombre Identificador de la sesión a eliminar
     * @return True si la sesión se eliminó, False si no fue así
     */
    public boolean cerrarSesion(String nombre) {
        boolean sesionCerrada = false;

        if (UtileriaCadena.validarCadena(nombre, 1, 150)) {
            SesionUsuario sesionACerrar = obtenerDatosSesionUsuario(nombre);
            sesionesActuales.remove(sesionACerrar);
            sesionCerrada = true;
        }

        return sesionCerrada;
    }

    /**
     * Busca entre las sesiones iniciadas en el sistema una sesión en particular
     * y la devuelve
     *
     * @param nombre Identificador de la sesión a buscar
     * @return Sesion de usuario actual en el sistema
     */
    public SesionUsuario obtenerDatosSesionUsuario(String nombre) {
        SesionUsuario sesion = null;
        for (SesionUsuario sesionActual : sesionesActuales) {
            if (nombre.equals(sesionActual.getUsuario().getNombre())) {
                sesion = sesionActual;
                break;
            }
        }
        return sesion;
    }

}
