/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sesion;

import accesoadatos.controller.UsuarioJpaController;
import accesoadatos.entity.Usuario;
import dominio.actores.UsuarioCliente;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Carlos Onorio
 */
public class AdministradorSesionUsuario {

    private static AdministradorSesionUsuario administradorSesiones;
    private final ArrayList<SesionUsuario> sesionesActuales;
    private final UsuarioJpaController controladorUsuario;
    private final EntityManagerFactory fabricaEntityManager;
    private Usuario usuario;

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
        this.fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        this.controladorUsuario = UsuarioJpaController.obtenerInstancia(fabricaEntityManager);        
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
        
        usuario = controladorUsuario.findUsuario(nombre);
        
        if (usuario != null) {
            if (contrasenia.equals(usuario.getPassword())) {
                Date fechaActual = new Date();
                UsuarioCliente usuarioCliente = new UsuarioCliente();
                usuarioCliente.setNombre(usuario.getNombre());
                usuarioCliente.setCorreo(usuario.getCorreo());
                usuarioCliente.setFotoPerfil(usuario.getFotoPerfil());
                
                SesionUsuario sesion = new SesionUsuario(fechaActual, usuarioCliente);
                sesionesActuales.add(sesion);
                autenticado = true;
            }            
        }

        return autenticado;
    }
    
    /**
     * Busca entre las sesiones iniciadas en el sistema una sesión en particular y la devuelve
     * @param nombre Identificador de la sesión a buscar
     * @return Sesion de usuario actual en el sistema
     */
    public SesionUsuario obtenerDatosSesionUsuario(String nombre) {
        SesionUsuario sesion = null;
        for (SesionUsuario sesionActual: sesionesActuales) {
            if (nombre.equals(sesionActual.getUsuario().getNombre())) {
                sesion = sesionActual;
                break;
            }
        }
        return sesion;        
    }
}
