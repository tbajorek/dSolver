package client;

import java.rmi.*;
import common.*;
import java.io.FileNotFoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * Client class which provides all client's functionality
 */
public class Client {

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
        } catch(ConnectException e) {
            System.out.println("Blad polaczenia z zadanym serwerem. Sprawdz, czy jest on uruchomiony i osiagalny.");
        } catch(Exception e) {
            System.out.println("Wystapil nieoczekiwany wyjatek. Aplikacja zostanie zamknieta.");
            e.printStackTrace();
        }
    }
    
    /**
     * Decompose the given matrix using server running on the passed hostname and port
     * @param hostname Host name of a decomposition server
     * @param port Port of the server
     * @param a Matrix to decompose
     * @return
     * @throws RemoteException
     * @throws NotBoundException 
     */
    private static Matrix decompose(String hostname, String port, Matrix a) throws RemoteException, NotBoundException {
        String location = "//" + hostname + ":" + port + "/decompose";
        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
        DecomposeIface remoteServer = (DecomposeIface) registry.lookup(location);
        return remoteServer.decompose(a);
    }

    /**
     * Solve the given system of linear equations using server running on the passed hostname and port
     * @param hostname Host name of a decomposition server
     * @param port Port of the server
     * @param eq System of equations to solve
     * @return
     * @throws RemoteException
     * @throws NotBoundException 
     */
    private static Equations solve(String hostname, String port, Equations eq) throws RemoteException, NotBoundException {
        String location = "//" + hostname + ":" + port + "/solve";
        Registry registry = LocateRegistry.getRegistry(Integer.valueOf(port));
        SolveIface remoteServer = (SolveIface) registry.lookup(location);
        return remoteServer.solve(eq);
    }
}
