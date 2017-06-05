package common;

import java.rmi.*;

public interface SolveIface extends Remote
{
    public Equations solve(Equations eq) throws RemoteException;
}
