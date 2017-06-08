package common;

import java.io.*;
import java.util.Scanner;

/**
 * A structure of matrix used in the application
 */
public class Matrix implements Serializable {
    /**
     * An array of values
     */
    public double a[];
    
    /**
     * A number of rows of the matrix
     */
    public int rows;
    
    /**
     * A number of culumns of the matrix
     */
    public int cols;
    
    /**
     * Initialization without parameters is empty
     */
    public Matrix() {}
    
    /**
     * Load a matrix fron the given file
     * @param filename Name of file where is located a matrix
     * @throws FileNotFoundException Thrown when file wasn't found.
     * @throws MatrixLoaderException Thrown when matrix can't be loaded from file.
     */
    public Matrix(String filename) throws FileNotFoundException, MatrixLoaderException {
        readFromFile(filename);
    }
    
    /**
     * Return a value from the given position
     * @param row Number of a row
     * @param col Number of a col
     * @return
     * @throws ParamException Thrown when row or col parameter is incorrect.
     */
    public double getValue(int row, int col) throws ParamException {
        if (row > rows || row < 1) {
            throw new ParamException("Parametr row jest nieprawidlowy");
        } else if(col > cols || col < 1) {
            throw new ParamException("Parametr col jest nieprawidlowy");
        }
        return a[(row-1)*cols + col - 1];
    }
    
    /**
     * Set the given value
     * @param row Number of a row
     * @param col Number of a col
     * @param value Value to set on a given position
     * @throws ParamException Thrown when row or col parameter is incorrect.
     */
    public void setValue(int row, int col, double value) throws ParamException {
        if (row > rows || row < 1) {
            throw new ParamException("Parametr row jest nieprawidlowy");
        } else if(col > cols || col < 1) {
            throw new ParamException("Parametr col jest nieprawidlowy");
        }
        a[(row-1)*cols + col - 1] = value;
    }
    
    
    /**
     * Display the matrix on a console
     */
    public void display() {
        for(int r = 1; r <= rows; ++r) {
            for(int c = 1; c <= cols; ++c) {
                try {
                    System.out.print(getValue(r, c)+"\t");
                } catch(ParamException e) {}
            }
            System.out.println();
        }
    }
    
    /**
     * Read a matrix fron a file with the given filename
     * @param filename Name of a file
     * @throws FileNotFoundException Thrown when file wasn't found.
     * @throws MatrixLoaderException Thrown when matrix can't be loaded from file.
     */
    public final void readFromFile(String filename) throws FileNotFoundException, MatrixLoaderException {
        Scanner sc = new Scanner(new File(filename));
        rows = sc.nextInt();
        cols = sc.nextInt();
        a = new double[rows * cols];
        int pointer = -1;
        while (sc.hasNextDouble()) {
            a[++pointer] = sc.nextDouble();
        }
        if(pointer+1 != rows * cols) {
            throw new MatrixLoaderException();
        }
        sc.close();
    }
    
    /**
     * Save the matrix to a file with the given filename
     * @param filename Name of a file
     * @throws UnsupportedEncodingException Thrown when unsupported encoding is used in a file.
     * @throws IOException Thrown when an error with IO operations occured.
     */
    public void saveToFile(String filename) throws UnsupportedEncodingException, IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(filename), "utf-8")))
        {
            writer.write(rows+"\t"+cols+"\n");
            for(int r = 1; r <= rows; ++r) {
                for(int c = 1; c <= cols; ++c) {
                    try {
                        writer.write(getValue(r, c)+"\t");
                    } catch(ParamException e) {}
                }
                writer.write("\n");
            }
        }
    }
}
