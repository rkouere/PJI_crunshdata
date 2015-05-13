/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import com.datacrunshing.tools.Tools;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rkouere
 */
public class GetFileInfo extends Average {
    /**
     * the number of measures present in the file
     */
    private int nbrMeasuresInFile = 0;
    /**
     * The maximum value present in the sample
     */
    private long maxValue;
    /**
     * The minimum value present in the sample
     */
    private long minValue;
    /**
     * An array representing the samples
     */
    private int[] data;
    /**
     * The indexes of each top in the file
     */
    private List<Integer> position_index;
    /**
     * The indexes of each top in the file
     */
    private List<Integer> interval_index;
    /**
     * The index of the first top of the sinusoidal
     * 
    */
    private int firstTop;

    /**
     * The index of the last top of the sinusoidal
     * 
     */
    private int lastTop;
    /**
     * The size of the sample in bites
     */
    private long nbrBytesInSample;
    
    
    
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
        this.interval_index = new ArrayList<>();
        this.position_index = new ArrayList<>();
        this.nbrBytesInSample = this.samples[0].length();
        // on verifie que le nombre de données présente dans le fichier ne va pas faire exploser notre ram
        if ((this.nbrBytesInSample/Tools.dataSize) > Integer.MAX_VALUE)
            Tools.displayErrorAndExit("Le fichier a traiter a plus de 2^32 byte. Cela ne va pas le faire");
        
        
        // on calcul le nombre de mesures present dans le fichier
        this.nbrMeasuresInFile = safeLongToInt(this.nbrBytesInSample)/Tools.dataSize;
        
        this.data = new int[this.nbrMeasuresInFile];
        
