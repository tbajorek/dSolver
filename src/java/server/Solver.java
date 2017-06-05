package server;

import common.Equations;
import common.SolveIface;
import common.Matrix;
import server.*;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

import java.net.URL;
import java.net.URLClassLoader;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Solver extends UnicastRemoteObject implements common.SolveIface {
    public Solver() throws RemoteException {
        super();
    }
    
    public native static int solveWrapper(double a[], int rows, int cols, double x[], double b[]);
    
    @Override
    public Equations solve(Equations eq) throws RemoteException {
        int errorCode =  solveWrapper(eq.A.a, eq.A.rows, eq.A.cols, eq.x.v, eq.b.v);
        System.out.println("Status operacji rozwiazywania ukladu rownan: "+errorCode);
        return eq;
    }
    
    static {
        System.loadLibrary("SolveWrapper");
    }
    
    public static void main( String args[] ) throws Exception {
        String hostName = InetAddress.getLocalHost().getHostName();
        String port = args[0];

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Solver server = new Solver();

        Registry reg = LocateRegistry.createRegistry(Integer.valueOf(port));
        String str = "//" + hostName + ":" + port + "/solve";
        reg.bind(str, server);
        System.err.println("Serwer zostal uruchomiony.");
    }
}
