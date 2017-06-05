#include <jni.h>
#include "server_Decomposer.h"

/* declare computational function implemented elsewhere */
int decompose(double *a, int rows, int cols);

/* provide JNI style definition of Java callable C function */
JNIEXPORT jint JNICALL	Java_server_Decomposer_decomposeWrapper(
                            JNIEnv *pEnv,
                            jobject object,
                            jdoubleArray a,
                            jint rows,
                            jint cols)
{
	jdouble *pMatrix = ( *pEnv ) -> GetDoubleArrayElements( pEnv, a, 0);
	int errorCode = decompose(pMatrix, rows, cols);
	( *pEnv )->ReleaseDoubleArrayElements( pEnv, a, pMatrix, 0 );
	return( errorCode );
}
