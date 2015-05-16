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
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Exports a file from the top of the first sinusoidal to the top of the last sinusoidal
 * @author rkouere
 */
public class Resample extends GetFileInfo {
    

    
    public Resample(List<String> args) throws FileNotFoundException, IOException {
        super(args);
        
        this.fileOutputStream = Tools.openOutputStream(this.samples[0], this.output);
        exportFile(fileOutputStream, this.data, getFirstTop(), getLastTop());
        this.fileOutputStream.close();
    }
    

    

    
}
