/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class CreateCueWordList {
    
    
    
    
    public static void main(String[] args) {
        
        String inLoc="/home/horacio/work/DIVFeatureComputation/resources/cue_words.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(inLoc));
            String line;
            while((line=reader.readLine())!=null) {
                System.out.println(line+":count=1");
                
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    
    
}
    
    
    
