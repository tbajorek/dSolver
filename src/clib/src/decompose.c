#include <stdlib.h>
#include <stdio.h>
#include <math.h>

double getValue(double *a, int row, int col, int cols) {
    return a[(row-1)*cols + col - 1];
}

double setValue(double *a, int row, int col, int cols, double value) {
    return a[(row-1)*cols + col - 1] = value;
}

int decompose(double *a, int rows, int cols) {
    int i,j,k,s, errorCode;
    for(k=1; k <= cols-1; k++){
        // STEP 1
        for (s=k+1; s <= rows; s++) {
            setValue(a, s, k, cols, getValue(a, s, k, cols)/getValue(a, k, k, cols));
        }
        // STEP 2
        for (i = k+1; i <= cols; i++) {
            for (j = k+1; j <= rows; j++){
                setValue(a, i, j, cols, getValue(a, i, j, cols) - getValue(a, i, k, cols) * getValue(a, k, j, cols));
            }
        }
    }
    errorCode = 0 ;
    return( errorCode );
}
