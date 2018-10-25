/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunicacion.servidor;

import comunicacion.interfaz.CuestionarioInterface;
import dominio.cuestionario.CuestionarioCliente;
import java.rmi.RemoteException;

/**
 *
 * @author Carlos Onorio
 */
public class ServidorCuestionario implements CuestionarioInterface{

    @Override
    public boolean guardarCuestionario(CuestionarioCliente cuestionario) throws RemoteException {
        boolean guardadoExitoso = false;
        
        
        
        return guardadoExitoso;
    }

    @Override
    public CuestionarioCliente recuperarCuestionario(long id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean editarCuestionario(CuestionarioCliente cuestionario) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminarCuestionario(long id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
