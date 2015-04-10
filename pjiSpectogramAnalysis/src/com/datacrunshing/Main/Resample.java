/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import java.io.FileNotFoundException;

/**
 *
 * @author rkouere
 */
public class Resample extends Average {
    private byte[] sampleData;
    public Resample(String[] args) throws FileNotFoundException {
        init(args);
        
        this.sampleData = new byte[(int)this.samples[0].length()];
    }
    
}
