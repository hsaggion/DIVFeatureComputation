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
public class NormalizeFeatureValue extends AbstractLanguageAnalyser  implements
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
    
    public String feat;
    public String getFeat() {
        return feat;
    }
    public void setFeat(String f) {
        feat=f;
    }
    
    public double max;
    public double getMax() {
        return max;
    }
    public void setMax(double m) {
        max=m;
    }
    
    
    public double min;
    public double getMin() {
        return min;
    }
    public void setMin(double m) {
        min=m;
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
        if(sentences==null || sentences.isEmpty()) return;
        Long startSet=sentences.firstNode().getOffset();
        
        String val;
        double dval;
        FeatureMap fm;
        double max_val=getMin();
        double min_val=getMax();
        
        for(Annotation sentence : sentences) {
            fm=sentence.getFeatures();
            if(fm.containsKey(feat)) {
                val=(String)fm.get(feat);
                dval=(new Double(val)).doubleValue();
                if(dval>max_val) {
                    max_val=dval;
                }
                if(dval<min_val) {
                    min_val=dval;
                }
            }
            
        }
        
        for(Annotation sentence : sentences) {
            fm=sentence.getFeatures();
            if(fm.containsKey(feat)) {
                val=(String)fm.get(feat);
                dval=(new Double(val)).doubleValue();
            } else {
                dval=0.0;
            }
            if(max_val!=min_val) {
                fm.put(feat+"_norm", ((dval-min_val)/(max_val-min_val))+"");
            } else {
                 fm.put(feat+"_norm", "0.0");
            }
            
        }
        
    }
    
    
    
    
     public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/temp/SciSUM-2016/test-last/gold_scores_features";
       
        String inLoc="/home/horacio/temp/SciSUM-2016/test-last/gold_scores_features";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        NormalizeFeatureValue fv=new NormalizeFeatureValue();
        fv.setFeat("textrank_score");
        fv.setMax(100);
        fv.setMin(0);
        fv.setSentenceAnnSet("");
        fv.setSentAnn("Sentence");
        try {
            Gate.init();
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                System.out.println(fname+"...");
                if(fname.endsWith(".xml")) {
                   // if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
                        fv.setDocument(doc);
                        fv.execute();
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();
                        Factory.deleteResource(doc);
            //    } else {
             //         System.out.println("Computing "+fname+"...SKIP");
               // }
                
                
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
