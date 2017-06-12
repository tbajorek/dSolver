package common;

import java.rmi.*;

/**
 * Interface of decomposing server
 */
public interface DecomposeIface extends Remote
{
    /**
     * Decompose the given matrix and return result of its computing
     * @param m Matrix to decompose
     * @return
     * @throws RemoteException 
     */
    public Matrix decompose(Matrix m) throws RemoteException;
}
