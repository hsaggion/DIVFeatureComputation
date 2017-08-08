/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import div.nlp.summarization.utils.GoldSummarySize;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.GateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * @author horacio
 */
public class ComputeSummaries {
    
    
 
    public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/data/dr_inventor_docs/TEXTRANK_SUMMARIES_ALL_XML";
       
        String inLoc="/home/horacio/temp/div_all";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        summa.SimpleSummarizer summarizer = new summa.SimpleSummarizer();
        summa.summarizer.ExportSelectedSentences exporter= new summa.summarizer.ExportSelectedSentences();
     
        AnnotationSet H1;
        Long startH1;
        AnnotationSet all;
        AnnotationSet to_extract;
        // only sentences after the first section
        
        AnnotationSet sentences;
        AnnotationSet tokens;
       
        try {
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/NEW_GOLD_SUMMARIES_XML");
            summarizer.init();
            exporter.init();
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("TEXTRANK");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
          
            ArrayList<String> fnames=new ArrayList();
            fnames.add("textrank_score_norm");
            ArrayList<Double> wnames=new ArrayList();
            wnames.add(new Double(1.0));
            summarizer.setSumFeatures(fnames);
            summarizer.setSumWeigths(wnames);
           
            int count=0;
            String key;
            Integer targetSize;
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
               //     if(count>=3) break;
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        key=fname.substring(0, 7);
                        targetSize=sizes.getSize(key);
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
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
                        exporter.setAnnotationSet("TEXTRANK");
                        exporter.setAnnotationType("Sentence");
                       
                        exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/TEXTRANK_SUMMARIES_ALL_TXT"));
                        exporter.execute();
                        /*
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                                */
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                count++;
                
            }
            
           
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    
    
    
    
}
    
    public static void main8(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/data/dr_inventor_docs/DIV2_SUMMARIES_XML";
       
        String inLoc="/home/horacio/temp/div_testing";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        summa.SimpleSummarizer summarizer = new summa.SimpleSummarizer();
        summa.summarizer.ExportSelectedSentences exporter= new summa.summarizer.ExportSelectedSentences();
     
        AnnotationSet H1;
        Long startH1;
        AnnotationSet all;
        AnnotationSet to_extract;
        // only sentences after the first section
        
        AnnotationSet sentences;
        AnnotationSet tokens;
       
        try {
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/GOLD_SUMMARIES_XML");
            summarizer.init();
            exporter.init();
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("DIV");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
          
            ArrayList<String> fnames=new ArrayList();
            
            
         //   fnames=loadFeatureNames();
            fnames=readFeatures();
            
            
            
            ArrayList<Double> wnames=new ArrayList();
            
            
            
          //  wnames=loadWeights();
            wnames=readWeights();
            
            summarizer.setSumFeatures(fnames);
            summarizer.setSumWeigths(wnames);
           
            int count=0;
            String key;
            Integer targetSize;
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
               //     if(count>=3) break;
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        key=fname.substring(0, 7);
                        targetSize=sizes.getSize(key);
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
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
                       
                        exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/DIV2_SUMMARIES_TXT"));
                        exporter.execute();
                        /*
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                                */
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                count++;
                
            }
            
           
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    
    
    
    
}
    
    public static void main7(String[] args) {
        readWeights();
        readFeatures();
    }
    
    public static ArrayList<Double> readWeights() {
        ArrayList<Double> ws=new ArrayList();
        StringTokenizer tokenizer=new StringTokenizer(text,"\n");
        String element;
        StringTokenizer tokenizeEle;
        String weight;
        while(tokenizer.hasMoreElements()) {
            element=tokenizer.nextToken();
            tokenizeEle=new StringTokenizer(element,"*");
            weight=tokenizeEle.nextToken();
            ws.add(new Double(weight));
        }
        
        return ws;
    }
    public static ArrayList<String> readFeatures() {
        ArrayList<String> fs=new ArrayList();
        StringTokenizer tokenizer=new StringTokenizer(text,"\n");
        String element;
        StringTokenizer tokenizeEle;
        String weight;
        String feature;
        while(tokenizer.hasMoreElements()) {
            element=tokenizer.nextToken();
            tokenizeEle=new StringTokenizer(element,"*");
            weight=tokenizeEle.nextToken();
            feature=tokenizeEle.nextToken();
            feature=feature.replace("+", "");
            feature=feature.trim();
            fs.add(feature);
        }
        
        return fs;
    }
    
    public static String text= 
    " -1.1957 * centroid_sim +\n" +
