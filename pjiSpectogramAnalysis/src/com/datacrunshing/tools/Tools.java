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
}
