/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.servidor;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.qa.comunicacion.interfaz.CuestionarioInterface;
import mx.fei.qa.comunicacion.interfaz.PartidaInterface;
import mx.fei.qa.dominio.actores.Jugador;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;
import mx.fei.qa.partida.Partida;

/**
 * Proceso servidor encargado de la administración de las partidas nuevas y las
 * ya creadas en el sistema
 *
 * @version 1.0 2 Nov 2018
 * @author Carlos Onorio
 */
public class ServidorPartidaRMI implements PartidaInterface {

    private ArrayList<Partida> partidasActuales;

    /**
     * Se inicializa y registra una nueva partida en el sistema para jugar un
     * cuestionario elegido por un usuario. Se utiliza el nombre del usuario y
     * el nombre del cuestionario como identificadores para recuperar el
     * cuestionario
     *
     * @param autorCuestionario Usuario que registro el cuestionario a jugar
     * @param nombreCuestionario Nombre del cuestionario que se jugará
     * @return True si la partida se creó exitosamente, False si no fué así
     * @throws RemoteException Lanzada si ocurre algún problema en la conexión
     * cliente-servidor
     */
    @Override
    public short crearPartida(String autorCuestionario, String nombreCuestionario) throws RemoteException {
        short codigoInvitacion = -1;
        Registry registro = LocateRegistry.getRegistry();
        try {
            CuestionarioInterface stubCuestionario = (CuestionarioInterface) registro.lookup("servidorCuestionarios");
            CuestionarioCliente cuestionarioAJugar = stubCuestionario.recuperarCuestionario(autorCuestionario, nombreCuestionario);
            Partida partida = new Partida(cuestionarioAJugar);
            partida.generarCodigoInvitacion();
            while (validarCodigoInvitacion(partida.getCodigoInvitacion())) {
                partida.generarCodigoInvitacion();
            }
            partidasActuales.add(partida);
            codigoInvitacion = partida.getCodigoInvitacion();
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(ServidorPartidaRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigoInvitacion;
    }

    /**
     * Remueve de la lista de partidas vigentes aquella que su código de
     * invitación coincida con el indicado en el parametro codigoInvitacion
     *
     * @param codigoInvitacion Código de invitación de la partida que será
     * finalizada
     * @return True si la partida fue removida, False si no fue así.
     * @throws RemoteException Lanzada si ocurre algún problema en la conexión
     * cliente-servidor
     */
    @Override
    public boolean finalizarPartida(short codigoInvitacion) throws RemoteException {
        boolean finalizacionExitosa = false;

        for (int a = 0; a < partidasActuales.size(); a++) {
            if (partidasActuales.get(a).getCodigoInvitacion() == codigoInvitacion) {
                partidasActuales.remove(a);
                finalizacionExitosa = true;
                break;
            }
        }

        return finalizacionExitosa;
    }

    /**
     * Une un nuevo jugador a una partida existente en el sistema si el código
     * de invitación que ingresó es válido
     *
     * @param codigoInvitacion Código de invitación a la partida que se desea
     * unir
     * @param jugador Datos del jugador a unir a la partida
     * @return True si el jugador se agregó exitosamente a la partida, False si
     * no fué así
     * @throws RemoteException Lanzada si ocurre algún problema en la conexión
     * cliente-servidor
     */
    @Override
    public boolean unirAPartida(short codigoInvitacion, Jugador jugador) throws RemoteException {
        boolean jugadorAgregado = false;

        if (validarCodigoInvitacion(codigoInvitacion)) {
            for (Partida partida : partidasActuales) {
                if (partida.getCodigoInvitacion() == codigoInvitacion) {
                    partida.agregarJugador(jugador);
                    jugadorAgregado = true;
                    break;
                }
            }
        }

        return jugadorAgregado;
    }

    /**
     * Verifica si el codigo de invitación pasado como parámetro está asociado a
     * una partida vigente en el sistema
     *
     * @param codigoInvitacion Código de invitación a validar
     * @return True si el código de invitación esta asociado a una partida,
     * False si no está asociado a ninguna partida
     * @throws RemoteException Lanzada si ocurre algún problema en la conexión
     * cliente-servidor
     */
    @Override
    public boolean validarCodigoInvitacion(short codigoInvitacion) throws RemoteException {
        boolean codigoInvitacionExistente = false;

        for (Partida partida : partidasActuales) {
            if (partida.getCodigoInvitacion() == codigoInvitacion) {
                codigoInvitacionExistente = true;
                break;
            }
        }

        return codigoInvitacionExistente;
    }
    
    /**
     * Devuele la partida que tenga el código de invitación pasado como
     * parámetro
     *
     * @param codigoInvitacion Código de invitación de la partida a recuperar
     * @return Datos de la Partida
     * @throws RemoteException Lanzada si ocurre algún fallo en la conexión
     * cliente-servidor
     */
    @Override
    public Partida recuperarPartida(short codigoInvitacion) throws RemoteException {
        Partida partida = null;
        for (int a = 0; a < partidasActuales.size(); a++) {
            Partida partidaActual = partidasActuales.get(a);
            if (partidaActual.getCodigoInvitacion() == codigoInvitacion) {
                partida = partidaActual;
            }
        }
        return partida;
    }

    public ServidorPartidaRMI() {
        this.partidasActuales = new ArrayList<>();
    }

}
