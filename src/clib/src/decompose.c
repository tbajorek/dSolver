#include <stdlib.h>
#include <stdio.h>
#include <math.h>

/**
 * Get a value fron the given position
 * @param a Values of fraction's matrix
 * @param row Number of a row with requested value
 * @param col Number of a column with requested value
 * @param cols Number of all columns in a matrix
 * @return 
 */
double getValue(double *a, int row, int col, int cols) {
    return a[(row-1)*cols + col - 1];
}

/**
 * Set the value to the given position
 * @param a Values of fraction's matrix
 * @param row Number of a row with requested value
 * @param col Number of a column with requested value
 * @param cols Number of all columns in a matrix
 * @param value Value to set
 * @return 
 */
double setValue(double *a, int row, int col, int cols, double value) {
    return a[(row-1)*cols + col - 1] = value;
}

/**
 * Decompose the given matrix to LU matrix
 * @param a Values of fraction's matrix
 * @param rows Number of rows
 * @param cols Number of columns
 * @return 
 */
int decompose(double *a, int rows, int cols) {
    int i,j,k,s, errorCode;
    for(k=1; k <= cols-1; k++){
        // STEP 1
        #pragma omp parallel for
        for (s=k+1; s <= rows; s++) {
            setValue(a, s, k, cols, getValue(a, s, k, cols)/getValue(a, k, k, cols));
        }
        // STEP 2
        #pragma omp parallel for
        for (i = k+1; i <= cols; i++) {
            for (j = k+1; j <= rows; j++){
                setValue(a, i, j, cols, getValue(a, i, j, cols) - getValue(a, i, k, cols) * getValue(a, k, j, cols));
            }
        }
    }
    errorCode = 0 ;
    return( errorCode );
}
