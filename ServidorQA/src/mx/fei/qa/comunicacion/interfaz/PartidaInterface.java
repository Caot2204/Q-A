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
 * @version 1.0 11 Nov 2018
 * @author Carlos Onorio
 */
public interface PartidaInterface extends Remote {

    public short crearPartida(String autorCuestionario, String nombreCuestionario) throws RemoteException;

    public boolean finalizarPartida(short codigoInvitacion) throws RemoteException;

    public boolean unirAPartida(short codigoInvitacion, Jugador jugador) throws RemoteException;

    public boolean validarCodigoInvitacion(short codigoInvitacion) throws RemoteException;

    public Partida recuperarPartida(short codigoInvitacion) throws RemoteException;

}
