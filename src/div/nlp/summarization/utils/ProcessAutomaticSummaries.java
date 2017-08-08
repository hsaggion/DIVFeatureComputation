/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.utils;

import gate.Document;
import gate.Gate;
import gate.ProcessingResource;
import gate.util.GateException;
import gate.Factory;


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
public class ProcessAutomaticSummaries {
    
  // public static String inLoc="/home/horacio/temp/SciSUM-2016/test/textrank"; 
  // public static String summ_type="TEXTRANK";
  // public static String outLoc="/home/horacio/temp/SciSUM-2016/test/textrank_xml";
   
   
   /*
      
   public static String inLoc="/home/horacio/temp/SciSUM-2016/test/community"; 
   public static String summ_type="SciSUMM-COMMUNITY";
   public static String outLoc="/home/horacio/temp/SciSUM-2016/test/community_xml";
   
   */
    /*
    public static String inLoc="/home/horacio/work/data/dr_inventor_docs/DIV_SUMMARIES_ALL_2_TXT"; 
   public static String summ_type="DIV";
   public static String outLoc="/home/horacio/work/data/dr_inventor_docs/DIV_SUMMARIES_ALL_2_XML";
   
   */
    public static String inLoc="/home/horacio/work/SciSUM-2016/EVALUATE-TEST/SUMMARIES"; 
   public static String summ_type="DIV";
   public static String outLoc="/home/horacio/work/SciSUM-2016/EVALUATE-TEST/SUMMARIES-XML";
   
     public static void main(String[] args) {
        
        
        File inDir=new File(inLoc);
        File inFile;
        File[] dlist=inDir.listFiles();
        File[] flist;
        String floc;
        String fname;
        Document doc;
        PrintWriter pw;
        
       
        ProcessingResource tokenizer;
        ProcessingResource splitter;
        ProcessingResource postag;
        
        String outName;
        
        try {
            Gate.init();
            Gate.getCreoleRegister().registerDirectories(new 
                  URL("file:///home/horacio/work/software/GATE-8.4.1/plugins/ANNIE"));
            tokenizer=(ProcessingResource)
                    Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
            splitter=(ProcessingResource)
                    Factory.createResource("gate.creole.splitter.SentenceSplitter");
            postag=(ProcessingResource)
                    Factory.createResource("gate.creole.POSTagger");
            
            String dname;
            String dloc;
            File faux;
            for(File dir : dlist) {
                
                dname=dir.getName();
                System.out.println("Processing "+dname);
                dloc=dir.getAbsolutePath();
                inFile=new File(dloc);
                flist=inFile.listFiles();
                faux=new File(outLoc+File.separator+dname);
                if(!faux.exists()) faux.mkdir();
                for(File file: flist) {

                    fname=file.getName();
                    floc=file.getAbsolutePath();
                    if(fname.endsWith(".summary")){

                        outName=fname.substring(0, 7)+"_"+summ_type+".xml";
                        System.out.println(fname+" ...");
                        doc=Factory.newDocument(new URL("file:///"+floc));
                        tokenizer.setParameterValue("annotationSetName", "Analysis");
                        tokenizer.setParameterValue("document",doc);
                        tokenizer.execute();

                        splitter.setParameterValue("document",doc);
                        splitter.setParameterValue("inputASName", "Analysis");
                         splitter.setParameterValue("outputASName", "Analysis");
                        splitter.execute();

                        postag.setParameterValue("document", doc);
                        postag.setParameterValue("inputASName", "Analysis");
                        postag.setParameterValue("outputASName", "Analysis");
                        postag.execute();

                        pw=new PrintWriter(new FileWriter(outLoc+File.separator+dname+File.separator+outName));
                        pw.println(doc.toXml());
                        pw.flush();
                        pw.close();

                    }


                }
            }
            
            
     
            
        } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
           Logger.getLogger(ProcessAutomaticSummaries.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(ProcessAutomaticSummaries.class.getName()).log(Level.SEVERE, null, ex);
       }
        
        
    }
   
    public static void main2(String[] args) {
        
        
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String floc;
        String fname;
        Document doc;
        PrintWriter pw;
        
        ProcessingResource tokenizer;
        ProcessingResource splitter;
        ProcessingResource postag;
        
        String outName;
        
        try {
            Gate.init();
            Gate.getCreoleRegister().registerDirectories(new 
                  URL("file:///home/horacio/work/software/GATE-8.4.1/plugins/ANNIE"));
            tokenizer=(ProcessingResource)
                    Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
            splitter=(ProcessingResource)
                    Factory.createResource("gate.creole.splitter.SentenceSplitter");
            postag=(ProcessingResource)
                    Factory.createResource("gate.creole.POSTagger");
            
            for(File file: flist) {
                
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".summary")){
                    
                    outName=fname.substring(0, 7)+"_"+summ_type+".xml";
                    doc=Factory.newDocument(new URL("file:///"+floc));
                    tokenizer.setParameterValue("document",doc);
                    tokenizer.execute();
                    
                    splitter.setParameterValue("document",doc);
                    splitter.execute();
                    
                    postag.setParameterValue("document", doc);
                    postag.execute();
                    
                    pw=new PrintWriter(new FileWriter(outLoc+File.separator+outName));
                    pw.println(doc.toXml());
                    pw.flush();
                    pw.close();
                    
                }
                
                
            }
            
            
     
            
        } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
           Logger.getLogger(ProcessAutomaticSummaries.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(ProcessAutomaticSummaries.class.getName()).log(Level.SEVERE, null, ex);
       }
        
        
    }
    
}
