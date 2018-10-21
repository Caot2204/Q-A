/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sesion;

/**
 *
 * @author Carlos Onorio
 */
public class AdministradorSesionActual {

    private static AdministradorSesionActual administradorSesion;
    private final SesionUsuario sesion;

    /**
     * Establece la sesión de usuario iniciada en el sistema cliente
     * @param sesion
     * @return Administrador de la sesión actual para obtener los datos de esta
     */
    public static AdministradorSesionActual obtenerAdministrador(SesionUsuario sesion) {
        if (administradorSesion == null) {
            administradorSesion = new AdministradorSesionActual(sesion);
        }
        return administradorSesion;
    }

    private AdministradorSesionActual(SesionUsuario sesion) {
        this.sesion = sesion;
    }

    public SesionUsuario obtenerSesionUsuario() {
        return sesion;
    }

}
