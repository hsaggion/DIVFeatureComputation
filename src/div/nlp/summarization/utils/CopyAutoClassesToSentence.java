/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.utils;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class CopyAutoClassesToSentence {
    
    
   // public static String inLoc="/home/horacio/work/data/dr_inventor_docs/DIV_WITH_AUTO_CLASSES";
   // public static String outLoc="/home/horacio/work/data/dr_inventor_docs/DIV_WITH_AUTO_CLASSES_2";
    
    public static String inLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
    public static String outLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
    
    public static void main(String[] args) {
        
        File inDir=new File(inLoc);
        PrintWriter pw;
        File[] files=inDir.listFiles();
        Document doc;
        String fname;
        String floc;
        try {
            Gate.init();
        
        
        for(int f=0;f<files.length ;f++) {
            try {
                fname=files[f].getName();
                floc=files[f].getAbsolutePath();
                if(fname.endsWith(".xml")) {
                    System.out.println(fname);
                    doc=Factory.newDocument(new URL("file:///"+floc));
                    copyFeatures(doc);
                    pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                    pw.print(doc.toXml());
                    pw.flush();
                    pw.close();
                    Factory.deleteResource(doc);
                }
            } catch (MalformedURLException ex) {
               ex.printStackTrace();
            } catch (ResourceInstantiationException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
        }
        
        } catch(GateException ge) {
            ge.printStackTrace();
        }
        
        
        
    }
    
    public static void copyFeatures(Document doc) {
        AnnotationSet sentences=doc.getAnnotations("Analysis").get("Sentence");
        AnnotationSet sentences_LOA=doc.getAnnotations("Analysis").get("Sentence_LOA");
        FeatureMap fm1, fm2;
        Long startS, endS;
        AnnotationSet auxLOA;
        Annotation sentLOA;
        String strKey;
        for(Annotation sentence : sentences) {
            fm1=sentence.getFeatures();
            startS=sentence.getStartNode().getOffset();
            endS  =sentence.getEndNode().getOffset();
            auxLOA=sentences_LOA.get(startS,endS);
            if(auxLOA.size()==1) {
                sentLOA=auxLOA.iterator().next();
                fm2=sentLOA.getFeatures();
                for(Object key : fm2.keySet()) {
                    strKey=(String)key;
                    if(strKey.startsWith("PROB_") || strKey.startsWith("rhet")) {
                        fm1.put(strKey, fm2.get(strKey));
                        
                    }
                }
                
            }
            
            
        }
        
        
    }
    
    
}
