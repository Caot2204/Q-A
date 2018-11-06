/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.partida;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mx.fei.qa.comunicacion.interfaz.PartidaInterface;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import mx.fei.qa.sesion.AdministradorSesionActual;
import mx.fei.qa.utileria.UtileriaInterfazUsuario;

/**
 *
 * @author Carlos Onorio
 */
public class AdministradorPartida {

    private static AdministradorPartida administrador;
    private Socket socket;
    private Partida partida;

    public static AdministradorPartida obtenerInstancia() {
        if (administrador == null) {
            try {
                administrador = new AdministradorPartida();
            } catch (URISyntaxException ex) {
                Logger.getLogger(AdministradorPartida.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return administrador;
    }

    private AdministradorPartida() throws URISyntaxException {
        this.socket = IO.socket("http://localhost:5000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                socket.emit("unirAPartida", partida.getCodigoInvitacion());
            }
        }).on("recibirRespuesta", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                char respuesta = (char) os[0];
                PuntajeJugador puntaje = (PuntajeJugador) os[1];
                partida.getGraficaPreguntaActual().actualizarGrafica(respuesta);
                partida.actualizarMarcador(puntaje);
            }
        });
    }

    public boolean iniciarPartida(String nombreCuestionario) {
        boolean creacionExitosa = false;
        try {
            Registry registro = LocateRegistry.getRegistry();
            PartidaInterface stubPartida = (PartidaInterface) registro.lookup("servidorPartidas");
            AdministradorSesionActual administradorSesion = AdministradorSesionActual.obtenerAdministrador();
            String autorCuestionario = administradorSesion.getNombreUsuarioActual();
            partida = stubPartida.crearPartida(autorCuestionario, nombreCuestionario);
            creacionExitosa = true;
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(AdministradorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return creacionExitosa;
    }

    public void enviarInvitaciones(ArrayList<String> correos) {
        correos.forEach((correo) -> {
            enviarCorreo(correo);
        });
    }

    private void enviarCorreo(String correoDestinatario) {
        ResourceBundle recursoPropiedadesCliente = ResourceBundle.getBundle("mx.fei.qa.utileria.cliente");
        String correoDelJuego = recursoPropiedadesCliente.getString("key.correoDelJuego");
        String contrasenia = recursoPropiedadesCliente.getString("key.contraseniaDelCorreo");

        Properties propiedadesCorreo = new Properties();
        propiedadesCorreo.put("mail.smtp.auth", "true");
        propiedadesCorreo.put("mail.smtp.starttls.enable", "true");
        propiedadesCorreo.put("mail.smtp.host", "smtp.gmail.com");
        propiedadesCorreo.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(propiedadesCorreo,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoDelJuego, contrasenia);
            }
        });

        try {
            Locale locale = Locale.getDefault();
            ResourceBundle recursoIdioma = ResourceBundle.getBundle("mx.fei.qa.lang.lang", locale);
            String asunto = recursoIdioma.getString("key.asuntoCorreo");
            String cuerpo = recursoIdioma.getString("key.cuerpoCorreo");
            Message correo = new MimeMessage(sesion);

            correo.setFrom(new InternetAddress(correoDelJuego));
            correo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestinatario));
            correo.setSubject(asunto);
            correo.setText(cuerpo + ":\n\n" + Short.toString(partida.getCodigoInvitacion()));

            Transport.send(correo);

        } catch (MessagingException ex) {
            UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema", "key.encabezadoError", "key.errorEnviarCorreo");
        }
    }

    public void empezarJuego() {
        short codigoPartida = partida.getCodigoInvitacion();
        PreguntaCliente pregunta = partida.obtenerPrimerPregunta();
        socket.emit("enviarSiguientePregunta", codigoPartida, pregunta);
    }
    
    public void enviarPreguntaAJugadores() {
        short codigoPartida = partida.getCodigoInvitacion();
        PreguntaCliente pregunta = partida.obtenerPrimerPregunta();
        socket.emit("enviarSiguientePregunta", codigoPartida, pregunta);
    }
    
    public void enviarRespuestasAJugadores() {
        short codigoPartida = partida.getCodigoInvitacion();
        ArrayList<RespuestaCliente> respuestas = partida.obtenerRespuestasPreguntaActual();
        socket.emit("enviarRespuestasDePregunta", codigoPartida, respuestas);
    }
    
    public void enviarGraficaRepuestasAJugadores() {
        short codigoPartida = partida.getCodigoInvitacion();
        GraficaRespuestas grafica = partida.getGraficaPreguntaActual();
        socket.emit("enviarGraficaRepuestas", codigoPartida, grafica);
    }
    
    public void enviarMarcadorAJugadores() {
        short codigoPartida = partida.getCodigoInvitacion();
        ArrayList<PuntajeJugador> marcador = partida.getMarcador();
        socket.emit("enviarMarcador", codigoPartida, marcador);
    }
}
