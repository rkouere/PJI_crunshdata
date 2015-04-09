/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import static com.datacrunshing.tools.Tools.byteArrayToHex;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author rkouere
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {


        //new AverageMultipleSamples(args).exportFile();
         new AverageSingleSample(args).exportFile();
    }
}
