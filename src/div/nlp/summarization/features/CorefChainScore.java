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
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author horacio
 */
public class CorefChainScore extends AbstractLanguageAnalyser  implements
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
    
    String corefChainSet;
    public void setCorefChainSet(String ch) {
        corefChainSet=ch;
    }
    public String getCorefChainSet() {
        return corefChainSet;
    }
    
    public Resource init() {
        return this;
    }
    
    public void execute() {
        
        Document doc=getDocument();
        
        AnnotationSet all;
        AnnotationSet corefSet;
        
        if(sentenceAnnSet.equals("")) {
            all=doc.getAnnotations();
        } else {
            all=doc.getAnnotations(sentenceAnnSet);
        }
        
        // coref chains
        if(corefChainSet.equals("")) {
            corefSet=doc.getAnnotations();
        } else {
            corefSet=doc.getAnnotations(corefChainSet);
        }
        
        AnnotationSet sentences;
        sentences=all.get(sentAnn);
        
        // how many elements in coref all coref chains
        double num_coref_eles=corefSet.size();
        double num_coref;
        FeatureMap fm;
        Long startS,endS;
        AnnotationSet corefs;
        
        // compute size of all coref chains
        
        // collect names of coref chains (i.e. type of annotation)
        double coref_size=0;
        TreeSet<String> corefChains=new TreeSet();
        for(Annotation coref : corefSet) {
            corefChains.add(coref.getType());
        }
        num_coref=corefChains.size();
        AnnotationSet auxCoref;
        AnnotationSet auxSents;
        Long startCoref;
        Long endCoref;
        
        // store in map coref chain type and the size of the chain (how many sentences it covers)
        // in fact difference last_sent - first_sent +1
        
        // also add these sizes up for all chains
        Map<String,Double> corefSizes=new TreeMap();
        for(String coref : corefChains) {
            auxCoref=corefSet.get(coref);
            startCoref=auxCoref.firstNode().getOffset();
            endCoref  =auxCoref.lastNode().getOffset();
            auxSents  =sentences.get(startCoref,endCoref);
            corefSizes.put(coref, new Double(auxSents.size()));
            coref_size=coref_size+auxSents.size();
        }
        
        double sum_coref;
        
        // for each sentence compute
        // how many chains are present in the sentence divided by the total number of coref chains
        double unique_chains;
        double sent_coref_size;
        TreeSet<String> uniqueChains;
        for(Annotation sentence : sentences) {
            startS=sentence.getStartNode().getOffset();
            endS  =sentence.getEndNode().getOffset();
            corefs=corefSet.get(startS,endS);
            // how many unique chains
            
            uniqueChains=new TreeSet();
            for(Annotation ele: corefs) {
                uniqueChains.add(ele.getType());
            }
            unique_chains=uniqueChains.size();
            
            fm=sentence.getFeatures();
            sent_coref_size=corefs.size();
            fm.put("num_coref_eles", sent_coref_size);
            fm.put("num_unique_eles", unique_chains);
            
           // if(corefs.size()!=unique_chains) System.out.println(corefs.size()+" -  "+unique_chains);
            fm.put("coref_density", sent_coref_size/num_coref_eles);
            fm.put("coref_density_unique", unique_chains/num_coref);
            
            sum_coref=0.0;
            for(String coref : uniqueChains) {
                sum_coref=sum_coref+corefSizes.get(coref).doubleValue();
            }
            fm.put("coref_summ",sum_coref);
            fm.put("coref_score", sum_coref/coref_size);
        }
        
        
        
        
    }
    
    
    public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outDir="/home/horacio/temp";
        try {
            Gate.init();
            CorefChainScore scorer=new CorefChainScore();
            doc=Factory.newDocument(
                    new URL("file:/home/horacio/work/DrInventor/resources/gold_review_features/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning_FINAL_1_GATE.xml"));           
            scorer.setDocument(doc);
            scorer.setSentenceAnnSet("Analysis");
            scorer.setSentAnn("Sentence");
            scorer.setCorefChainSet("CorefChains");
            scorer.execute();
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
