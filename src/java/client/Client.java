package client;

import java.rmi.*;
import common.*;
import java.io.FileNotFoundException;
import server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
public class Client {
    // static
    // {
    // 	System.loadLibrary("PowerMeanWrapper");
    // }

    private static Matrix decompose(String hostname, String port, Matrix a) throws RemoteException, NotBoundException {
        String location = "//" + hostname + ":" + port + "/decompose";
        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
        DecomposeIface remoteServer = (DecomposeIface) registry.lookup(location);
        return remoteServer.decompose(a);
    }

    private static Equations solve(String hostname, String port, Equations eq) throws RemoteException, NotBoundException {
        String location = "//" + hostname + ":" + port + "/solve";
        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
        SolveIface remoteServer = (SolveIface) registry.lookup(location);
        return remoteServer.solve(eq);
    }

    public static void main( String arg[] ) throws Exception
    {
        if( arg.length < 4 )
        {
            System.out.println( "Specify hostname and power registry port." );
            return;
        }

        String decomposerHostName = arg[0]; /* Decomposer server */
        String decomposerPort = arg[1]; /* Decomposer port */
        String solverHostName = arg[2]; /* Decomposer server */
        String solverPort = arg[3]; /* Decomposer port */
        String afile = (arg.length >= 5)?arg[4]:"resources/A2.txt";
        String bfile = (arg.length >= 6)?arg[5]:"resources/b2.txt";
        String xfile = (arg.length >= 7)?arg[6]:"resources/x2.txt";

        try {
            if( System.getSecurityManager() == null ) {
                System.setSecurityManager( new SecurityManager() );
            }
            Matrix a = new Matrix(afile);
            Equations eq = new Equations(a, new Vector(a.rows), new Vector(bfile));
            
            eq.A = decompose(decomposerHostName, decomposerPort, eq.A);

            eq = solve(solverHostName, solverPort, eq);
            eq.x.saveToFile(xfile);
            eq.A.saveToFile("resources/LU.txt");
        } catch(MatrixLoaderException | VectorLoaderException e) {
            System.out.println("Blad ladowania danych.");
        } catch(FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku.");
        } catch(Exception e) {
            System.out.println("Wystapil nieoczekiwany wyjatek. Aplikacja zostanie zamknieta.");
            e.printStackTrace();
        }
    }
}
