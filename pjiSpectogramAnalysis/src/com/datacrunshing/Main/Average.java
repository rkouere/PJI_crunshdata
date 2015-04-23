/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import com.datacrunshing.tools.Tools;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author rkouere
 */
public class Average  {
    //private String[] samples = null;
    public int numberOfSamples = 0;
    public InputStream[] fileInputStream = null;
    public File[] samples = null;
    public List<String> arguments = new ArrayList<String>();
    public List<String> input = new ArrayList<String>();
    public String output = "newSample.bin";

    
    public Average(List<String> args)  throws FileNotFoundException {
        // on fait une copie des arguments
        this.arguments = args;
        getInputs();
        
        // if we specifie a file for output, let's define it now
        if((this.arguments.size() > 0) && this.arguments.get(0).equals("-o")) {
            setOutput();
        }
        
        this.samples = openFiles(this.input, this.numberOfSamples);
        openInputStreams();

    }
    
    public void setOutput() {
        this.arguments.remove(0);
        this.output = this.arguments.get(0);
        this.arguments.remove(0);
    }
    

    
    /**
     * Will get the input files.
     * Stops when either another command is in the arguments or when there are no more samples to deal with
     * @param args
     * @return the number of arguments left or -1 if there are none left
     */
    public int getInputs() {
        // permet de stoquer la nouvelle 
        
        if(!this.arguments.get(0).equals("-i"))
            Tools.displayErrorAndExit(Tools.help);
        
        this.arguments.remove(0);
        
        List<String> tmp = new ArrayList<>(this.arguments);
        for(int i = 0; i < this.arguments.size(); i++) {
            // if we have another argument
            if(this.arguments.get(i).startsWith("-"))  {
                this.arguments = tmp;
                return this.arguments.size() - this.numberOfSamples;
            }
            else {
                this.numberOfSamples++;
                this.input.add(this.arguments.get(i));
                tmp.remove(0);
            }
        }
        this.arguments = tmp;

        return -1;
    }
    

    /**
     * Initialise le tableau de files + cree les fichiers..
     * 
     */
    public File[] openFiles(List<String> args, int numberOfSamples) {
        File[] tmp = new File[numberOfSamples];
        for(int i = 0; i < numberOfSamples; i++) {
            tmp[i] = new File(args.get(i));
        }
        return tmp;
    }
    
    /** Initialise le tableau d'InputStream.
     * Ouvre les stream pour chaque fichiers
     * @throws FileNotFoundException 
     */
    public void openInputStreams()  throws FileNotFoundException {
        this.fileInputStream = new InputStream[this.numberOfSamples];
        
        for(int i = 0; i < this.numberOfSamples; i++) {
            
            this.fileInputStream[i] = new BufferedInputStream(new FileInputStream(this.samples[i]));
        }
    }
    
    /**
     * Permet de tranformer un long en int tout en verifiant que l'on ne perdra pas d'information en route.
     * @param l Le lon a tranformer
     * @return un int
     */
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    
    /**
     * Tranforms a table of 4 little endian bytes to a long
     * @param bytes a table of 4 bytes
     * @return signed long
     */
    public int byteToLong(byte[] bytes) {
        long result = 0;
        if(bytes.length != Tools.dataSize) {
            Tools.displayErrorAndExit("La taille du tableau envoyé n'est pas bonne");
        }
            
        return (bytes[0] & 0xFF) | (((bytes[1]) & 0xFF) << 8) | (((bytes[2]) & 0xFF) << 16) |(((bytes[3]) & 0xFF) << 24);   
    }
    
    public byte[] intToByte(int num) {
        byte[] result = new byte[Tools.dataSize];
        result[3] = (byte)((num >> 24) & 0xFF);
        result[2] = (byte)((num >> 16) & 0xFF);
        result[1] = (byte)((num >> 8) & 0xFF);
        result[0] = (byte)((num) & 0xFF);

        return result;   
    }


    
}
