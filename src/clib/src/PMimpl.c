
#include <jni.h>
#include "server_PMimpl.h"

/* declare computational function implemented elsewhere */
int PowerMean( float *pVector, int vec_len, float Power, float *pMean );

/* provide JNI style definition of Java callable C function */
JNIEXPORT jint JNICALL	Java_server_PMimpl_PowerMeanWrapper(
	/* JNI environment structure */	JNIEnv      *pEnv,
	/* native method's object */	jobject      object,
	/* native method's arguments */	jfloatArray  vector,
					jfloat       Power,
 					jfloatArray  mean	)
{
	/* unpack argumrents from JNI transport structures */

	jsize vec_len = ( *pEnv ) -> GetArrayLength( pEnv, vector );
	jfloat *pVector = ( *pEnv ) -> GetFloatArrayElements( pEnv, vector, 0);
	jsize mean_len = ( *pEnv ) -> GetArrayLength( pEnv, mean );
	jfloat *pMean = ( *pEnv ) -> GetFloatArrayElements( pEnv, mean, 0 );

	/* execute the stuff */
	int errorCode = PowerMean( pVector, vec_len, Power, pMean);
	printf(" PMimpl.c *** mean = %f  \n", *pMean );

	/* pack results back into JNI structures */

	( *pEnv )->ReleaseFloatArrayElements( pEnv, vector, pVector, 0 );
	( *pEnv )->ReleaseFloatArrayElements( pEnv, mean, pMean, 0 );

	return( errorCode );
}
