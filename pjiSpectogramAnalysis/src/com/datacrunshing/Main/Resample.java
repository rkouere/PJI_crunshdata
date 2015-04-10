/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datacrunshing.Main;

import static com.datacrunshing.Main.Average.safeLongToInt;
import com.datacrunshing.tools.Tools;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author rkouere
 */
public class Resample extends AverageSingleSample {
    private FileOutputStream fileOutputStream = null;

    
    public Resample(String[] args) throws FileNotFoundException, IOException {
        super(args);
        this.fileOutputStream = Tools.openOutputStream(this.samples[0], "newSample.bin");
    }
    
    
    

    

    
}
