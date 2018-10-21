/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorqa;

import comunicacion.interfaz.CuentaUsuarioInterface;
import comunicacion.servidor.ServidorCuentaUsuario;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos Onorio
 */
public class ServidorQA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServidorCuentaUsuario servidorCuentas = new ServidorCuentaUsuario();
        try {
            CuentaUsuarioInterface stub = (CuentaUsuarioInterface) UnicastRemoteObject.exportObject(servidorCuentas, 0);
            Registry registro = LocateRegistry.getRegistry();
            registro.bind("servidorCuentasUsuario", stub);
            System.out.println("ServidorCuentasUsuario escuchando....");
        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(ServidorQA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
