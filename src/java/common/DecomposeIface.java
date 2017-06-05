package common;

import java.rmi.*;

public interface DecomposeIface extends Remote
{
    public Matrix decompose(Matrix m) throws RemoteException;
}
