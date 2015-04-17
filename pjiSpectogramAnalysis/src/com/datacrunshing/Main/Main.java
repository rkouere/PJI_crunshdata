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
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        List<String> arguments = null;
        if(args.length < 3) {
            Tools.displayErrorAndExit(Tools.help);
        }
        else { 
            arguments = Tools.arrayStringToList(args);

            String arg = arguments.get(0);
            arguments.remove(0);
            
            switch(arg) {
                case "-info":
                    new GetFileInfo(arguments).printFileInfo();
                    break;
                case "-combineAvg":
                    new AveragingSamples(arguments).exportFile();
                    break;
                case "-resample":
                    new Resample(arguments);
                    break;
                case "-test":
                    new Average(arguments);
                    break;
                default:
                    Tools.displayErrorAndExit("Le programe prend au moins un argument. Tappez -h pour afficher l'aide.");
                    break;
            }
        }
//        
        //
    }
    
}
