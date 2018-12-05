/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.comunicacion.interfaz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import mx.fei.qa.dominio.cuestionario.CuestionarioCliente;

/**
 * Métodos para el objeto RMI encargado de la administración de los
 * Cuestionarios
 *
 * @version 1.0 28 Oct 2018
 * @author Carlos Onorio
 */
public interface CuestionarioInterface extends Remote {

    public boolean guardarCuestionario(CuestionarioCliente cuestionario) throws RemoteException;

    public CuestionarioCliente recuperarCuestionario(String autor, String nombreCuestionario) throws RemoteException;

    public List<CuestionarioCliente> recuperarCuestionariosPorAutor(String autor) throws RemoteException;

    public boolean editarCuestionario(CuestionarioCliente cuestionario) throws RemoteException;

    public boolean eliminarCuestionario(String autor, String nombreCuestionario) throws RemoteException;
}
