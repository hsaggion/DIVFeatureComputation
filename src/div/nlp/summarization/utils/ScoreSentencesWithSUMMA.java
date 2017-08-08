/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.utils;

import gate.Gate;
import gate.util.GateException;

/**
 *
 * @author horacio
 */
public class ScoreSentencesWithSUMMA {
    
    
    
    public static void main(String[] args) {
        
        
        summa.SimpleSummarizer summarizer=new summa.SimpleSummarizer();
        try {
            
            Gate.init();
            
            summarizer.setCompression(Integer.SIZE);
            
            
        } catch(GateException ge) {
            ge.printStackTrace();
        }
        
    }
    
    
}
