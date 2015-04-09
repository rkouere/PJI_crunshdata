/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import com.datacrunshing.tools.Tools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author rkouere
 */
public class AverageSingleSample extends Average {
    // the number of measures present in the file
    private int numberOfOctet = 0;

    public AverageSingleSample(String[] args) throws FileNotFoundException {
        if(args.length != 1)
            Tools.displayErrorAndExit("Le programe ne prend qu'un argument : le fichier a traiter");
        this.numberOfSamples = args.length;
        this.samples = openFiles(args, this.numberOfSamples);
        openInputStreams(); 
    }
    
    public void exportFile() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        long result = 0;
        int bytesRead;
        
        /* Pour chaque 32 bits, on va lire le premier fichier et ajouter sa valeur dans result.
            On va ensuite faire de même pour les autres et calculer sa moyenne.
        */
        while ((bytesRead = fileInputStream[0].read(buffer)) != -1)     
        {

            result += byteToLong(buffer);

            this.numberOfOctet ++;
        }
        
        System.out.println("La mesure  moyenne est de : " + result/this.numberOfOctet);
    }
    
}
