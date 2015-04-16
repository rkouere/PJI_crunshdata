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
import java.util.List;

/** 
 * Permets de prendre n fichiers binaire en entrée et de ressortir un fichier avec les valeures moyenne en sortie.
 * merci http://www.devmanuals.com/tutorials/java/corejava/files/java-read-large-file-efficiently.html
 *
 * @author rkouere
 */
public class AveragingSamples extends Average {
    private FileOutputStream fileOutputStream = null;
    private String[] files = null;
    /** Initialise les variables.
     * Vérifie que l'on a au moins un fichier a traiter.
     * Ouvre les fichiers et verifie que ces fichiers ont la même taille.
     * Ouvre les Stream pour commencer les traitements.
     * 
     * @param args
     * @throws FileNotFoundException 
     */
    public AveragingSamples(List<String> args) throws FileNotFoundException {
        super(args);
        
        this.fileOutputStream = Tools.openOutputStream(this.samples[0], this.output);
    }
    
    /**
     * Will make an average of each samples and export it in a new file.
     * If the files are not the same size, we stop where the first file has not got any date left
     * @throws IOException 
     */
    public void exportFile() throws IOException {
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
            // on ajoute la valeur du premier fichier dans les resultat
            result = byteToLong(buffer);
            // on fait de même pour tous les autres fichiers à traiter
            for(int i = 1; i < this.samples.length; i++) {
                bytesRead = fileInputStream[i].read(buffer);
                
                // si un des fichiers n'a plus de donnees on arrete le traitement
                if(bytesRead == -1) {
                    break;
                }
                else
                    result += byteToLong(buffer);
            }
            
            // si l'un des fichiers est fini, on arrete le program.
            if(bytesRead == -1) {
                Tools.displayInfo("The files were not the same size");
                break;
            }
            // on fait la moyenne
            result = result/this.samples.length;
            int tmpInt = safeLongToInt(result);
            fileOutputStream.write(intToByte(tmpInt), 0, bytesRead);         
        }
        this.fileOutputStream.close();
    }
    

    /**
     * Checks that all the file sizes are the same.
     * Deprecated
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

}
