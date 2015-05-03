/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import com.datacrunshing.tools.Tools;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Creates a "perfect sinusoidal".
 * 
 * @author rkouere
 */
public class CreateSinusoidal {
    private int maxValue = 2147417723;
    int sizeSinusoid = 20480070;
    private int[] data = null;
    private FileOutputStream fileOutputStream = null;
    
    
    public CreateSinusoidal(String fileName, String path, int maxValue, int sizeSinusoid) throws FileNotFoundException, IOException {
        this.maxValue = maxValue;
        this.sizeSinusoid = sizeSinusoid;
        this.fileOutputStream = new FileOutputStream(path + fileName);
        this.data = new int[sizeSinusoid];
        generateSinusoidal();
        exportSinusoid();
    }
    
    public CreateSinusoidal(String fileName, String path) throws FileNotFoundException, IOException {
        this.fileOutputStream = new FileOutputStream(path + fileName);
        this.data = new int[sizeSinusoid];
        generateSinusoidal();
        exportSinusoid();
    }
    
    public void generateSinusoidal() {
        boolean goingUp = true;
        int cmpt = 0;
        for(int i = 0; i < sizeSinusoid; i++) {
            if(goingUp) {
                cmpt += 50000;
                data[i] = cmpt;
            }
            else {
                cmpt -= 50000;
                data[i] = cmpt;
                
            }
            
            if(cmpt == this.maxValue)
                goingUp = false;

            else if(cmpt == -this.maxValue)
                goingUp = true;
        }
    }
    
    private void exportSinusoid() throws IOException {
        byte[] buffer = new byte[Tools.dataSize];
        Average avg = new Average();
        for(int i = 0; i < sizeSinusoid; i++) {
            fileOutputStream.write(avg.intToByte(this.data[i]), 0, Tools.dataSize);
        }
        this.fileOutputStream.close();
    }
    
    private void printSinusoidal() {
        for(int i:this.data)
            System.out.println(i);
    }
    
}
