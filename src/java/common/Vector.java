package common;

import java.io.*;
import java.util.Scanner;

/**
 * A structure of vector used in the application
 */
public class Vector implements Serializable {
    /**
     * An array of values
     */
    public double v[];
    
    /**
     * A number of elements of the vector
     */
    public int length;
    
    /**
     * Initialization without parameters is empty
     */
    public Vector() {}
    
    /**
     * Initialization of a vector without setting any values
     * @param size Size of the vector
     */
    public Vector(int size) {
        emptyInit(size);
    }
    
    /**
     * Initialization of a vector by loading values from a file
     * @param filename Name of a file
     * @throws FileNotFoundException Thrown when file wasn't found.
     * @throws VectorLoaderException Thrown when vector can't be loaded from file.
     */
    public Vector(String filename) throws FileNotFoundException, VectorLoaderException {
        readFromFile(filename);
    }
    
    /**
     * Return a value fron the given position
     * @param pos Position in the vector
     * @return
     * @throws ParamException Thrown when position parameter is incorrect.
     */
    public double getValue(int pos) throws ParamException {
        if (pos > length || pos < 1) {
            throw new ParamException("Parametr pos jest nieprawidlowy");
        }
        return v[pos - 1];
    }
    
    /**
     * Set the given value
     * @param pos Position in the vector
     * @param value Value to set on a given position
     * @throws ParamException Thrown when position parameter is incorrect.
     */
    public void setValue(int pos, double value) throws ParamException {
        if (pos > length || pos < 1) {
            throw new ParamException("Parametr pos jest nieprawidlowy");
        }
        v[pos - 1] = value;
    }
    
    /**
     * Display the vector on a console
     */
    public void display() {
        for(int pos = 1; pos <= length; ++pos) {
            try {
                System.out.print(getValue(pos)+"\t");
            } catch(ParamException e) {}
        }
        System.out.println();
    }
    
    /**
     * Initialize a vector without setting any values
     * @param size Size of the vector
     */
    public final void emptyInit(int size) {
        v = new double[size];
        length = size;
    }
    
    /**
     * Read a vector fron a file with the given filename
     * @param filename
     * @throws FileNotFoundException Thrown when file wasn't found.
     * @throws VectorLoaderException Thrown when vector can't be loaded from file.
     */
    public final void readFromFile(String filename) throws FileNotFoundException, VectorLoaderException {
        Scanner sc = new Scanner(new File(filename));
        length = sc.nextInt();
        v = new double[length];
        int pointer = -1;
        while (sc.hasNextDouble()) {
            v[++pointer] = sc.nextDouble();
        }
        if(pointer+1 != length) {
            throw new VectorLoaderException();
        }
        sc.close();
    }
    
    /**
     * Save the vector to a file with the given filename
     * @param filename Name of a file
     * @throws UnsupportedEncodingException Thrown when unsupported encoding is used in a file.
     * @throws IOException Thrown when an error with IO operations occured.
     */
    public void saveToFile(String filename) throws UnsupportedEncodingException, IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(filename), "utf-8")))
        {
            writer.write(length+"\n");
            for(int r = 1; r <= length; ++r) {
                try {
                    writer.write(getValue(r)+"\n");
                } catch(ParamException e) {}
            }
        }
    }
}
