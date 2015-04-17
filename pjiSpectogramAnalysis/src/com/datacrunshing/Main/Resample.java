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
import java.util.List;

/**
 * Exports a file from the top of the first elipse to the top of the last elipse
 * @author rkouere
 */
public class Resample extends GetFileInfo {
    private FileOutputStream fileOutputStream = null;

    
    public Resample(List<String> args) throws FileNotFoundException, IOException {
        super(args);
        
        this.fileOutputStream = Tools.openOutputStream(this.samples[0], "newSample.bin");
        exportFile();
        this.fileOutputStream.close();
    }
    
    
    private void exportFile() throws IOException {
        for(int i = getIndexFirstTopSinusoidal(); i <= getIndexLastTopSinusoidal(); i++) {
            fileOutputStream.write(intToByte(this.getSampleData()[i]), 0, Tools.dataSize);
        }
    }

    

    
}
