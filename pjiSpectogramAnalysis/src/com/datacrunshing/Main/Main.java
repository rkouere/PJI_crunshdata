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
        byte[] test = new byte[4];
        test[0] = 1;
        test[1] = 1;
        test[2] = 1;
        test[3] = (byte)255;
        System.out.println(byteArrayToHex(test));
        
        
        ByteBuffer bb = ByteBuffer.wrap(test);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println(bb.getInt() & 0xEFFFFFFF);

        //new AverageSamples(args).exportFile();
    }
}
