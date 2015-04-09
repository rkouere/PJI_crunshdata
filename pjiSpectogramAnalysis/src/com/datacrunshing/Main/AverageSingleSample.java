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
    private long maxValue;
    private long minValue;
    private boolean started = false;
    
    public AverageSingleSample(String[] args) throws FileNotFoundException {
        if(args.length != 1)
            Tools.displayErrorAndExit("Le programe ne prend qu'un argument : le fichier a traiter");
        this.numberOfSamples = args.length;
        this.samples = openFiles(args, this.numberOfSamples);
        openInputStreams(); 
    }
    
    /**
     * Imprime la valeur moyenne, minimum et maximum d'un fichier bin 
     * @throws IOException 
     */
    public void printFileInfo() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        long result = 0;
        long tmp;
        /* Pour chaque 32 bits, on va lire le premier fichier et ajouter sa valeur dans result.
            On va ensuite faire de même pour les autres et calculer sa moyenne.
        */
        while (fileInputStream[0].read(buffer) != -1)     
        {
            tmp = byteToLong(buffer);
            // si c'est notre premiere passe, on initialise les valeur max et min (sinon, elle seront initialisé à 0 par défault et les résultat que nous aurons ne seront pas formcément les bon)
            if(started == false) {
                this.maxValue = this.minValue = tmp;
                started = true;
            }
            // on met à jours les valeurs max et min, si besoin
            else {
                if(tmp > this.maxValue)
                    this.maxValue = tmp;
                if(tmp < this.minValue)
                    this.minValue = tmp;
            }
            // on ajoute la valeur à notre resultat final
            result += tmp;
            this.numberOfOctet ++;
        }
        
        System.out.println("La mesure moyenne est de : " + result/this.numberOfOctet);
        System.out.println("La mesure maximum est de : " + this.maxValue);
        System.out.println("La mesure minimum est de : " + this.minValue);
        
    }
    
}
