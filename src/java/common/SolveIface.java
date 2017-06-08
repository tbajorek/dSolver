package common;

import java.rmi.*;

/**
 * Interface of solving server
 */
public interface SolveIface extends Remote
{
    /**
     * Solve the given matrix and return result of its computing
     * @param eq System of linear equations to solve
     * @return
     * @throws RemoteException 
     */
    public Equations solve(Equations eq) throws RemoteException;
}
