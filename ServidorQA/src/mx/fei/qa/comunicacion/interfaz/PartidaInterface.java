/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.interfaz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import mx.fei.qa.dominio.actores.Jugador;
import mx.fei.qa.partida.Partida;

/**
 * Métodos para el objeto RMI encargado de la administración de las partidas
 *
 * @version 1.0 28 Oct 2018
 * @author Carlos Onorio
 */
public interface PartidaInterface extends Remote {

    public Partida crearPartida(String dueñoCuestionario, String nombreCuestionario) throws RemoteException;

    public boolean finalizarPartida(long codigoInvitacion) throws RemoteException;

    public boolean unirAPartida(long codigoInvitacion, Jugador jugador) throws RemoteException;

    public boolean validarCodigoInvitacion(long codigoInvitacion) throws RemoteException;
}
