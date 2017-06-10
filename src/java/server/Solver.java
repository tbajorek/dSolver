package server;

import common.Equations;
import common.SolveIface;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * Server to solve a system of linear equations
 */
public class Solver extends UnicastRemoteObject implements SolveIface {
    /**
     * Initialization of the solver server
     * @throws RemoteException 
     */
    public Solver() throws RemoteException {
        super();
    }
    
    /**
     * Wrapper of a native function to solve the given system of equations
     * @param a Values of a fraction's matrix
     * @param rows Number of rows
     * @param cols Number of columns
     * @param x Values of unknown's vector
     * @param b Values of coefficient's vector
     * @return 
     */
    public native static int solveWrapper(double a[], int rows, int cols, double x[], double b[]);
    
    /**
     * Selve the passed system of equations
     * @param eq System of equations to solve
     * @return
     * @throws RemoteException 
     */
    @Override
    public Equations solve(Equations eq) throws RemoteException {
        long startTime = System.currentTimeMillis();
        int errorCode =  solveWrapper(eq.A.a, eq.A.rows, eq.A.cols, eq.x.v, eq.b.v);
        long stopTime = System.currentTimeMillis();
        System.out.println("Status operacji rozwiazywania ukladu rownan: "+errorCode+", czas wykonania: "+(stopTime-startTime));
        return eq;
    }
    
    /**
     * Loading of a native library
     */
    static {
        System.loadLibrary("SolveWrapper");
    }
    
    /**
     * Main function of the solver server
     * @param args Arguments passed by command line
     */
    public static void main( String args[] ) {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String port = args[0];

            if(System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            Solver server = new Solver();

            Registry reg = LocateRegistry.createRegistry(Integer.valueOf(port));
            String str = "//" + hostName + ":" + port + "/solve";
            reg.bind(str, server);
            System.out.println("Serwer zostal uruchomiony.");
            System.out.println("Lokalizacja: "+str);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
