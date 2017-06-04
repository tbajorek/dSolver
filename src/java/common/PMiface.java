package common;

import java.rmi.*;

public interface PMiface extends Remote
{
   /* declare RMI method */
   public PMdata PowerMean( PMdata parameters ) throws RemoteException;
}
