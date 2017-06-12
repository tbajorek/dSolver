package server;

import common.DecomposeIface;
import common.Matrix;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * Server to decompose fraction's matrix to LU matrix
 */
public class Decomposer extends UnicastRemoteObject implements DecomposeIface
{
    /**
     * Initialization of the decomposer server
     * @throws RemoteException 
     */
    public Decomposer() throws RemoteException {
        super();
    }
    
    /**
     * Wrapper of a native function to decompose the given matrix
     * @param a Values of a fraction's matrix
     * @param rows Number of rows
     * @param cols Number of columns
     * @return 
     */
    public native static int decomposeWrapper(double a[], int rows, int cols);

    /**
     * Decompose the passed matrix to LU matrix
     * @param m Matrix to decompose
     * @return
     * @throws RemoteException 
     */
    @Override
    public Matrix decompose(Matrix m) throws RemoteException {
        long startTime = System.currentTimeMillis();
        int errorCode =  decomposeWrapper(m.a, m.rows, m.cols);
        long stopTime = System.currentTimeMillis();
        System.out.println("Status operacji dekompozycji LU: "+errorCode+", czas wykonania: "+(stopTime-startTime));
        return m;
    }
    
    /**
     * Loading of a native library
     */
    static {
        System.loadLibrary("DecomposeWrapper");
    }
    
    /**
     * Main function of the decomposer server
     * @param args Arguments passed by command line
     */
    public static void main( String args[] ) {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String port = args[0];

            if(System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            Decomposer server = new Decomposer();

            Registry reg = LocateRegistry.createRegistry(Integer.valueOf(port));
            String str = "//" + hostName + ":" + port + "/decompose";
            reg.bind(str, server);
            System.out.println("Serwer zostal uruchomiony.");
            System.out.println("Lokalizacja: "+str);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
