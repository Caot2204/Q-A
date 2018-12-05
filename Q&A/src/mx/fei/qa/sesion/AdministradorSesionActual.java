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
public class AdministradorSesionActual {

    private static AdministradorSesionActual administradorSesion;
    private SesionUsuario sesionUsuario;

    /**
     * Obtiene una instancia de AdministradorSesionActual para manipular la
     * sesión iniciada en el cliente
     *
     * @return Administrador de la sesión actual para obtener los datos de esta
     */
    public static AdministradorSesionActual obtenerAdministrador() {
        if (administradorSesion == null) {
            administradorSesion = new AdministradorSesionActual();
        }
        return administradorSesion;
    }

    private AdministradorSesionActual() {

    }

    /**
     * Verifica si existe una sesión de usuario iniciada en el sistema.
     *
     * @return True si hay una sesión iniciada, False si no la hay
     */
    public boolean existeSesionIniciada() {
        boolean existeSesion = false;
        if (sesionUsuario != null) {
            existeSesion = true;
        }
        return existeSesion;
    }

    /**
     *
     * @return Nombre del usuario de la sesión actual en el cliente
     */
    public String getNombreUsuarioActual() {
        return this.sesionUsuario.getUsuario().getNombre();
    }

    /**
     *
     * @return Correo del usuario de la sesión actual en el cliente
     */
    public String getCorreoUsuarioActual() {
        return this.sesionUsuario.getUsuario().getCorreo();
    }

    /**
     *
     * @param sesionUsuario Sesion actual del usuario
     */
    public void setSesionUsuario(SesionUsuario sesionUsuario) {
        this.sesionUsuario = sesionUsuario;
    }

    /**
     *
     * @return Sesión actual del usuario
     */
    public SesionUsuario getSesionUsuario() {
        return sesionUsuario;
    }

    /**
     * Elimina la sesión actual de usuario.
     */
    public void removerSesionActual() {
        this.sesionUsuario = null;
    }

}
