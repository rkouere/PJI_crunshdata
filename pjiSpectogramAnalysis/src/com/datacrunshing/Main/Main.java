/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import com.datacrunshing.tools.Tools;
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

        if(args.length == 0) {
            Tools.displayErrorAndExit("Le programe prend au moins un argument. Tappez -h pour afficher l'aide.");
        }
        else {
            switch(args[0]) {
                case "-info":
                    new AverageSingleSample(args).printFileInfo();
                    break;
                case "-combineAvg":
                    new AverageMultipleSamples(args).exportFile();
                    break;
                case "-resample":
                    //System.out.println(new Resample(args).findFirstTopElipse());
                    break;
                default:
                    Tools.displayErrorAndExit("Le programe prend au moins un argument. Tappez -h pour afficher l'aide.");
                    break;
            }
        }
//        
        //
    }
}
