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
public class CitMarkerCount extends AbstractLanguageAnalyser  implements
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
    
    
    // citation marker
    
    String citAnn;
    public String getCitAnn() {
        return citAnn;
    }
    public void setCitAnn(String ca) {
        citAnn=ca;
    }
    
    
    // citation feature
    
    String citCount;
    public String getCitCount() {
        return citCount;
    }
    public void setCitCount(String cc) {
        citCount=cc;
    }
    
    
    
    // citation feature
    String citFeat;
    public String getCitFeat() {
        return citFeat;
    }
    public void setCitFeat(String cf) {
        citFeat=cf;
    }
    
    public Resource init() {
     return this;   
    }
    
    
     public void execute() {
        
        
        Document doc=getDocument();
        
        AnnotationSet all;
        
        AnnotationSet sentences;
        
        AnnotationSet markers;
        
        if(sentenceAnnSet.equals("")) {
            all=doc.getAnnotations();
        } else {
            all=doc.getAnnotations(sentenceAnnSet);
        }
        
        sentences=all.get(sentAnn);
        
        double countMarkers;
        double maxCount;
        double minCount;
        
        markers=all.get(citAnn);
        minCount=markers.size();
        maxCount=0;
        
        FeatureMap sentfm;
        Long startS,endS;
        
        
        
        for(Annotation sentence : sentences) {
            startS=sentence.getStartNode().getOffset();
            endS  =sentence.getEndNode().getOffset();
            sentfm=sentence.getFeatures();
            countMarkers=(markers.get(citAnn,startS,endS)).size();
            System.out.println(countMarkers);
            
            if(countMarkers>maxCount) {
                maxCount=countMarkers;
            }
            
            if(countMarkers<minCount) {
                minCount=countMarkers;
            }
            
            sentfm.put(citCount, new Double(countMarkers));
            
        }
        
        double citRatio;
        Double countM;
        for(Annotation sentence : sentences) {
          
            sentfm=sentence.getFeatures();
            countM=(Double)sentfm.get(citCount);
            if(maxCount!=minCount) {
              citRatio=(countM.doubleValue()-minCount)/(maxCount-minCount);
            } else citRatio = 0.0;
            sentfm.put(citFeat, citRatio+"");
        }
        
     }
     
     
     
     public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        String inLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        File inDir=new File(inLoc);
        String fname;
        try {
           Gate.init();
           CitMarkerCount citCounter=new CitMarkerCount();
           
            File[] flist=inDir.listFiles();
           
            citCounter.setSentenceAnnSet("Analysis");
            citCounter.setSentAnn("Sentence");
            
            citCounter.setCitAnn("CitMarker");
            citCounter.setCitCount("citCount");
            citCounter.setCitFeat("citRatio");
            
            
            
            for(int f=0;f<flist.length ;f++) {
                fname=flist[f].getName();
                doc=Factory.newDocument(
                   new URL("file:///"+flist[f].getAbsolutePath()));           
 
                citCounter.setDocument(doc);
                citCounter.execute();
                pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                pw.println(doc.toXml());
                pw.flush();
                pw.close();
                Factory.deleteResource(doc);
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
