package common;

import java.io.*;

public class Equations implements Serializable {
    public Matrix A;
    public Vector b;
    public Vector x;
    
    public Equations(Matrix A, Vector x, Vector b) {
        this.A = A;
        this.x = x;
        this.b = b;
    }
}
