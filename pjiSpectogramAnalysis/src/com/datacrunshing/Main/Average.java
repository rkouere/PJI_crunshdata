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

/**
 *
 * @author rkouere
 */
public class Average  {
    //private String[] samples = null;
    public int numberOfSamples = 0;
    public InputStream[] fileInputStream = null;
    public File[] samples = null;
    /**
     * Initialise le tableau de files + cree les fichiers..
     * 
     */
    public File[] openFiles(String[] args, int numberOfSamples) {
        File[] tmp = new File[numberOfSamples];
        for(int i = 0; i < numberOfSamples; i++) {
            tmp[i] = new File(args[i]);
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
    public long byteToLong(byte[] bytes) {
        long result = 0;
        if(bytes.length != Tools.dataSize) {
            Tools.displayErrorAndExit("La taille du tableau envoyé n'est pas bonne");
        }
            
        return (bytes[0] & 0xFF) | (((bytes[1]) & 0xFF) << 4) | (((bytes[2]) & 0xFF) << 16) |(((bytes[3]) & 0xFF) << 24);   
    }
    
    public byte[] longToByte(long num) {
        byte[] result = new byte[Tools.dataSize];
        

            
        return result;   
    }

    
}
