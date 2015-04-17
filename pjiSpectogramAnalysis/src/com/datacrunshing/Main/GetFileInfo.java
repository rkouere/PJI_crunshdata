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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author rkouere
 */
public class GetFileInfo extends Average {
    // the number of measures present in the file
    private int nbrMeasuresInFile = 0;
    private long maxValue;
    private long minValue;

    private int[] sampleData;
    private long nbrBytesInSample;
    private int counter = 0;
    private int indexFirstTopSinusoidal;
    private int indexLastTopSinusoidal;
    private int bestFit;
    private String input = null;
    /**
     * Initialise les paramètres puis renseigne les variables.
     * Vérifie que la taille des samples est traitable.
     * Copie les données du sample dans un tableau
     * Recupere la position de la premiere sinusoidal.
     * Recupere la position de la derniere sinusoidal.
     * 
     * @param args les arguments du programe
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public GetFileInfo(List<String> args) throws FileNotFoundException, IOException {
        super(args);

        this.nbrBytesInSample = this.samples[0].length();
        
        // on verifie que le nombre de données présente dans le fichier ne va pas faire exploser notre ram
        if ((this.nbrBytesInSample/Tools.dataSize) > Integer.MAX_VALUE)
            Tools.displayErrorAndExit("Le fichier a traiter a plus de 2^32 byte. Cela ne va pas le faire");
        
        
        // on calcul le nombre de mesures present dans le fichier
        this.nbrMeasuresInFile = safeLongToInt(this.nbrBytesInSample)/Tools.dataSize;
        this.sampleData = new int[this.nbrMeasuresInFile];
        
        //on copie les données dans un tableau
        parseData();
        // on recupere les index du debut et de la fin du future fichiers
        this.indexFirstTopSinusoidal = findFirstTopSinusoidal(0);
        this.indexLastTopSinusoidal = findLastTopSinusoidal();
        this.bestFit = this.indexLastTopSinusoidal - this.indexFirstTopSinusoidal;
        if(this.arguments.contains("-nbrSinusoide"))
            getNbrSinusoides();

    }
    

    private void getNbrSinusoides() {
        System.out.println("OUICH !");
    }
 
    /**
     * Imprime la valeur moyenne, minimum et maximum d'un fichier bin 
     * @throws IOException 
     */
    public void printFileInfo() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        long result = 0;
        long tmp;

        this.maxValue = Long.MIN_VALUE;
        this.minValue = Long.MAX_VALUE;
        /* Pour chaque 32 bits, on va lire le premier fichier et ajouter sa valeur dans result.
            On va ensuite faire de même pour les autres et calculer sa moyenne.
        */
        
        for(int i = 0; i < this.nbrMeasuresInFile; i++) {
            tmp = this.sampleData[i];
            // on met à jours les valeurs max et min, si besoin
            if(tmp > this.maxValue) {
                //System.out.println(tmp);
                this.maxValue = tmp;
            }
            if(tmp < this.minValue) {
                this.minValue = tmp;
            }

            // on ajoute la valeur à notre resultat final
            result += tmp;
            
        }
        System.out.println("La mesure moyenne est de : " + result/this.nbrMeasuresInFile);
        System.out.println("La mesure maximum est de : " + this.maxValue);
        System.out.println("La mesure minimum est de : " + this.minValue);
        System.out.println("En decoupant à partir de la premiere sinusoidal, il resterait " + (this.sampleData.length - this.indexFirstTopSinusoidal)  + " samples.");
        System.out.println("La taille optimal du fichier (best fit) allant de la premiere sinusoidal a la derniere serait de " + this.bestFit  + " samples.");
        
    }
    
    /**
     * Copie les données présente dans le sample dans un tableau de int
     * @throws IOException 
     */
    private void parseData() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        long result = 0;
        int counter = 0;
        long tmp;

        while (fileInputStream[0].read(buffer) != -1)     
        {
            this.sampleData[counter++] = safeLongToInt(byteToLong(buffer));
        }        
    }
    
    private int tmpFunction(int[] samples, int index) {
        return samples[index];
    }
    /**
     * Trouve la valeure correspondat au debut de la premiere sinusoidal 
     * @return L'index de la valeur
     */
    public int findFirstTopSinusoidal(int start) {
        int j =  start;
        // we are going to go through all the samples up to the point where we have found a value that has n consecutive lower values.
        // This value is the top of the first sinusoidal
        for(int i = start; i < (this.nbrMeasuresInFile - Tools.sampleToParseToGetHighSinusoid); i++) {
            int currentMax = this.sampleData[i];
            // we go through all the samples. If we find a value higher that the current max, it becomes our new gighest value
            for(j = 0; j < Tools.sampleToParseToGetHighSinusoid; j++){
                if(currentMax < tmpFunction(sampleData, i+j)) {
                    currentMax = this.sampleData[i+j];
                    i = i + j;
                    break;
                }
            }
            if(j == Tools.sampleToParseToGetHighSinusoid)
                return i;
        }
        
        // si on a pas trouvé de max, c'est que quelque chose de très très problématique est arrivé dans le programe.
        Tools.displayErrorAndExit("[findStartFirstSinusoidal] Nous aurions du trouver une valeure max.");
        return -1;
    }

    /**
     * Trouve la valeure correspondat à la derniere sinusoidal 
     * @return L'index de la valeur
     */
    private int findLastTopSinusoidal() {
        int j = 0;
        // we are going to go through all the samples from back to front up to the point where we have found a value that has n consecutive lower values.
        // This value is the top of the last sinusoidal
        for(int i = this.nbrMeasuresInFile - 1; i >= (0+Tools.sampleToParseToGetHighSinusoid); i--) {
            int currentMax = this.sampleData[i];
            // we go through all the samples. If we find a value higher that the current max, it becomes our new gighest value
            for(j = 0; j < Tools.sampleToParseToGetHighSinusoid; j++){
                if(currentMax < tmpFunction(sampleData, i-j)) {
                    currentMax = this.sampleData[i-j];
                    i = i - j;
                    break;
                }
            }
            if(j == Tools.sampleToParseToGetHighSinusoid)
                return i;
        }
        
        
        // si on a pas trouvé de max, c'est que quelque chose de très très problématique est arrivé dans le programe.
        Tools.displayErrorAndExit("[findStartFirstSinusoidal] Nous aurions du trouver une valeure max.");
        return -1;
    }
    
    //=================GETTER/SETTER=================
    /**
     *  
     * @return the index of the top of the first sinusoidal
     */
    public int getIndexFirstTopSinusoidal() {
        return indexFirstTopSinusoidal;
    }
    
    /**
     *  
     * @return the index of the top of the last sinusoidal
     */
    public int getIndexLastTopSinusoidal() {
        return indexLastTopSinusoidal;
    }
    
    /**
     *  
     * @return the measures as an array of int
     */
    public int[] getSampleData() {
        return sampleData;
    }
}
