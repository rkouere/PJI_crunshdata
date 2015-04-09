/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.datacrunshing.tools.Tools;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/** 
 * Permets de prendre n fichiers binaire en entrée et de ressortir un fichier avec les valeures moyenne en sortie.
 * merci http://www.devmanuals.com/tutorials/java/corejava/files/java-read-large-file-efficiently.html
 *
 * @author rkouere
 */
public class AverageMultipleSamples extends Average {
 

    /** Initialise les variables.
     * Vérifie que l'on a au moins un fichier a traiter.
     * Ouvre les fichiers et verifie que ces fichiers ont la même taille.
     * Ouvre les Stream pour commencer les traitements.
     * 
     * @param args
     * @throws FileNotFoundException 
     */
    public AverageMultipleSamples(String[] args) throws FileNotFoundException {
        this.numberOfSamples = args.length;
        System.out.println("Processing " + this.numberOfSamples + " samples");
        checkArgs(this.numberOfSamples);
        this.samples = openFiles(args, this.numberOfSamples);
        checkFileSize();
        openInputStreams(); 
    }
    
    
    public boolean exportFile() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        long result = 0;
        long tmp = 0;
        int bytesRead;
        ByteBuffer bb = null;
        
        /* Pour chaque 32 bits, on va lire le premier fichier et ajouter sa valeur dans result.
            On va ensuite faire de même pour les autres et calculer sa moyenne.
        */
        while ((bytesRead = fileInputStream[0].read(buffer)) != -1)     
        {
            result = 0;
            //  create a byte buffer and wrap the array

            result += byteToLong(buffer);
            System.out.println(new Tools().byteArrayToHex(buffer));

            System.out.println("[0] " + result);
            
            for(int i = 1; i < this.samples.length; i++) {
                bytesRead = fileInputStream[i].read(buffer);
                result += byteToLong(buffer);
                System.out.println("[" + i +"] " + result);
            }

            result = result / this.numberOfSamples;
            
            //fileOutputStream.write(buffer, 0, bytesRead);         
        }   
        return true;
    }
    

    /**
     * Checks that all the file sizes are the same.
     * If not, we stop the program.
     * 
     */
    private void checkFileSize() {
        long length = this.samples[0].length();
        boolean lengthOk = true;
        System.out.println("File size = " + length);
        for(int i = 1; i < this.samples.length; i++) {
            if(this.samples[i].length() != length) {
                System.out.println("File " + this.samples[i].getName() + " has not the same file size.");
                lengthOk = false;
            }
        }
        if(!lengthOk) {
            System.out.println("Ce programme ne peut traiter que des fichiers ayant la même taille. Exiting ");
            System.exit(-1);
        }
    }

    

    

    
    /**
     * Verifie que l'on traite au monins deux samples.
     * @param numberOfSamples 
     */
    private void checkArgs(int numberOfSamples) {
        if(numberOfSamples <=1) {
            System.out.println("Pour faire une moyenne, le programme doit traiter au moins deux fichiers");
            System.exit(-1);
        }
    }
    
    
//    public static void main(String[] args) throws IOException {
// 
//        
//
//        try {
//
//            //find the file size
//            File fileHandle = new File("test.jpg");     
//            long length = fileHandle.length();
//
//                //Open the input and out files for the streams
//                fileInputStream = new BufferedInputStream(new FileInputStream(args[0]));
//                
//                //Read data into buffer and then write to the output file
//                // We import 4 octets
//                byte[] buffer = new byte[Tools.dataSize];                
//                int bytesRead;                                    
//                while ((bytesRead = fileInputStream.read(buffer)) != -1)     
//                {     
//                    
//                    System.out.println(new Tools().byteArrayToHex(buffer));
//                    //fileOutputStream.write(buffer, 0, bytesRead);           
//                }   
//
//        }
//        catch (IOException e)
//        {
//            //Display or throw the error
//            System.out.println("Eorr while execting the program: " + e.getMessage());
//        }    
//         finally 
//        {
//            //Close the resources correctly
//            if (fileInputStream != null)
//            {
//                fileInputStream.close();
//            }
//            if (fileInputStream != null)
//            {
//                fileOutputStream.close();
//            }
//        }
//
//
//        
//    }
}