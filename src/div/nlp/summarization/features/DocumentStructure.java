/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.Resource;
import gate.Factory;
import gate.creole.AbstractLanguageAnalyser;
import gate.util.GateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class DocumentStructure extends AbstractLanguageAnalyser  implements
ProcessingResource, Serializable {
    
    // the document to process
    Document document;
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document d) {
        document=d;
    }
    // the input annotation set
    String sentenceAnnSet;
    public String getSentenceAnnSet() {
        return sentenceAnnSet;
    }
    public void setSentenceAnnSet(String as) {
        sentenceAnnSet=as;
    }
    
    // sentence 
    
    String sentAnn;
    public String getSentAnn() {
        return sentAnn;
    }
    public void setSentAnn(String sa) {
        sentAnn=sa;
    }
    
    public Resource init() {
        return this;
    }
    
    public void execute() {
        
        Document doc=getDocument();
        
        AnnotationSet all;
        
        if(sentenceAnnSet.equals("")) {
            all=doc.getAnnotations();
        } else {
            all=doc.getAnnotations(sentenceAnnSet);
        }
        
        AnnotationSet sentences;
        sentences=all.get(sentAnn);
        
        AnnotationSet h1=all.get("H1");
        AnnotationSet h2=all.get("H2");
        
        double num_h1=h1.size();
        double num_h2=h2.size();
        
        Long startH1=h1.firstNode().getOffset();
        Long endH1  =h1.lastNode().getOffset();
        
        FeatureMap fm;
        Long startS, endS;
        double h1_before;
        double h1_after;
        for(Annotation sentence : sentences) {
            fm=sentence.getFeatures();
            startS=sentence.getStartNode().getOffset();
            endS  =sentence.getEndNode().getOffset();
      //      System.out.println(startH1+" - "+startS);
            if(startH1.doubleValue()<startS.doubleValue()) {
                h1_before=all.get("H1",startH1,startS).size();
            } else {
                h1_before=0;
            }
            if(endS.doubleValue()<endH1.doubleValue()) {
                h1_after=all.get("H1",endS,endH1).size();
            } else {
                h1_after=0;
            }
            fm.put("H1_after", h1_after);
            fm.put("H1_before", h1_before);
            
            if(num_h1>0) {
                fm.put("H1_after_score",h1_after/num_h1);
                fm.put("H1_before_score", h1_before/num_h1);
            } else {
                fm.put("H1_after_score",0.0);
                fm.put("H1_before_score",0.0);
                
            }
            
        }
        
        
        
        
    }
    
    
    
    public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outDir="/home/horacio/temp";
        try {
            Gate.init();
            DocumentStructure ds=new DocumentStructure();
            doc=Factory.newDocument(
                    new URL("file:/home/horacio/work/DrInventor/resources/gold_review_features/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning_FINAL_1_GATE.xml"));           
            ds.setDocument(doc);
            ds.setSentAnn("Sentence");
            ds.setSentenceAnnSet("Analysis");
            ds.execute();
            pw=new PrintWriter(new FileWriter(outDir+File.separator+"div_1.xml"));
            pw.println(doc.toXml());
            pw.flush();
            pw.close();
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    }
    
}
   