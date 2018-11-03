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

    public void setSesionUsuario(SesionUsuario sesionUsuario) {
        this.sesionUsuario = sesionUsuario;
    }

    public SesionUsuario getSesionUsuario() {
        return sesionUsuario;
    }

    public void removerSesionActual() {
        this.sesionUsuario = null;
    }

}
