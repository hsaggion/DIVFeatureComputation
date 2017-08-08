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
import summa.centroid.Centroid;


import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


/**
 *
 * @author horacio
 */
public class InSecCentroid extends AbstractLanguageAnalyser  implements
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
    
    // vector
    String vecAnn;
    public String getVecAnn() {
        return vecAnn;
        
    }
    
    public void setVecAnn(String va) {
        
        vecAnn=va;
    }
    
    // "section" annotation name
    
    String secAnn;
    public String getSecAnn() {
        return secAnn;
    }
    public void setSecAnn(String sa) {
        secAnn=sa;
    }
    
    public String inSecCentroid;
    public String getInSecCentroid() {
        return inSecCentroid;
    }
    public void setInSecCentroid(String feat) {
        inSecCentroid=feat;
    }
    
    public Resource init() {
        return this;
    }
    
    public void execute() {
        
        
        Document doc=getDocument();
        
        AnnotationSet all;
        
        AnnotationSet sentences;
        
        AnnotationSet vectors;
        
        AnnotationSet sections;
        
        
        if(sentenceAnnSet.equals("")) {
            all=doc.getAnnotations();
        } else {
            all=doc.getAnnotations(sentenceAnnSet);
        }
        
        sentences=all.get(sentAnn);
        
        sections=all.get(secAnn);

        vectors=all.get(vecAnn);
        
        Long startS,endS;
        Long startSec, endSec;
        
        
        
        // centroid in each section
        
        AnnotationSet secSentences;
        AnnotationSet secVectors;
        
        FeatureMap secCentroid;
        Set<FeatureMap> fms;
        FeatureMap vecfm;
        Annotation sentence;
        FeatureMap sentfm;
        AnnotationSet auxsents;
        double cosval;
        for(Annotation section : sections) {
            
            startSec=section.getStartNode().getOffset();
            endSec  =section.getEndNode().getOffset();
            secSentences=sentences.get(startSec,endSec);
            secVectors=vectors.get(startSec,endSec);
            fms=new HashSet();
            for(Annotation vector : secVectors) {
                fms.add(vector.getFeatures());
            }
            
            secCentroid=summa.centroid.Centroid.Centroid1(fms);
            
            // compte similarity against each vector
            
            for(Annotation vector : secVectors) {
                startS=vector.getStartNode().getOffset();
                endS  =vector.getEndNode().getOffset();
                vecfm =vector.getFeatures();
                cosval=summa.scorer.Cosine.cosine1(vecfm, secCentroid);
                auxsents=sentences.get(startS,endS);
                if(auxsents.size()==1) {
                    sentence=auxsents.iterator().next();
                    sentfm=sentence.getFeatures();
                    sentfm.put(inSecCentroid,cosval+"");
                    
                }
            }
            
            
            
            
        }
        
        
    }
    
     public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outDir="/home/horacio/temp";
        try {
            Gate.init();
            InSecCentroid inSecCent=new InSecCentroid();
            doc=Factory.newDocument(
                    new URL("file:/home/horacio/work/DrInventor/resources/gold_review_features/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning_FINAL_1_GATE.xml"));           
            inSecCent.setDocument(doc);
            inSecCent.setSentenceAnnSet("Analysis");
            inSecCent.setSentAnn("Sentence");
            inSecCent.setVecAnn("Vector_Norm");
            inSecCent.setSecAnn("Section");
            inSecCent.setInSecCentroid("in_sec_centroid");
            inSecCent.execute();
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
