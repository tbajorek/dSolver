#include <stdlib.h>
#include <stdio.h>
#include <math.h>

int PowerMean( float *pVector, int length, float Power, float *Mean )
{
	int i, errorCode;
	
	/* calculate power mean */
	*Mean = 0.0;

	printf(" start *** mean= %f power= %f\n", *Mean,  Power );

        for ( i = 0 ; i < length ; i++ ) 
        {
		*(pVector + i)  = pow( *(pVector + i), Power ); 

		*Mean += *(pVector + i);

	//	printf(" i= %d val=%lf \n", i, *(pVector + i) );
	}
	
	*Mean = pow( *Mean, 1.0/Power );
        
	printf(" PowerMean.c *** mean = %f \n", *Mean );
	errorCode = 0 ;

	return( errorCode );
}