"      1.1351 * position_score +\n" +
"      0.2799 * relInSecTop +\n" +
"      0.7788 * relSecBottom +\n" +
"      0.4063 * relSecTop +\n" +
"     -0.1564 * title_sim +\n" +
"    -11.4105 * coref_density_unique +\n" +
"      1.1146 * abs_sim +\n" +
"     -0.3062 * citRatio +\n" +
"      0.32   * in_sec_centroid +\n" +
"      1.7905 * PROB_DRI_Approach +\n" +
"      1.3846 * PROB_DRI_Background +\n" +
"      2.0917 * PROB_DRI_Challenge +\n" +
"      3.0705 * PROB_DRI_FutureWork +\n" +
"      3.0975 * PROB_DRI_Outcome +\n" +
"      0.5853 * textrank_score_norm +\n" +
"     -0.7553 * tr_sec_score_norm +\n" +
"      0.5826 * tf_entity +\n" +
"     -0.0855 * entity_cent_sim +\n" +
"      0.1948 * tr_enti_sec_score_norm +\n" +
"      0.4308 * norm_cue +\n" +
"      2.7572 * coref_score +";
    
    
    
    public static ArrayList<Double> loadWeights() {
        ArrayList<Double> ws=new ArrayList();
        ws.add(new Double(-1.3164));
         ws.add(new Double(1.1752));
         ws.add(new Double(0.3109)); 
         ws.add(new Double(0.801));  
         ws.add(new Double(0.4078)); 
       ws.add(new Double(-15.722));  
         ws.add(new Double(1.108));  
        ws.add(new Double(-0.2764)); 
         ws.add(new Double(0.2704)); 
         ws.add(new Double(1.8273)); 
         ws.add(new Double(1.2897)); 
         ws.add(new Double(2.0285)); 
         ws.add(new Double(3.0745)); 
         ws.add(new Double(3.1227)); 
         ws.add(new Double(0.7934)); 
        ws.add(new Double(-0.7367)); 
         ws.add(new Double(0.5301)); 
         ws.add(new Double(0.1567)); 
         ws.add(new Double(0.5202)); 
        return ws;
             }
    
    public static ArrayList<String> loadFeatureNames() {
       
        ArrayList<String> fs=new ArrayList();
        fs.add("centroid_sim"); 
     fs.add("position_score");
      fs.add("relInSecTop");
       fs.add("relSecBottom");
       fs.add("relSecTop");
      fs.add("coref_density");
      fs.add("abs_sim");
      fs.add("citRatio");
      fs.add("in_sec_centroid");
     fs.add("PROB_DRI_Approach");
       fs.add("PROB_DRI_Background");
     fs.add("PROB_DRI_Challenge");
       fs.add("PROB_DRI_FutureWork");
       fs.add("PROB_DRI_Outcome");
       fs.add("textrank_score_norm");
      fs.add("tr_sec_score_norm");
       fs.add("tf_entity");
      fs.add("tr_enti_sec_score_norm");
      fs.add("norm_cue");
        
        return fs;
    }
      
    public static void main4(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/data/dr_inventor_docs/POSITION_SUMMARIES_XML";
       
        String inLoc="/home/horacio/temp/div_testing";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        summa.SimpleSummarizer summarizer = new summa.SimpleSummarizer();
        summa.summarizer.ExportSelectedSentences exporter= new summa.summarizer.ExportSelectedSentences();
     
        AnnotationSet H1;
        Long startH1;
        AnnotationSet all;
        AnnotationSet to_extract;
        // only sentences after the first section
        
        AnnotationSet sentences;
        AnnotationSet tokens;
       
        try {
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/GOLD_SUMMARIES_XML");
            summarizer.init();
            exporter.init();
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("POSITION");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
          
            ArrayList<String> fnames=new ArrayList();
            fnames.add("position_score");
            ArrayList<Double> wnames=new ArrayList();
            wnames.add(new Double(1.0));
            summarizer.setSumFeatures(fnames);
            summarizer.setSumWeigths(wnames);
           
            int count=0;
            String key;
            Integer targetSize;
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
               //     if(count>=3) break;
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        key=fname.substring(0, 7);
                        targetSize=sizes.getSize(key);
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
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
                        exporter.setAnnotationSet("POSITION");
                        exporter.setAnnotationType("Sentence");
                       
                        exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/POSITION_SUMMARIES_TXT"));
                        exporter.execute();
                        /*
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                                */
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                count++;
                
            }
            
           
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    
    
    
    
}
    public static void main3(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/data/dr_inventor_docs/TF_SUMMARIES_XML";
       
        String inLoc="/home/horacio/temp/div_testing";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        summa.SimpleSummarizer summarizer = new summa.SimpleSummarizer();
        summa.summarizer.ExportSelectedSentences exporter= new summa.summarizer.ExportSelectedSentences();
     
        AnnotationSet H1;
        Long startH1;
        AnnotationSet all;
        AnnotationSet to_extract;
        // only sentences after the first section
        
        AnnotationSet sentences;
        AnnotationSet tokens;
       
        try {
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/GOLD_SUMMARIES_XML");
            summarizer.init();
            exporter.init();
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("FREQUENCY");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
          
            ArrayList<String> fnames=new ArrayList();
            fnames.add("tf_score");
            ArrayList<Double> wnames=new ArrayList();
            wnames.add(new Double(1.0));
            summarizer.setSumFeatures(fnames);
            summarizer.setSumWeigths(wnames);
           
            int count=0;
            String key;
            Integer targetSize;
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
               //     if(count>=3) break;
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        key=fname.substring(0, 7);
                        targetSize=sizes.getSize(key);
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
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
                        exporter.setAnnotationSet("FREQUENCY");
                        exporter.setAnnotationType("Sentence");
                       
                        exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/TF_SUMMARIES_TXT"));
                        exporter.execute();
                        /*
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                                */
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                count++;
                
            }
            
           
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    
    
    
    
}
    public static void main2(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/data/dr_inventor_docs/CENTROID_SUMMARIES_XML";
       
        String inLoc="/home/horacio/temp/div_testing";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        summa.SimpleSummarizer summarizer = new summa.SimpleSummarizer();
        summa.summarizer.ExportSelectedSentences exporter= new summa.summarizer.ExportSelectedSentences();
     
        AnnotationSet H1;
        Long startH1;
        AnnotationSet all;
        AnnotationSet to_extract;
        // only sentences after the first section
        
        AnnotationSet sentences;
        AnnotationSet tokens;
       
        try {
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/GOLD_SUMMARIES_XML");
            summarizer.init();
            exporter.init();
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("CENTROID");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
          
            ArrayList<String> fnames=new ArrayList();
            fnames.add("centroid_sim");
            ArrayList<Double> wnames=new ArrayList();
            wnames.add(new Double(1.0));
            summarizer.setSumFeatures(fnames);
            summarizer.setSumWeigths(wnames);
           
            int count=0;
            String key;
            Integer targetSize;
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
                  
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        key=fname.substring(0, 7);
                        targetSize=sizes.getSize(key);
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
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
                        exporter.setAnnotationSet("CENTROID");
                        exporter.setAnnotationType("Sentence");
                       
                        exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/CENTROID_SUMMARIES_TXT"));
                        exporter.execute();
                        /*
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                                */
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                count++;
                
            }
            
           
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    
    
    
    
}
    
    
    public static void main1(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/work/data/dr_inventor_docs/TEXTRANK_SUMMARIES_XML";
       
        String inLoc="/home/horacio/temp/div_testing";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        summa.SimpleSummarizer summarizer = new summa.SimpleSummarizer();
        summa.summarizer.ExportSelectedSentences exporter= new summa.summarizer.ExportSelectedSentences();
     
        AnnotationSet H1;
        Long startH1;
        AnnotationSet all;
        AnnotationSet to_extract;
        // only sentences after the first section
        
        AnnotationSet sentences;
        AnnotationSet tokens;
       
        try {
            Gate.init();
            GoldSummarySize sizes=new GoldSummarySize();
            sizes.loadSummaries("/home/horacio/work/data/dr_inventor_docs/GOLD_SUMMARIES_XML");
            summarizer.init();
            exporter.init();
            summarizer.setScoreOnly(Boolean.FALSE);
            summarizer.setWordAnn("Token");
            summarizer.setSumSetName("TEXTRANK");
            summarizer.setCompression(new Integer(200));
            summarizer.setSentCompression(Boolean.FALSE);
            summarizer.setSentAnn("Sentence");
            summarizer.setNewDocument(Boolean.FALSE);
          
            ArrayList<String> fnames=new ArrayList();
            fnames.add("textrank_score_norm");
            ArrayList<Double> wnames=new ArrayList();
            wnames.add(new Double(1.0));
            summarizer.setSumFeatures(fnames);
            summarizer.setSumWeigths(wnames);
           
            int count=0;
            String key;
            Integer targetSize;
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
               //     if(count>=3) break;
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        key=fname.substring(0, 7);
                        targetSize=sizes.getSize(key);
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
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
                        exporter.setAnnotationSet("TEXTRANK");
                        exporter.setAnnotationType("Sentence");
                       
                        exporter.setDirName(new File("/home/horacio/work/data/dr_inventor_docs/TEXTRANK_SUMMARIES_TXT"));
                        exporter.execute();
                        /*
                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+fname));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                                */
                        Factory.deleteResource(doc);
                } else {
                      System.out.println("Computing "+fname+"...SKIP");
                }
                count++;
                
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