        //on copie les données dans un tableau
        copyData();
        // we get the index of each top of each sinusoidal
        getNbrSinusoidals();
        // we set the data of the first and of the last sinusoidal
        setFirstLastTop();
        setPositionIndex(this.interval_index);
        if(args.contains("-sinGaps")) {
            printGapBetweenSinusoidals(this.position_index);
        }

        
    }
    /**
     * Gives the interval between the sinusoidals
     * @param interval_index 
     */
    private void setPositionIndex(List<Integer> interval_index) {
        for(int i = 1; i < interval_index.size(); i++) {
            this.position_index.add(interval_index.get(i) - interval_index.get(i - 1));
        }
    }
    
    /**
     *  Prints the intervals between each sinusoidals
     * @param position_index 
     */
    private void printGapBetweenSinusoidals(List<Integer> position_index) {
        for(Integer i : this.position_index)
            System.out.println(i);      
    }
    /**
     * This is just a simple function to set the variables relating to the 
     * position of the first and last top of the sinusoidal
     * 
     */
    private void setFirstLastTop() {
        setFirstTop(this.interval_index.get(0));
        setLastTop(this.interval_index.get(this.interval_index.size() - 1));       
    }

    /**
     * Will go through all the samples to find the number of sinusoidals present in the file.
     * 
     * @return the index of the last Sinusoidal
     */
    private boolean getNbrSinusoidals() {
        int i = 0;
        
        for(i = Tools.averagePrecision; i < this.nbrMeasuresInFile; i++) {
            i = findTopSinusoidal(i);
            if(i == -1)
                return true;
            i = findBottomSinusoidal(i);
            if(i == -1)
                return true;
            this.interval_index.add(i);
        }
       return false;
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
            tmp = this.data[i];
            // on met à jours les valeurs max et min, si besoin
            if(tmp > this.maxValue) {
                //System.out.println(tmp);
                this.maxValue = tmp;
            }
            if(tmp < this.minValue) {
                this.minValue = tmp;
            }
//            System.out.println(this.data[i]);
            // on ajoute la valeur à notre resultat final
            result += tmp;
            
        }
        System.out.println("L'index du premier top de la sinusoid est : " + this.firstTop);
        System.out.println("L'index du dernier top de la sinusoid est : " + this.lastTop);
        System.out.println("Le nombre de sinusoidals present in the file is : " + this.interval_index.size());

        System.out.println("La mesure moyenne est de : " + result/this.nbrMeasuresInFile);
        System.out.println("La mesure maximum est de : " + this.maxValue);
        System.out.println("La mesure minimum est de : " + this.minValue);
        System.out.println("En decoupant à partir de la premiere sinusoidal, il resterait " + (this.data.length - this.interval_index.get(0))  + " samples.");
        System.out.println("La taille optimal du fichier (best fit) allant de la premiere sinusoidal a la derniere serait de " +  (this.lastTop - this.firstTop)  + " samples.");
        
    }
    
    /**
     * Copie les données présente dans le sample dans un tableau de int
     * @throws IOException 
     */
    private void copyData() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        long result = 0;
        long tmp;
        int counter = 0;

        while (fileInputStream[0].read(buffer) != -1)     
        {
            this.data[counter++] = safeLongToInt(byteToLong(buffer));
        }        
    }
    
    /**
     * 
     */
    private int getAverageMeasure(int[] samples, int index, int precision) {
        long average = 0;
        // we want to use the samples on the left of the measure and on the right of the measure
        int i;
        
        // we never start at 0
        if(index < precision) {
            return -1;
        }

        for(i = -precision/2; i < precision/2; i++) {
            average += samples[i + index];            
        }             
 
        return safeLongToInt(average/precision);
    }


    /**
     * Finds the lowest point from the index.
     * 
     * @param index The starting point of the test
     * @return The index of the lowest point
     */
    private int findBottomSinusoidal(int start) {
        int tmp = 0;
        int j;

        for(int i = start; i < (this.nbrMeasuresInFile - (Tools.averagePrecision + Tools.sampleToParseAfter)); i++) {
            int currentMax = getAverageMeasure(data, i, Tools.averagePrecision);
            // we go through all the samples. If we find a value higher that the current max, 
            // it becomes our new gighest value
            for(j = 1; j < Tools.sampleToParseAfter; j++){
                tmp = getAverageMeasure(data, i + j, Tools.averagePrecision);
                // if the current max if lower that the average datas, the average datas are becoming the new max
 
                if(currentMax > tmp) {
                    currentMax = tmp;
                    i = i + j;
                    break;
                }
            }
            if(j == Tools.sampleToParseAfter) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Trouve la valeure correspondat au debut de la premiere sinusoidal 
     * @return L'index de la valeur
     */
    public int findTopSinusoidal(int start) {
        int tmp = 0;
        int j;
        // we are going to go through all the samples up to the point where we 
        // have found a value that has n consecutive lower values.
        // This value is the top of the first sinusoidal
        // We stop before the end minux the number of samples we use to find the first top. 
        // Tools.sampleToParseAfter + Tools.averagePrecision : we do 
        // it because we start parsing the files at Tools.averagePrecision

        for(int i = start; i < (this.nbrMeasuresInFile - (Tools.averagePrecision + Tools.sampleToParseAfter)); i++) {
            int currentMax = getAverageMeasure(data, i, Tools.averagePrecision);
            // we go through all the samples. If we find a value higher that the current max, 
            // it becomes our new gighest value
            for(j = 1; j < Tools.sampleToParseAfter; j++){
                tmp = getAverageMeasure(data, i + j, Tools.averagePrecision);
                // if the current max if lower that the average datas, the average datas are becoming the new max
 
                if(currentMax < tmp) {
                    currentMax = tmp;
                    i = i + j;
                    break;
                }
            }
            if(j == Tools.sampleToParseAfter) {
                return i;
            }
        }
        
        // si on a pas trouvé de max, c'est que quelque chose de très très problématique est arrivé dans le programe.
        return -1;
    }


    
    //=================GETTER/SETTER=================

 
    public void setFirstTop(int firstTop) {
        this.firstTop = firstTop;
    }

    public void setLastTop(int lastTop) {
        this.lastTop = lastTop;
    }

    public int getFirstTop() {
        return firstTop;
    }

    public int getLastTop() {
        return lastTop;
    }
 
    
    /**
     *  
     * @return the measures as an array of int
     */
    public int[] getData() {
        return this.data;
    }
}
