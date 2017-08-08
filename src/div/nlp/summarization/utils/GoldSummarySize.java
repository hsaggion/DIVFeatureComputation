/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.utils;

import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class GoldSummarySize {
    
    public Map<String,Integer> summ_sizes;
    
    public GoldSummarySize() {
        summ_sizes=new TreeMap();
    }
    
    public void addSummSize(String key,Integer v) {
        
        summ_sizes.put(key, v);
    }
    
    public Integer getSize(String summ) {
        return summ_sizes.get(summ);
    }
    public boolean hasSize(String summ) {
        return summ_sizes.containsKey(summ);
    }
    public void loadSummaries(String loc) {
        
        File inDir=new File(loc);
        File[] flist=inDir.listFiles();
        String floc;
        String fname;
        String key;
        int size;
        Document doc;
        for(File file : flist) {
            floc=file.getAbsolutePath();
            fname=file.getName();
            if(fname.endsWith(".xml")) {
                key=fname.substring(0, 7);
                System.out.println(key);
                try {
                    doc=Factory.newDocument(new URL("file://"+floc), "UTF-8");
                    size=doc.getAnnotations().get("Token").size();
                   
                    addSummSize(key,new Integer(size));
                    Factory.deleteResource(doc);
                } catch (ResourceInstantiationException ex) {
                   ex.printStackTrace();
                } catch (MalformedURLException ex) {
                   ex.printStackTrace();
                }
                
            }
            
        }
        
        
    }
    
    
    public static void main(String[] args) {
        
        
        
        try {
            
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/GOLD_SUMMARIES_XML");
            System.out.println(sizes.getSize("A32_C02"));
        } catch(GateException ge) {
            ge.printStackTrace();
        }
    }
    
    
    
}
