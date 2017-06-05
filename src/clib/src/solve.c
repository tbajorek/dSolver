#include <stdlib.h>
#include <stdio.h>
#include <math.h>

double getMValue(double *a, int row, int col, int cols) {
    return a[(row-1)*cols + col - 1];
}

double setMValue(double *a, int row, int col, int cols, double value) {
    return a[(row-1)*cols + col - 1] = value;
}

double getVValue(double *v, int pos) {
    return v[pos - 1];
}

double setVValue(double *v, int pos, double value) {
    return v[pos - 1] = value;
}

double calculateLsum(double *a, double *x, int i, int cols) {
    int j;
    double sum = 0.0;
    for(j = 1; j < i; ++j) {
        sum += getMValue(a, i, j, cols)*getVValue(x, j);
    }
    return sum;
}

double calculateRsum(double *a, double *x, int i, int rows, int cols) {
    int j;
    double sum = 0.0;
    for(j = i + 1; j <= rows; ++j) {
        sum += getMValue(a, i, j, cols)*getVValue(x, j);
    }
    return sum;
}

int solve(double *a, int rows, int cols, double *x, double *b) {
    int i,j,k,s, errorCode;
    //STEP 1 - calculating y
    setVValue(x, 1, getVValue(b, 1));//first value of Y
    for (i = 2; i <= rows; ++i) {//for next n values of Y
        setVValue(x, i, getVValue(b, i) - calculateLsum(a, x, i, cols));
    }
    //STEP 2
    setVValue(x, rows, getVValue(x, rows)/getMValue(a, rows, rows, cols));//last value of X
    for (i = rows - 1; i > 0; --i) {//every next value of X
        setVValue(x, i, (getVValue(x, i) - calculateRsum(a, x, i, rows, cols))/getMValue(a, i, i, cols));
    }
    errorCode = 0 ;
    return( errorCode );
}
