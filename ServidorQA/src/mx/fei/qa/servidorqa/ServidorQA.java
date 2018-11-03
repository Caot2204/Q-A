/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.fei.qa.servidorqa;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.qa.comunicacion.interfaz.CuentaUsuarioInterface;
import mx.fei.qa.comunicacion.interfaz.CuestionarioInterface;
import mx.fei.qa.comunicacion.interfaz.PartidaInterface;
import mx.fei.qa.comunicacion.servidor.ServidorCuentaUsuarioRMI;
import mx.fei.qa.comunicacion.servidor.ServidorCuestionarioRMI;
import mx.fei.qa.comunicacion.servidor.ServidorPartidaRMI;

/**
 *
 * @author Carlos Onorio
 */
public class ServidorQA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServidorCuentaUsuarioRMI servidorCuentas = new ServidorCuentaUsuarioRMI();
        ServidorCuestionarioRMI servidorCuestionario = new ServidorCuestionarioRMI();
        ServidorPartidaRMI servidorPartidas = new ServidorPartidaRMI();
        
        try {
            CuentaUsuarioInterface stubCuentaUsuario = (CuentaUsuarioInterface) UnicastRemoteObject.exportObject(servidorCuentas, 0);
            CuestionarioInterface stubCuestionario = (CuestionarioInterface) UnicastRemoteObject.exportObject(servidorCuestionario, 0);
            PartidaInterface stubPartida = (PartidaInterface) UnicastRemoteObject.exportObject(servidorPartidas, 0);
            
            Registry registro = LocateRegistry.getRegistry();
            registro.bind("servidorCuentasUsuario", stubCuentaUsuario);
            registro.bind("servidorCuestionarios", stubCuestionario);
            registro.bind("servidorPartidas", stubPartida);
            
            System.out.println("ServidorCuentasUsuario escuchando....");
            System.out.println("ServidorCuestionarios escuchando....");
            System.out.println("ServidorPartidas escuchando....");
        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(ServidorQA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
