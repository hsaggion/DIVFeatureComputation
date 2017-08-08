/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import div.nlp.summarization.experiment.LeaveOneOutExperiment;
import div.nlp.summarization.utils.GoldSummarySize;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class ComputeSummariesFromModel {
    
    
    
  //  public static String corpusLoc="/home/horacio/temp/div_all";
   // public static String corpusLoc="/home/horacio/temp/div_faltantes";
   // public static String modelLoc="/home/horacio/temp/output_experiments";
  //  public static String corpusLoc="/home/horacio/temp/div_faltantes";
    // public static String modelLoc="/home/horacio/temp/output_experiments";
    
    
    public static String corpusLoc="/home/horacio/work/data/dr_inventor_docs/DIV_WITH_AUTO_CLASSES_2";
    public static String modelLoc="/home/horacio/work/data/dr_inventor_docs/output_models_classes";
    
    public ComputeSummariesFromModel() {
        
    }
    
    
    ArrayList<String> features;
    ArrayList<Double> weights;
    
    public void loadLRModel(String modelLoc) {
        BufferedReader reader;
        String line;
        String feature;
        String value;
        features=new ArrayList();
        weights=new ArrayList();
        
        int pos;
        try {
            reader=new BufferedReader(new FileReader(modelLoc));
            while((line=reader.readLine()
                    )!=null) {
                
                pos=line.indexOf("\t");
                feature=line.substring(0, pos);
                value=line.substring(pos+1,line.length()-1);
                features.add(feature);
                weights.add(new Double(value));
                System.out.println(feature+"\t"+value);
            }
            
           
            
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        
    }
    
    public static void main(String[] args) {
        
        ComputeSummariesFromModel computator=new ComputeSummariesFromModel();
        
        File inDir=new File(corpusLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        Document doc;
        
        try {
            Gate.init();
            computator.initSummarizer();
            
            for(int f=0;f<flist.length ;f++) {
                fname=flist[f].getName();
                floc=flist[f].getAbsolutePath();
             
                fname=fname.substring(0, 7);
                System.out.println("Computing "+fname+"...");
                computator.loadLRModel(modelLoc+File.separator+fname+".model");
                doc=Factory.newDocument(new URL("file:///"+floc));
                computator.summarizeDocument(doc);
                Factory.deleteResource(doc);

            }
        
        } catch(GateException ge) {
            
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ComputeSummariesFromModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
       
    }
    
    
    
    summa.SimpleSummarizer summarizer ;
    summa.summarizer.ExportSelectedSentences exporter;
    GoldSummarySize sizes;
    public void initSummarizer() {
        
        try {
            summarizer = new summa.SimpleSummarizer();
            exporter= new summa.summarizer.ExportSelectedSentences();
            summarizer.init();
            exporter.init();
            sizes=new GoldSummarySize();
            sizes.loadSummaries(goldSummaries);
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(LeaveOneOutExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    public String goldSummaries="/home/horacio/work/data/dr_inventor_docs/NEW_GOLD_SUMMARIES_XML"; 
    
    public void summarizeDocument(Document doc) {
        
       
        try {
            AnnotationSet H1;
            Long startH1;
            AnnotationSet all;
            AnnotationSet to_extract;
            String fname;
            // only sentences after the first section
            
            AnnotationSet sentences;
            AnnotationSet tokens;
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("DIV");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
            summarizer.setSumFeatures(features);
            summarizer.setSumWeigths(weights);
            
            fname=doc.getName();
            String key;
            Integer targetSize;
            System.out.println("Computing "+fname+"...");
            key=fname.substring(0, 7);
            if(sizes.hasSize(key)) {
                targetSize=sizes.getSize(key);

                all=doc.getAnnotations("Analysis");
                to_extract=doc.getAnnotations("TO_EXTRACT");
                H1=all.get("H1");
                startH1=H1.firstNode().getOffset();

                // only sentences after the first section


                sentences=all.get("Sentence",startH1,all.lastNode().getOffset());
                tokens=all.get("Token",startH1,all.lastNode().getOffset());
                to_extract.addAll(sentences);
                to_extract.addAll(tokens);
                summarizer.setAnnSetName("TO_EXTRACT");
                summarizer.setCompression(targetSize);

                summarizer.setDocument(doc);
                summarizer.execute();
                exporter.setDocument(doc);
                exporter.setAnnotationSet("DIV");
                exporter.setAnnotationType("Sentence");
                exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/DIV_SUMMARIES_ALL_2_TXT"));
                exporter.execute();
            
            }
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
          
             
                
    
    
           
        
        
    }
    
}
