package common;

import java.io.*;
import java.util.Scanner;

public class Matrix implements Serializable {
    public double a[];
    public int rows;
    public int cols;
    
    public Matrix() {}
    
    public Matrix(String filename) throws FileNotFoundException, MatrixLoaderException {
        readFromFile(filename);
    }
    
    public double getValue(int row, int col) throws ParamException {
        if (row > rows || row < 1) {
            throw new ParamException("Parametr row jest nieprawidlowy");
        } else if(col > cols || col < 1) {
            throw new ParamException("Parametr col jest nieprawidlowy");
        }
        return a[(row-1)*cols + col - 1];
    }
    
    public void setValue(int row, int col, double value) throws ParamException {
        if (row > rows || row < 1) {
            throw new ParamException("Parametr row jest nieprawidlowy");
        } else if(col > cols || col < 1) {
            throw new ParamException("Parametr col jest nieprawidlowy");
        }
        a[(row-1)*cols + col - 1] = value;
    }
    
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
    
    public void saveToFile(String filename) throws UnsupportedEncodingException, FileNotFoundException, IOException {
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
