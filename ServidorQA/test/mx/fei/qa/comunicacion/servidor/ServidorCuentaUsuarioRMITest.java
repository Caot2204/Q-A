/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.servidor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import mx.fei.qa.accesoadatos.controller.UsuarioJpaController;
import mx.fei.qa.accesoadatos.controller.exceptions.IllegalOrphanException;
import mx.fei.qa.accesoadatos.controller.exceptions.NonexistentEntityException;
import mx.fei.qa.dominio.actores.UsuarioCliente;
import mx.fei.qa.sesion.SesionUsuario;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Carlos Onorio
 */
public class ServidorCuentaUsuarioRMITest {

    private UsuarioCliente usuarioSinImagen;
    private UsuarioCliente usuarioConImagen;
    private final ServidorCuentaUsuarioRMI servidor;

    public ServidorCuentaUsuarioRMITest() {
        servidor = new ServidorCuentaUsuarioRMI();
    }

    @Before
    public void crearUsuarios() {
        crearUsuarioSinImagen();
        crearUsuarioConImagen();
    }

    private void crearUsuarioSinImagen() {
        usuarioSinImagen = new UsuarioCliente();
        usuarioSinImagen.setNombre("Fernando");
        usuarioSinImagen.setContrasenia("12345567");
        usuarioSinImagen.setCorreo("elFercho@gmail.com");
        usuarioSinImagen.setFotoPerfil(null);
    }

    private void crearUsuarioConImagen() {
        usuarioConImagen = new UsuarioCliente();
        usuarioConImagen.setNombre("Fernando2");
        usuarioConImagen.setContrasenia("123455617");
        usuarioConImagen.setCorreo("elFercho2@gmail.com");
        File fotoPerfil = new File("C:\\Users\\Carlos Onorio\\Documents\\GitHub\\Q-A\\Q&A\\src\\mx\\fei\\qa\\recursos\\fotoPerfilDefault.png");
        try {
            usuarioConImagen.setFotoPerfil(Files.readAllBytes(fotoPerfil.toPath()));
        } catch (IOException ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void guardarUsuarioSinImagen() {
        try {
            boolean valorEsperado = true;
            boolean valorObtenido = servidor.guardarUsuario(usuarioSinImagen);
            assertEquals("Guardar usuario sin imagen", valorEsperado, valorObtenido);
            
            iniciarSesion();
            editarUsuario();
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void guardarUsuarioConImagen() {
        try {
            boolean valorEsperado = true;
            boolean valorObtenido = servidor.guardarUsuario(usuarioConImagen);
            assertEquals("Guardar usuario con imagen", valorEsperado, valorObtenido);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void iniciarSesion() {
        try {
            String usuario = "Fernando";
            String contrasenia = "12345567";
            SesionUsuario sesion = servidor.iniciarSesion(usuario, contrasenia);
            assertEquals("Obtencion nombre usuario", usuario, sesion.getUsuario().getNombre());
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editarUsuario() {
        usuarioSinImagen.setContrasenia("otraContrasenia");
        File fotoPerfil = new File("C:\\Users\\Carlos Onorio\\Documents\\GitHub\\Q-A\\Q&A\\src\\mx\\fei\\qa\\recursos\\fotoPerfilDefault.png");
        try {
            usuarioSinImagen.setFotoPerfil(Files.readAllBytes(fotoPerfil.toPath()));
            servidor.editarUsuario(usuarioSinImagen);
            
            SesionUsuario sesion = servidor.iniciarSesion("Fernando", "otraContrasenia");
            assertEquals("Obtencion nombre usuario", "Fernando", sesion.getUsuario().getNombre());
            assertEquals("Editar usuario", usuarioSinImagen.getFotoPerfil(), sesion.getUsuario().getFotoPerfil());
        } catch (IOException ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void limpiarDatosTest() {
        EntityManagerFactory fabricaEntityManager = Persistence.createEntityManagerFactory("ServidorQAPU");
        UsuarioJpaController controlador = new UsuarioJpaController(fabricaEntityManager);
        try {
            controlador.destroy("Fernando");
            controlador.destroy("Fernando2");
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(ServidorCuentaUsuarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
