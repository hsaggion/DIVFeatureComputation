/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Gate;
import gate.util.GateException;
import gate.Factory;



import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class ComputeBabelTable {
    
    
    
    public static void main(String[] args) {
        String inLoc="/home/horacio/work/SciSUM-2016/DATA-2017/C00-2123";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        Document doc;
        Corpus corpus;
        AnnotationSet all;
        AnnotationSet entities;
        try {
            Gate.init();
            corpus=Factory.newCorpus("");
            for(File file : flist) {
                
                fname=file.getName();
                floc=file.getAbsolutePath();
                doc=Factory.newDocument(new URL("file://"+floc), "UTF-8");
                all=doc.getAnnotations("Analysis");
                entities=doc.getAnnotations("Babelnet");
                all.addAll(entities);
                
                corpus.add(doc);
            }
            
            
            
            summa.resources.frequency.OnTheFlyInvertedTable table=
                    new summa.resources.frequency.OnTheFlyInvertedTable();
            table.setCreateTable(Boolean.TRUE);
            table.setTableLocation(new URL("file:////home/horacio/work/SciSUM-2016/DATA-2017/IDFs/BabelNet/babelnet_2017.idf"));
            table.setEncoding("UTF-8");
            table.setNormalised(Boolean.FALSE);
            table.setInputAnnotationSet("Analysis");
            table.setInputAnnotationType("Entity");
            table.setFeatureName("synsetID");
            table.setCorpus(corpus);
            table.init();
            
            
            
        } catch(GateException ge) {
            
            ge.printStackTrace();
        } catch (MalformedURLException murle) {
           murle.printStackTrace();
        }
        
        
    }
    
}
