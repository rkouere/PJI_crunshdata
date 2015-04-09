/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.tools;

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
     * Taille des données (en octets). D'après nos calculs, les données sont codés sur 32 bits, littleendien.
     */
    public static final int dataSize = 4;
    /**
     * Takes a byte array and print a hexString.
     * @param a byte aqrray
     * @return 
     */
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
           sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
    
    public static void displayErrorAndExit(String msg) {
        System.out.println(ANSI_RED + msg  + ANSI_BLACK);
        System.exit(-1);
    }
}
