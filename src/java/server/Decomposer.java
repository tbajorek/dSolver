package server;

import common.Matrix;
import common.DecomposeIface;
import common.Matrix;
import server.*;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

import java.net.URL;
import java.net.URLClassLoader;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Decomposer extends UnicastRemoteObject implements common.DecomposeIface
{
    public Decomposer() throws RemoteException {
        super();
    }
    
    public native static int decomposeWrapper(double a[], int rows, int cols);

    @Override
    public Matrix decompose(Matrix m) throws RemoteException {
        int errorCode =  decomposeWrapper(m.a, m.rows, m.cols);
        System.out.println("Status operacji dekompozycji LU: "+errorCode);
        return m;
    }
    
    static {
        System.loadLibrary("DecomposeWrapper");
    }
    
    public static void main( String args[] ) throws Exception {
        String hostName = InetAddress.getLocalHost().getHostName();
        String port = args[0];

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Decomposer server = new Decomposer();

        Registry reg = LocateRegistry.createRegistry(Integer.valueOf(port));
        String str = "//" + hostName + ":" + port + "/decompose";
        reg.bind(str, server);
        System.err.println("Serwer zostal uruchomiony.");
    }
}
