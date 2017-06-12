#include <jni.h>
#include "server_Decomposer.h"

/* declare computational function implemented elsewhere */
int solve(double *a, int rows, int cols, double *x, double *b);

/* provide JNI style definition of Java callable C function */
JNIEXPORT jint JNICALL	Java_server_Solver_solveWrapper(
                            JNIEnv *pEnv,
                            jobject object,
                            jdoubleArray a,
                            jint rows,
                            jint cols,
                            jdoubleArray x,
                            jdoubleArray b)
{
	jdouble *pA = ( *pEnv ) -> GetDoubleArrayElements( pEnv, a, 0);
        jdouble *pX = ( *pEnv ) -> GetDoubleArrayElements( pEnv, x, 0);
        jdouble *pB = ( *pEnv ) -> GetDoubleArrayElements( pEnv, b, 0);
	int errorCode = solve(pA, rows, cols, pX, pB);
	( *pEnv )->ReleaseDoubleArrayElements( pEnv, x, pX, 0 );
	return( errorCode );
}
