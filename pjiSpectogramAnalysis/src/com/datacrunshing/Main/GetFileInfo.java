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
    private int indexFirstTopElipse;
    private int indexLastTopElipse;
    private int bestFit;
    private String input = null;
    /**
     * Initialise les paramètres puis renseigne les variables.
     * Vérifie que la taille des samples est traitable.
     * Copie les données du sample dans un tableau
     * Recupere la position de la premiere elipse.
     * Recupere la position de la derniere elipse.
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
        this.indexFirstTopElipse = findFirstTopElipse();
        this.indexLastTopElipse = findLastTopElipse();
        this.bestFit = this.indexLastTopElipse - this.indexFirstTopElipse;
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
        System.out.println("En decoupant à partir de la premiere elipse, il resterait " + (this.sampleData.length - this.indexFirstTopElipse)  + " samples.");
        System.out.println("La taille optimal du fichier (best fit) allant de la premiere elipse a la derniere serait de " + this.bestFit  + " samples.");
        
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
    
    /**
     * Trouve la valeure correspondat au debut de la premiere elipse 
     * @return L'index de la valeur
     */
    public int findFirstTopElipse() {
        int indexMaxValue = 0;
        // we are going to go through all the samples up to the point where we have found a value that has n consecutive lower values.
        // This value is the top of the first elipse
        for(int i = indexMaxValue + 1; i < this.nbrMeasuresInFile; i++) {
            if(this.sampleData[i] < this.sampleData[indexMaxValue])
                this.counter++;
            else {
                indexMaxValue = i;
                this.counter = 0;
            }
            if(this.counter == Tools.sampleToParseToGetHighElipse)
               return indexMaxValue; 
        }
        
        // si on a pas trouvé de max, c'est que quelque chose de très très problématique est arrivé dans le programe.
        Tools.displayErrorAndExit("[findStartFirstElipse] Nous aurions du trouver une valeure max.");
        return -1;
    }

    /**
     * Trouve la valeure correspondat à la derniere elipse 
     * @return L'index de la valeur
     */
    private int findLastTopElipse() {
        int indexMaxValue = this.nbrMeasuresInFile - 1;
        // we are going to go through all the samples from back to front up to the point where we have found a value that has n consecutive lower values.
        // This value is the top of the last elipse
        for(int i = indexMaxValue - 1; i >= 0; i--) {
            if(this.sampleData[i] < this.sampleData[indexMaxValue])
                this.counter++;
            else {
                indexMaxValue = i;
                this.counter = 0;
            }
            if(this.counter == Tools.sampleToParseToGetHighElipse)
               return indexMaxValue; 
        }
        
        // si on a pas trouvé de max, c'est que quelque chose de très très problématique est arrivé dans le programe.
        Tools.displayErrorAndExit("[findStartFirstElipse] Nous aurions du trouver une valeure max.");
        return -1;
    }
    
    //=================GETTER/SETTER=================
    /**
     *  
     * @return the index of the top of the first elipse
     */
    public int getIndexFirstTopElipse() {
        return indexFirstTopElipse;
    }
    
    /**
     *  
     * @return the index of the top of the last elipse
     */
    public int getIndexLastTopElipse() {
        return indexLastTopElipse;
    }
    
    /**
     *  
     * @return the measures as an array of int
     */
    public int[] getSampleData() {
        return sampleData;
    }
}
