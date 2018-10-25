/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacion.interfaz;

import dominio.cuestionario.CuestionarioCliente;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Carlos Onorio
 */
public interface CuestionarioInterface extends Remote{
    public boolean guardarCuestionario(CuestionarioCliente cuestionario) throws RemoteException;
    public CuestionarioCliente recuperarCuestionario(long id) throws RemoteException;
    public boolean editarCuestionario(CuestionarioCliente cuestionario) throws RemoteException;
    public boolean eliminarCuestionario(long id) throws RemoteException; 
}
