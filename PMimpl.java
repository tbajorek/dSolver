



import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class PMimpl extends UnicastRemoteObject implements PMiface
{
	/* constructor */
	public PMimpl() throws RemoteException
		{ 
			super(); 	/* call parent class constructor,
			                   it will create server skeleton */
		}

	/* declare external C function as this class'es method (native) */

/*** Step 1:
   * Provide Java side declaration of JNI native (C implemented) method 
   * (named i.e. PowerMeanWrapper) performing actual calculations on 
   * vector[], Power, Mean[] arguments and returning dummy errorCode.
   */

	public native int PowerMeanWrapper( float vector[],
					    float Power,
					    float Mean[]  );


	/* implement method declared in PMiface interface */

/*** Step 2:
   * Implement RMI method (server side) declared in the RMI interface
   * throwing RemoteException class exceptions. This method calls
   * JNI native method (PowerMeanWrapper - declared in step 1) 
   * to do the calculations. Parameters called by reference may change
   * its values.
   */

	public PMdata PowerMean( PMdata parameters ) throws RemoteException
	{
		// execute JNI call 
		int errorCode =  PowerMeanWrapper( parameters.x, 
					parameters.Power, parameters.Mean );

		System.out.println( " PMimpl.java *** Mean = "
					+ parameters.Mean[ 0 ] );
		return parameters;
	}

	/* load dynamic system library containing native function */

/*** Step 3:
   * Load dynamically linked library (with native C function implementation).
   */

	static
	{
		System.loadLibrary( "PowerMeanWrapper" );
	}

	/* register server in RMI registry */

	public static void main( String args[] ) throws Exception {
		String hostName = InetAddress.getLocalHost().getHostName();
		
		/* start security manager */
		if( System.getSecurityManager() == null )
			System.setSecurityManager( new SecurityManager() );

        /* bind server implemented object to URL (with optional port)
		   in RMI registry on local hostName:port */

/*** Step 4:
   * Register this class object creation with specific URL in RMI registry
   * by rebinding //server_host_name/path/name with new object of this class.
   */
		Naming.rebind( "//" + hostName + 
			(args.length>0?":" + args[0]:"") + "/PowerMean", 
								new PMimpl() );

	}
}


