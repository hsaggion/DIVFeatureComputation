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
 * Copies gs value (gold standard summary value) to sentences in case of inexistent
 * puts a default value (0, -5)
 * @author horacio
 */
public class CopyGSToDocuments extends AbstractLanguageAnalyser  implements
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
    
    // gold standard annotation set
    String gsAnnSet;
    public String getGsAnnSet() {
        return gsAnnSet;
    }
    public void setGsAnnSet(String gs) {
        gsAnnSet=gs;
    }
    
    // gold standard summary sentence
    
    
    String gsSentence;
    public String getGsSentence() {
        return gsSentence;
    }
    public void setGsSentence(String gs) {
        gsSentence=gs;
    }
    
    public double min;
    public double getMin() {
        return min;
    }
    public void setMin(double m) {
        min=m;
    }
    public Resource init() {
        min=0.0;
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
        
        
        AnnotationSet gold;
        if(gsAnnSet.equals("")) {
            gold=doc.getAnnotations();
        } else {
            gold=doc.getAnnotations(gsAnnSet);
        }
        
        // gold standard sentences
        AnnotationSet goldSents=gold.get(gsSentence);
        
        
        AnnotationSet H1=all.get("H1");
        Long startH1=H1.firstNode().getOffset();
        
        // only sentences after the first section
        
        AnnotationSet sentences;
        sentences=all.get(sentAnn,startH1,all.lastNode().getOffset());
        
        Long startS,endS;
        FeatureMap fmSent;
        FeatureMap fmGold;
        Annotation goldSent;
        AnnotationSet auxGold;
        double gs;
        for(Annotation sentence : sentences) {
            
            fmSent=sentence.getFeatures();
            startS=sentence.getStartNode().getOffset();
            endS  =sentence.getEndNode().getOffset();
            auxGold=goldSents.get(startS,endS);
            if(auxGold.size()>=1) {
                goldSent=(auxGold.iterator().next());
                fmGold=goldSent.getFeatures();
                if(fmGold.containsKey("gs")) {
                    gs=(new Double((String)fmGold.get("gs"))).doubleValue();
                } else {
                    gs=min;
                }
            } else {
                gs=min;
            }
            fmSent.put("gs", gs);
            
        }
        
        
    }
    
     public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/temp/test_div_3";
       
        String inLoc="/home/horacio/temp/test_div_2";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;

        try {
            Gate.init();
            CopyGSToDocuments toDocs=new CopyGSToDocuments();
            toDocs.setSentAnn("Sentence");
            toDocs.setSentenceAnnSet("Analysis");
            toDocs.setGsAnnSet("GoldStandard_SUMMARY");
            toDocs.setGsSentence("SUMM_SENTENCE");
            toDocs.setMin(0);
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
                        toDocs.setDocument(doc);
                        toDocs.execute();
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                
                
            }
            
           
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    
    
    
}
    
}
