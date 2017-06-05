package common;

import java.io.*;
import java.util.Scanner;

public class Vector implements Serializable {
    public double v[];
    public int length;
    
    public Vector() {}
    
    public Vector(int size) {
        emptyInit(size);
    }
    
    public Vector(String filename) throws FileNotFoundException, VectorLoaderException {
        readFromFile(filename);
    }
    
    public double getValue(int pos) throws ParamException {
        if (pos > length || pos < 1) {
            throw new ParamException("Parametr pos jest nieprawidlowy");
        }
        return v[pos - 1];
    }
    
    public void setValue(int pos, double value) throws ParamException {
        if (pos > length || pos < 1) {
            throw new ParamException("Parametr pos jest nieprawidlowy");
        }
        v[pos - 1] = value;
    }
    
    public void display() {
        for(int pos = 1; pos <= length; ++pos) {
            try {
                System.out.print(getValue(pos)+"\t");
            } catch(ParamException e) {}
        }
        System.out.println();
    }
    
    public final void emptyInit(int size) {
        v = new double[size];
        length = size;
    }
    
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
    
    public void saveToFile(String filename) throws UnsupportedEncodingException, FileNotFoundException, IOException {
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
