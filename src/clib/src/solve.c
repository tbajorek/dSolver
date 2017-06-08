#include <stdlib.h>
#include <stdio.h>
#include <math.h>

/**
 * Get a value fron the given matrix position
 * @param a Values of fraction's matrix
 * @param row Number of a row with requested value
 * @param col Number of a column with requested value
 * @param cols Number of all columns in a matrix
 * @return 
 */
double getMValue(double *a, int row, int col, int cols) {
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
double setMValue(double *a, int row, int col, int cols, double value) {
    return a[(row-1)*cols + col - 1] = value;
}

/**
 * Get a value fron the given vector position
 * @param v Values of a vector
 * @param pos Position in a vector
 * @return 
 */
double getVValue(double *v, int pos) {
    return v[pos - 1];
}

/**
 * Set the given value to the position
 * @param v Values of a vector
 * @param pos Position in a vector
 * @param value Value to set
 * @return 
 */
double setVValue(double *v, int pos, double value) {
    return v[pos - 1] = value;
}

/**
 * Calculate sum of columns on the left side of the given column number
 * @param a Values of a fraction's matrix
 * @param x Values of unknown's vector
 * @param i A number of the column
 * @param cols Number of fraction's matrix columns
 * @return 
 */
double calculateLsum(double *a, double *x, int i, int cols) {
    int j;
    double sum = 0.0;
    for(j = 1; j < i; ++j) {
        sum += getMValue(a, i, j, cols)*getVValue(x, j);
    }
    return sum;
}

/**
 * Calculate sum of columns on the right side of the given column number
 * @param a Values of a fraction's matrix
 * @param x Values of unknown's vector
 * @param i A number of the column
 * @param rows Number of fraction's matrix rows
 * @param cols Number of fraction's matrix columns
 * @return 
 */
double calculateRsum(double *a, double *x, int i, int rows, int cols) {
    int j;
    double sum = 0.0;
    for(j = i + 1; j <= rows; ++j) {
        sum += getMValue(a, i, j, cols)*getVValue(x, j);
    }
    return sum;
}

/**
 * Solve the given system of linear equations
 * @param a Values of a fraction's matrix
 * @param rows Number of fraction's matrix rows
 * @param cols Number of fraction's matrix columns
 * @param x Values of unknown's vector
 * @param b Values of coefficients' vector
 * @return 
 */
int solve(double *a, int rows, int cols, double *x, double *b) {
    int i,j,k,s, errorCode;
    //STEP 1 - calculating y
    setVValue(x, 1, getVValue(b, 1));//first value of Y
    #pragma omp parallel for
    for (i = 2; i <= rows; ++i) {//for next n values of Y
        setVValue(x, i, getVValue(b, i) - calculateLsum(a, x, i, cols));
    }
    //STEP 2
    setVValue(x, rows, getVValue(x, rows)/getMValue(a, rows, rows, cols));//last value of X
    #pragma omp parallel for
    for (i = rows - 1; i > 0; --i) {//every next value of X
        setVValue(x, i, (getVValue(x, i) - calculateRsum(a, x, i, rows, cols))/getMValue(a, i, i, cols));
    }
    errorCode = 0 ;
    return( errorCode );
}
