import java.rmi.*;			

public class client
{
	public static void main( String arg[] ) throws Exception
	{
		if( arg.length < 2 )
		{					
			System.out.println( "Specify hostname and power (and optionally registry port)." );
			return;
		}
		
		String	hostName = arg[ 0 ];			/* Server */
		Float	sPower	 = new Float( arg[ 1 ] );
		float	mean[]	 = new float [ 1 ];
		float	power	 = sPower.floatValue();		/* Power */
		final 	int DIM  = 5;						
		float 	vector[] = new float[ DIM ];
		
		/* initialize vector data */
		mean[ 0 ] = 0;
		for( int i = 0; i < DIM; i++ )	vector[ i ] = i + 1;

		try
		{
			/* start security manager */
			if( System.getSecurityManager() == null )
				System.setSecurityManager( new SecurityManager() );

			/* obtain reference to remote method interface */
/*** Step 1:
   * Get reference to the stub object (obj) representing RMI interface
   * by calling lookup method of Naming class and providing server's URL.
   */
			PMiface obj = ( PMiface ) Naming.lookup( "//" 
				+ hostName + (arg.length>2?":" + arg[2]:"") 
				+  "/PowerMean" );

			/* prepare "parameters" RMI argument object */

/*** Step 2:
   * Prepare PMdata "parameters" object with x, Mean, power fields.
   */
			float x[] = new float[ vector.length ];	
			PMdata parameters = new PMdata();
			parameters.x = x;
			parameters.Mean = mean;		
			parameters.Power = power;		
			System.arraycopy( vector, 0, parameters.x, 0, 
								x.length );
			/* execute RMI call */

/*** Step 3:
   * Call remote method of obj object. 
   */
			parameters = obj.PowerMean( parameters );	

			/* print results from returned parameters object */

/*** Step 4:
   * Print results obtained by RMI.
   */
			System.out.print( " *** Server " + hostName + ":" );
			for( int i = 0; i < parameters.x.length; i++ )
			{
				System.out.print( " " + parameters.x[ i ] );
			}
			System.out.println();
			System.out.println( " Power " + power 
				+ " Mean of vector: " + parameters.Mean[ 0 ] );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return;
		}
	}
}
