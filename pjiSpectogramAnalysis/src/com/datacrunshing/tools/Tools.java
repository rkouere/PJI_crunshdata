/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rkouere
 */
public class Tools {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    /**
     * Display the help message
     */
    public static final String help =   
                "This program can take the following arguments : \n" + 
                ANSI_BLUE + "-info : displays the min, max and average measures, the size of the file if we cut it from the first top elipse and the size of the file if we cut from the top of the first elipse to the top of the last elipse.\n" + ANSI_RESET +
                "\t-i filename : the name of the file to deal with\n" + 
                "========\n" + 
                " -combineAvg : takes n parameters and create a file containing an average of each measure\n" + 
                "-resample : ";
    /**
     * Le nombre de données que l'on lit après avoir trouvé une valeure.
     */
    public static final int sampleToParseToGetHighElipse = 1000;
    /**
     * Taille des données (en octets). D'après nos calculs, les données sont codés sur 32 bits, littleendien.
     */
    public static final int dataSize = 4;
    /**
     * Takes a byte array and print a hexString.
     * @param a byte array
     * @return 
     */
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
           sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
    
       /**
     * Display a message in red and exits
     * @param msg 
     */
    public static void displayInfo(String msg) {
        System.out.println(ANSI_GREEN + msg  + ANSI_RESET);
    }
    
    /**
     * Display a message in red and stops the program
     * @param msg 
     */
    public static void displayErrorAndExit(String msg) {
        System.out.println(ANSI_RED + msg  + ANSI_BLACK);
        System.exit(-1);
    }
    
    /**
     * Ouvre un stream
     * @param sample
     * @param fileName
     * @return
     * @throws FileNotFoundException 
     */
    public static FileOutputStream openOutputStream(File sample, String fileName) throws FileNotFoundException{
        String[] tmp = sample.getAbsolutePath().split("\\/");
        String path = new String();
        for(int i = 0; i < tmp.length - 1; i++)
            path += tmp[i] + "/";
        return new FileOutputStream(path + fileName);
    }
    
    public static List<String> arrayStringToList(String[] string) {
        List<String> tmp = new ArrayList<>();
        
        for(String str:string) {
            tmp.add(str);
        }
        return tmp;
    }

}
