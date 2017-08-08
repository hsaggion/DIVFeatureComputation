/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.util.GateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author horacio
 */
public class MainOrSecondarySection extends AbstractLanguageAnalyser  implements
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
        Long startSet=sentences.firstNode().getOffset();
        
        AnnotationSet h1;
        AnnotationSet h2;
        
        Long lastH1, lastH2;
        
        FeatureMap fm;
        Long startS, endS;
        double h1_before;
        double h1_after;
        for(Annotation sentence : sentences) {
            fm=sentence.getFeatures();
            startS=sentence.getStartNode().getOffset();
            endS  =sentence.getEndNode().getOffset();
            h1=all.get("H1",startSet,startS);
            h2=all.get("H2",startSet,startS);
            if(h1.isEmpty()) {
                lastH1=new Long(0);
            } else {
                lastH1=h1.lastNode().getOffset();
            }
            if(h2.isEmpty()) {
                lastH2=new Long(0);
            } else {
                
                lastH2=h2.lastNode().getOffset();
            }
            if(lastH1.longValue() > lastH2.longValue()) {
                fm.put("in_structure", "1");
            } else {
                fm.put("in_structure", "0.5");
            }
            
        }
        
    }
    
    public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outDir="/home/horacio/temp";
        try {
            Gate.init();
            MainOrSecondarySection section=new MainOrSecondarySection();
            doc=Factory.newDocument(
                    new URL("file:/home/horacio/work/DrInventor/resources/gold_review_features/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning_FINAL_1_GATE.xml"));           
            section.setDocument(doc);
            section.setSentenceAnnSet("Analysis");
            section.setSentAnn("Sentence");
            section.execute();
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
