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
        long startAllTime = System.currentTimeMillis();
        if( arg.length < 7 )
        {
            System.out.println( "Prosze podac nazwy hostow, numery portow " +
                                "oraz sciezki do wymaganych plikow." );
            return;
        }

        String decomposerHostName = arg[0]; /* Decomposer server */
        String decomposerPort = arg[1]; /* Decomposer port */
        String solverHostName = arg[2]; /* Decomposer server */
        String solverPort = arg[3]; /* Decomposer port */
        String afile = arg[4]; /* File with A matrix */
        String bfile = arg[5]; /* File with b vector */
        String xfile = arg[6]; /* File path where computing results will be saved */
        // String afile = (arg.length >= 5)?arg[4]:"resources/A3.txt";
        // String bfile = (arg.length >= 6)?arg[5]:"resources/b3.txt";
        // String xfile = (arg.length >= 7)?arg[6]:"resources/x3.txt";

        try {
            if( System.getSecurityManager() == null ) {
                System.setSecurityManager( new SecurityManager() );
            }
            System.out.println("Ładowanie danych macierzy A...");
            Matrix a = new Matrix(afile);
            System.out.println("Ładowanie danych wektora b...");
            Equations eq = new Equations(a, new Vector(a.rows), new Vector(bfile));

            System.out.println("Rozklad LU..");
            long startDecTime = System.currentTimeMillis();
            eq.A = decompose(decomposerHostName, decomposerPort, eq.A);
            long stopDecTime = System.currentTimeMillis();

            System.out.println("Rozwiazywanie ukladu rownan...");
            long startSolTime = System.currentTimeMillis();
            eq = solve(solverHostName, solverPort, eq);
            long stopSolTime = System.currentTimeMillis();

            eq.x.saveToFile(xfile);
            eq.A.saveToFile("resources/LU.txt");
            System.out.println("Uklad zostal rozwiazany. Wyniki znajduja sie w pliku "+xfile);
            long stopAllTime = System.currentTimeMillis();
            System.out.println("-------------------------------------");
            System.out.println("Calkowity czas uruchomienia: "+(stopAllTime-startAllTime));
            System.out.println("Czas rozkladu macierzy: "+(stopDecTime-startDecTime));
            System.out.println("Czas rozwiazania ukladu: "+(stopSolTime-startSolTime));
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
