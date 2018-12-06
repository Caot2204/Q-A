/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.utileria;

import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Carlos Onorio
 */
public class UtileriaCorreo {
    
    /**
     * Notifica que es una clase de utilidades y no puede ser instanciada.
     */
    private UtileriaCorreo() {
        throw new IllegalStateException("Clase de utilidades para correos");
    }


    /**
     * Envía un correo electrónico al destinatario con los datos de los
     * parámetros.
     *
     * @param correoDestinatario Correo electrónico destinarario
     * @param asunto Asunto del correo
     * @param mensaje Cuerpo del correo
     */
    private static void enviarCorreo(String correoDestinatario, String asunto, String mensaje) {
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
            Message correo = new MimeMessage(sesion);

            correo.setFrom(new InternetAddress(correoDelJuego));
            correo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestinatario));
            correo.setSubject(asunto);
            correo.setText(mensaje);

            Transport.send(correo);

        } catch (MessagingException ex) {
            UtileriaInterfazUsuario.mostrarMensajeError("key.mensajeDeSistema",
                    "key.encabezadoError", "key.errorEnviarCorreo");
        }
    }

    /**
     * Envía al destinatario el código de invitación a una partida.
     *
     * @param destinatario Correo electrónico a enviar el código
     * @param codigoInvitacion Código de invitación a una partida
     */
    public static void enviarCorreoCodigoInvitacion(String destinatario, int codigoInvitacion) {
        ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
        String asunto = recursoIdioma.getString("key.asuntoInvitacion");
        String cuerpo = recursoIdioma.getString("key.cuerpoInvitacion")
                + "\n\n" + Integer.toString(codigoInvitacion);
        enviarCorreo(destinatario, asunto, cuerpo);
    }

    /**
     * Envía al destinatario el código de invitación a una partida.
     *
     * @param destinatario Correo electrónico a enviar el código
     * @param codigoConfirmacion Código de confirmación para completar el
     * registro de un usuario
     */
    public static void enviarCorreoCodigoConfirmacion(String destinatario, int codigoConfirmacion) {
        ResourceBundle recursoIdioma = UtileriaInterfazUsuario.recuperarRecursoIdiomaCliente();
        String asunto = recursoIdioma.getString("key.asuntoConfirmacion");
        String cuerpo = recursoIdioma.getString("key.cuerpoConfirmacion")
                + "\n\n" + Integer.toString(codigoConfirmacion);
        enviarCorreo(destinatario, asunto, cuerpo);
    }

}
