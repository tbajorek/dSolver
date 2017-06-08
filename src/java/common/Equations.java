package common;

import java.io.*;

/**
 * Struct describes a system of linear equations
 */
public class Equations implements Serializable {
    /**
     * Matrix of factors
     */
    public Matrix A;
    
    /**
     * Vector of coefficients terms
     */
    public Vector b;
    
    /**
     * Vector of unknowns
     */
    public Vector x;
    
    /**
     * Initialization of the structure
     * @param A Matrix of factors
     * @param x Vector of unknowns
     * @param b Vector of coefficients terms
     */
    public Equations(Matrix A, Vector x, Vector b) {
        this.A = A;
        this.x = x;
        this.b = b;
    }
}
