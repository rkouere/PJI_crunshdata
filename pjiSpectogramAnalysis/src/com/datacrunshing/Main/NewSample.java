/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import com.datacrunshing.tools.Tools;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * This class will generate a new sample.
 * @author rkouere
 */
public class NewSample {
    
    private int[] new_sample;
    /**
     * The number of times each interval appears in the file
     */
    private int[] poids;
    /**
     * The index of the value of the sample most likely to show the most accurate top
     */
    private int bestPos;
    /**
     * The value of the most likely "clean" amplitude
     */
    private int amp;
    /**
     * the index from the old file that will be the start of the new file
     */
    private int newSampleStart;
    /**
     * the index from the old file that will be the end of the new file
     */
    private int newSampleEnd;
    /**
     * The intervals_size between the top of each sinudoidals
     */
    private List<Integer> intervals_size
;    /**
     * The indexes of each top in the file
     */
    private List<Integer> position_top_index;
    
    private int[] old_sample;

    
    private FileOutputStream fileOutputStream = null;
    
    public NewSample(int[] sample, List<Integer> interval_size, List<Integer> position_top_index) {
        this.old_sample = sample;
        this.intervals_size = interval_size;
        this.position_top_index = position_top_index;

        // we initialise the table we'll have to use to store the number of time a top appears in the file
        this.poids = new int[this.intervals_size.size()];
        // we find the occurence of each tops in the sample
    }
    
    public int[] generateNewSample() {
        int averge_interval = 0;
        poid(this.intervals_size, Tools.epsilon);
        // we fing the index of the measure that most likely represents the "ideal" top
        this.bestPos = Tools.maxIntArray(this.poids);
        // we fing the average amplitude
        averge_interval = getAvergeInterval(this.intervals_size);
        this.amp = avergage_amp(this.old_sample, this.position_top_index.get(this.bestPos), averge_interval, Tools.epsilon);
        // we get the indexes that will be used to create the new sample 
        // (we only want to keep the measures which are within an acceptable range, compared with the most probable top).
        getNewSampleBounds(this.old_sample, this.position_top_index.get(this.bestPos), averge_interval, Tools.epsilon);
        this.new_sample = new int[this.newSampleEnd - this.newSampleStart];

        buildSample(this.old_sample, this.position_top_index.get(this.bestPos), averge_interval, this.amp, this.newSampleEnd, this.newSampleStart, Tools.epsilon);
        return this.new_sample;
    }
    
