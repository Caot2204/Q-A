/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.servidor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.dominio.cuestionario.PreguntaCliente;
import mx.fei.qa.dominio.cuestionario.RespuestaCliente;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Carlos Onorio
 */
public class ServidorCuestionarioRMITest {

    private CuestionarioCliente cuestionario;
    private ArrayList<PreguntaCliente> preguntas;
    private final ServidorCuestionarioRMI servidor;

    public ServidorCuestionarioRMITest() {
        servidor = new ServidorCuestionarioRMI();
    }

    @Before
    public void crearCuestionario() {
        cuestionario = new CuestionarioCliente();
        preguntas = new ArrayList<>();
        crearYAgregarPrimerPregunta();
        crearYAgregarSegundaPregunta();
        crearYAgregarTerceraPregunta();
        crearYAgregarCuartaPregunta();
        crearYAgregarQuintaPregunta();

        cuestionario.setAutor("caot2204");
        cuestionario.setNombre("Días festivos en México");
        cuestionario.setPreguntas(preguntas);
        cuestionario.setUltimoGanador(null);
        cuestionario.setVecesJugado(0);
    }

    @Test
    public void guardarCuestionarioSinImagenes() {
        boolean valorEsperado = true;
        try {
            boolean valorObtenido = servidor.guardarCuestionario(cuestionario);
            assertEquals("Prueba guardar cuestionario sin imágenes", valorEsperado, valorObtenido);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorCuestionarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void eliminarCuestionario() {
        boolean valorEsperado = true;
        try {
            boolean valorObtenido = servidor.eliminarCuestionario(cuestionario.getAutor(), cuestionario.getNombre());
            assertEquals("Prueba eliminar cuestionario", valorEsperado, valorObtenido);
        } catch (RemoteException ex) {
            Logger.getLogger(ServidorCuestionarioRMITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void crearYAgregarPrimerPregunta() {
        RespuestaCliente respuestaA = new RespuestaCliente();
        respuestaA.setLetra('A');
        respuestaA.setDescripcion("15 de septiembre");
        respuestaA.setEsCorrecta(true);
        respuestaA.setImagen(null);

        RespuestaCliente respuestaB = new RespuestaCliente();
        respuestaB.setLetra('B');
        respuestaB.setDescripcion("10 de septiembre");
        respuestaB.setEsCorrecta(false);
        respuestaB.setImagen(null);

        RespuestaCliente respuestaC = new RespuestaCliente();
        respuestaC.setLetra('C');
        respuestaC.setDescripcion("9 de septiembre");
        respuestaC.setEsCorrecta(false);
        respuestaC.setImagen(null);

        RespuestaCliente respuestaD = new RespuestaCliente();
        respuestaD.setLetra('D');
        respuestaD.setDescripcion("15 de enero");
        respuestaD.setEsCorrecta(false);
        respuestaD.setImagen(null);

        ArrayList<RespuestaCliente> respuestas = new ArrayList<>();
        respuestas.add(respuestaA);
        respuestas.add(respuestaB);
        respuestas.add(respuestaC);
        respuestas.add(respuestaD);

        PreguntaCliente pregunta = new PreguntaCliente();
        pregunta.setNumero(1);
        pregunta.setDescripcion("¿Cuándo es la independencia de México?");
        pregunta.setImagen(null);
        pregunta.setRespuestas(respuestas);

        preguntas.add(pregunta);
    }

    private void crearYAgregarSegundaPregunta() {
        RespuestaCliente respuestaA = new RespuestaCliente();
        respuestaA.setLetra('A');
        respuestaA.setDescripcion("21 de noviembre");
        respuestaA.setEsCorrecta(false);
        respuestaA.setImagen(null);

        RespuestaCliente respuestaB = new RespuestaCliente();
        respuestaB.setLetra('B');
        respuestaB.setDescripcion("19 de noviembre");
        respuestaB.setEsCorrecta(false);
        respuestaB.setImagen(null);

        RespuestaCliente respuestaC = new RespuestaCliente();
        respuestaC.setLetra('C');
        respuestaC.setDescripcion("20 de noviembre");
        respuestaC.setEsCorrecta(true);
        respuestaC.setImagen(null);

        RespuestaCliente respuestaD = new RespuestaCliente();
        respuestaD.setLetra('D');
        respuestaD.setDescripcion("22 de noviembre");
        respuestaD.setEsCorrecta(false);
        respuestaD.setImagen(null);

        ArrayList<RespuestaCliente> respuestas = new ArrayList<>();
        respuestas.add(respuestaA);
        respuestas.add(respuestaB);
        respuestas.add(respuestaC);
        respuestas.add(respuestaD);

        PreguntaCliente pregunta = new PreguntaCliente();
        pregunta.setNumero(2);
        pregunta.setDescripcion("¿Cuándo fué la revolución mexicana?");
        pregunta.setImagen(null);
        pregunta.setRespuestas(respuestas);

        preguntas.add(pregunta);
    }

    private void crearYAgregarTerceraPregunta() {
        RespuestaCliente respuestaA = new RespuestaCliente();
        respuestaA.setLetra('A');
        respuestaA.setDescripcion("10 de octubre");
        respuestaA.setEsCorrecta(false);
        respuestaA.setImagen(null);

        RespuestaCliente respuestaB = new RespuestaCliente();
        respuestaB.setLetra('B');
        respuestaB.setDescripcion("5 de noviembre");
        respuestaB.setEsCorrecta(false);
        respuestaB.setImagen(null);

        RespuestaCliente respuestaC = new RespuestaCliente();
        respuestaC.setLetra('C');
        respuestaC.setDescripcion("20 de noviembre");
        respuestaC.setEsCorrecta(false);
        respuestaC.setImagen(null);

        RespuestaCliente respuestaD = new RespuestaCliente();
        respuestaD.setLetra('D');
        respuestaD.setDescripcion("2 de noviembre");
        respuestaD.setEsCorrecta(true);
        respuestaD.setImagen(null);

        ArrayList<RespuestaCliente> respuestas = new ArrayList<>();
        respuestas.add(respuestaA);
        respuestas.add(respuestaB);
        respuestas.add(respuestaC);
        respuestas.add(respuestaD);

        PreguntaCliente pregunta = new PreguntaCliente();
        pregunta.setNumero(3);
        pregunta.setDescripcion("¿Cuándo es el día de muertos?");
        pregunta.setImagen(null);
        pregunta.setRespuestas(respuestas);

        preguntas.add(pregunta);
    }

    private void crearYAgregarCuartaPregunta() {
        RespuestaCliente respuestaA = new RespuestaCliente();
        respuestaA.setLetra('A');
        respuestaA.setDescripcion("10 de abril");
        respuestaA.setEsCorrecta(false);
        respuestaA.setImagen(null);

        RespuestaCliente respuestaB = new RespuestaCliente();
        respuestaB.setLetra('B');
        respuestaB.setDescripcion("10 de mayo");
        respuestaB.setEsCorrecta(true);
        respuestaB.setImagen(null);

        RespuestaCliente respuestaC = new RespuestaCliente();
        respuestaC.setLetra('C');
        respuestaC.setDescripcion("10 de enero");
        respuestaC.setEsCorrecta(false);
        respuestaC.setImagen(null);

        RespuestaCliente respuestaD = new RespuestaCliente();
        respuestaD.setLetra('D');
        respuestaD.setDescripcion("10 de marzo");
        respuestaD.setEsCorrecta(false);
        respuestaD.setImagen(null);

        ArrayList<RespuestaCliente> respuestas = new ArrayList<>();
        respuestas.add(respuestaA);
        respuestas.add(respuestaB);
        respuestas.add(respuestaC);
        respuestas.add(respuestaD);

        PreguntaCliente pregunta = new PreguntaCliente();
        pregunta.setNumero(4);
        pregunta.setDescripcion("¿Cuándo es el día de las madres?");
        pregunta.setImagen(null);
        pregunta.setRespuestas(respuestas);

        preguntas.add(pregunta);
    }

    private void crearYAgregarQuintaPregunta() {
        RespuestaCliente respuestaA = new RespuestaCliente();
        respuestaA.setLetra('A');
        respuestaA.setDescripcion("15 de mayo");
        respuestaA.setEsCorrecta(false);
        respuestaA.setImagen(null);

        RespuestaCliente respuestaB = new RespuestaCliente();
        respuestaB.setLetra('B');
        respuestaB.setDescripcion("22 de marzo");
        respuestaB.setEsCorrecta(false);
        respuestaB.setImagen(null);

        RespuestaCliente respuestaC = new RespuestaCliente();
        respuestaC.setLetra('C');
        respuestaC.setDescripcion("6 de enero");
        respuestaC.setEsCorrecta(true);
        respuestaC.setImagen(null);

        RespuestaCliente respuestaD = new RespuestaCliente();
        respuestaD.setLetra('D');
        respuestaD.setDescripcion("2 de noviembre");
        respuestaD.setEsCorrecta(false);
        respuestaD.setImagen(null);

        ArrayList<RespuestaCliente> respuestas = new ArrayList<>();
        respuestas.add(respuestaA);
        respuestas.add(respuestaB);
        respuestas.add(respuestaC);
        respuestas.add(respuestaD);

        PreguntaCliente pregunta = new PreguntaCliente();
        pregunta.setNumero(5);
        pregunta.setDescripcion("¿Cuándo es el día de reyes magos");
        pregunta.setImagen(null);
        pregunta.setRespuestas(respuestas);

        preguntas.add(pregunta);
    }

}