    /**
     * Calculate the average interval within the chosen bounds
     * @return 
     */
    private int getAvergeInterval(List<Integer> intervals_size) {
        long avg = 0;
        int cpt = 0;
        for(int i = 0; i < intervals_size.size(); i++) {
            avg += intervals_size.get(i);
            cpt++;
        }
        return Tools.safeLongToInt(avg/cpt);
    }
    /**
     * This will try to remove the noise from the original sample and store it in this.new_sample.
     * @param data : the original sample
     * @param bestPos : the index of the top most likely reflecting the most accurate value for a top
     * @param interval : the interval that is the most likely the "average" interval produced by the signal generator
     * @param amplitude : the average amplitude
     */
    private void buildSample(int[] data, int bestPos, int interval, int amplitude, int newSampleEnd, int newSampleStart, double epsilon) {
        double valMax = ((double)this.old_sample[bestPos]) * epsilon;
        double cos;
        System.out.println("nest pos = " + this.old_sample[bestPos]);
        System.out.println("best top = " + bestPos);

        System.out.println("val max = " + (int)(Math.round(valMax)));
        for(int i = 0; i < this.new_sample.length; i++) {
            cos = ((double)amplitude) * (Math.cos((double)(i+newSampleStart-bestPos)*(2*Math.PI)/(double)interval));
            this.new_sample[i] = (int) (((double)this.old_sample[i+newSampleStart]) - cos);
            if(this.position_top_index.contains(i+newSampleStart)) {
                System.out.print(
                        "valeur origine = " + this.old_sample[i+newSampleStart] + 
                        "; valeur cos = " + Math.round(cos) + 
                        "; valeur result = " + this.new_sample[i]
                        );
                if(this.new_sample[i] >= Math.round(valMax))
                    System.out.print("*");
                System.out.print("\n");
            }
        }
    }    
    /**
     * Will find the bounds of the new sample.
     * This method will parse the measures taking the index of the measure representing the most "typicall" sinusoid.
     * It will will find the first measure m that is not within the range we have set with epsilon.
     * When parsing the files towards the start of the sample, m+1 will be the index of the start of the new sample.
     * When parsing towards the end, m-1 will be the index of the end of the new sample.
     * @param data
     * @param position_top : the index of the top taken as reference
     * @param interval_size : the interval associated with the top
     * @param epsilon : the percentage we want to use as tolerence to data changes
     */
    private void getNewSampleBounds(int[] data, Integer position_top, Integer interval_size, double epsilon) {
        int i = position_top;
        // the value that we use to see when to stop the calculation of the 
        double maxVal = this.old_sample[position_top] * epsilon;

        // we are going to go through all the data on the left of the index
        while(i >= 0) {
            if(data[i] < maxVal) {
                break;
            }
            this.newSampleStart = i;
            i -= interval_size;
        }
        
        // we reset the position just after the first position (we already have this value. Using it again in pointless).
        i = position_top + interval_size;
        while(i < data.length) {
            if(data[i] < maxVal) {
                break;
            }
            this.newSampleEnd = i;
            i += interval_size;            
        }
    }
    /**
     * Calculates the average amplitude if using the most likely average interval.
     * This function will add the values of each most probable top and calculate the average.
     * We noticed that some of the values we got from the parsing of the data were negative. 
     * We have therefore decided to stop using them if they are too low.
     * @param position_top : the index of the top taken as reference
     * @param interval_size : the interval associated with the top
     * @param epsilon : the percentage we want to use as tolerence to data changes
     * @return 
     */
    private int avergage_amp(int[] data, Integer position_top, Integer interval_size, double epsilon) {
        long amp = 0;
        int i = position_top;
        int cpt = 0;
        // the value that we use to see when to stop the calculation of the 
        double maxVal = this.old_sample[position_top] * epsilon;
        // we are going to go through all the data on the left of the index
        while(i >= 0) {
            if(data[i] > maxVal) {
                amp += data[i];
                cpt++;
                i -= interval_size;
            }
            // if the value is too small, we stop 
            else
                break;
        }
        
        // we reset the position just after the first position (we already have this value. Using it again in pointless).
        i = position_top + interval_size;
        while(i < data.length) {
            if(data[i] > maxVal) {
                amp += data[i];
                cpt++;
                i += interval_size;            
            }
            // if the value is too small, we stop 
            else
                break;
        }        
        System.out.println("original number of tops: " + this.position_top_index.size() + "; new_sample number of tops: " + cpt);
        amp = amp / cpt;

        return Tools.safeLongToInt(amp);
    }

    /**
     * Prints the value of each top that are at the distance interval_size of the measure at position top
     * @param position_top : the index of the top taken as reference
     * @param interval_size : the interval associated with the top
     * @param epsilon : the percentage we want to use as tolerence to data changes
     * @return 
     */
    private void print_top(int[] data, Integer position_top, Integer interval_size, double epsilon) {
        long amp = 0;
        int i = position_top;
        int cpt = 0;
        // the value that we use to see when to stop the calculation of the 
        double maxVal = this.old_sample[position_top] * epsilon;
        // we are going to go through all the data on the left of the index
        System.out.println("parcours fichier vers le debut");
        while(i >= 0) {
            System.out.println("valeur max dans fichier : " + (int)maxVal + "; valeur trouvé : " + data[i]);
            i -= interval_size;            

        }
        
        // we reset the position just after the first position (we already have this value. Using it again in pointless).
        System.out.println("==============================");
        System.out.println("parcours fichier vers la fin");
        i = position_top + interval_size;
        while(i < data.length) {
            System.out.println("valeur max dans fichier : " + (int)maxVal + "; valeur trouvé : " + data[i]);
            i += interval_size;            
        }

    }
    /**
     * Calculates the number of times the interval * epsilon appear in our sample.
     * Every times it encounters a value that fits the constraints, it increaments the poids[]
     * @param interval_index
     * @param epsilon 
     */
    private void poid(List<Integer> interval_index, double epsilon) {
        for(int i = 0; i < interval_index.size(); i++) {
            this.poids[i] = 0;
            for(int y = 0; y < interval_index.size(); y++) {
                if((interval_index.get(y) * epsilon >= interval_index.get(i) * epsilon))
                    this.poids[i] ++;
            }
        }
    }
    

    
}
