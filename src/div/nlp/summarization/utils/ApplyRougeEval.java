/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.utils;

import gate.Corpus;
import gate.Document;
import gate.Gate;
import gate.util.GateException;
import gate.Factory;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class ApplyRougeEval {
    
    //public static String goldLoc="/home/horacio/work/data/dr_inventor_docs/NEW_GOLD_SUMMARIES_XML";
   // public static String autoLoc="/home/horacio/work/data/dr_inventor_docs/TEXTRANK_SUMMARIES_ALL_XML";
   // public static int ngram=2;
    //  public static String goldLoc="/home/horacio/temp/SciSUM-2016/test/gold_xml";
   // public static String autoLoc="/home/horacio/temp/SciSUM-2016/test/textrank_xml";
    
    /*
     public static String goldLoc="/home/horacio/temp/SciSUM-2016/test/Summaries-Test-Set-2016/human_xml";
    public static String autoLoc="/home/horacio/temp/SciSUM-2016/test/automatic_xml";
    
    
    public static int ngram=1;
    */
  /*
    public static String goldLoc="/home/horacio/work/data/dr_inventor_docs/NEW_GOLD_SUMMARIES_XML";
    public static String autoLoc="/home/horacio/work/data/dr_inventor_docs/DIV_SUMMARIES_ALL_2_XML";
*/
    public static String goldLoc="/home/horacio/work/SciSUM-2016/EVALUATE-TEST/GOLD-SUMMARIES";
    public static String autoLoc="/home/horacio/work/SciSUM-2016/EVALUATE-TEST/SUMMARIES-XML";
    public static int ngram=1;
    public static String goldType="human";
    
    public static void main(String[] args) {
        
        File goldDir=new File(goldLoc);
        File[] goldf=goldDir.listFiles();
        File autoDir=new File(autoLoc);
        File[] autod=autoDir.listFiles();
        File autoSubDir;
        File[] autof;
        
        Document automatic;
        Document goldDoc;
        Corpus ideal;
         
        
        summa.evaluation.rouge.RougeEval eval=new  summa.evaluation.rouge.RougeEval();
        summa.ngrams.ComputeNgrams ngrams=new summa.ngrams.ComputeNgrams();
        
        String autoName;
        String autoFileLoc;
        
        String goldName;
        String goldFileLoc;
        
        String key;
        
        try {
            Gate.init();
            
            ngrams.setAnnType("Token");
            ngrams.setFeature("string");
            ngrams.setInputAS("Analysis");
            ngrams.setOutputAS("Ngrams");
            ngrams.setNormalise(Boolean.TRUE);
            ngrams.setSentAnn("Sentence");
            ngrams.setNgram(new Integer(ngram));
            
            eval.setIsExtract(Boolean.FALSE);
            eval.setNgram(new Integer(ngram));
            eval.setSentAnn("Sentence");
            eval.setExtractSet("Analysis");
            
            String dname;
            String dloc;
            File dfile;
            
            for(File dir : autod) {
                dname=dir.getName();
                dloc=dir.getAbsolutePath();
                System.out.println("DIR: "+dname+"...");
                autoSubDir=new File(dloc);
                autof=autoSubDir.listFiles();
                for(File file : autof) {
                    autoName=file.getName();
                    autoFileLoc=file.getAbsolutePath();

                    key=autoName.substring(0, 7);


                    // automatic summary

                    automatic=Factory.newDocument(new URL("file:///"+autoFileLoc));

                    ngrams.setDocument(automatic);
                    ngrams.execute();


                    // gold summaries

                    ideal=Factory.newCorpus("");

                    for(File gfile : goldf) {
                        goldName=gfile.getName();
                        goldFileLoc=gfile.getAbsolutePath();

                        if(goldName.startsWith(key) && goldName.contains(goldType)) {

                            goldDoc=Factory.newDocument(new URL("file:///"+goldFileLoc));
                            ngrams.setDocument(goldDoc);
                            ngrams.execute();
                            ideal.add(goldDoc);

                        }


                    }

                    eval.setIdealCorpus(ideal);
                    eval.setDocument(automatic);

                    System.out.println("KEY = "+key);

                    eval.execute();
                    
                    Factory.deleteResource(ideal);
                    Factory.deleteResource(automatic);

                }
            
            }
            
            
        } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApplyRougeEval.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ApplyRougeEval.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    public static void main2(String[] args) {
        
        File goldDir=new File(goldLoc);
        File[] goldf=goldDir.listFiles();
        File autoDir=new File(autoLoc);
        File[] autof=autoDir.listFiles();
        
        Document automatic;
        Document goldDoc;
        Corpus ideal;
         
        
        summa.evaluation.rouge.RougeEval eval=new  summa.evaluation.rouge.RougeEval();
        summa.ngrams.ComputeNgrams ngrams=new summa.ngrams.ComputeNgrams();
        
        String autoName;
        String autoFileLoc;
        
        String goldName;
        String goldFileLoc;
        
        String key;
        
        try {
            Gate.init();
            
            ngrams.setAnnType("Token");
            ngrams.setFeature("string");
            ngrams.setInputAS("");
            ngrams.setOutputAS("Ngrams");
            ngrams.setNormalise(Boolean.TRUE);
            ngrams.setSentAnn("Sentence");
            ngrams.setNgram(new Integer(ngram));
            
            eval.setIsExtract(Boolean.FALSE);
            eval.setNgram(new Integer(ngram));
            eval.setSentAnn("Sentence");
            eval.setExtractSet("");
            
            for(File file : autof) {
                autoName=file.getName();
                autoFileLoc=file.getAbsolutePath();
                
                key=autoName.substring(0, 7);
                
                
                // automatic summary
                
                automatic=Factory.newDocument(new URL("file:///"+autoFileLoc));
                
                ngrams.setDocument(automatic);
                ngrams.execute();
                
                
                // gold summaries
                
                ideal=Factory.newCorpus("");
                
                for(File gfile : goldf) {
                    goldName=gfile.getName();
                    goldFileLoc=gfile.getAbsolutePath();
                    
                    if(goldName.startsWith(key)) {
                        
                        goldDoc=Factory.newDocument(new URL("file:///"+goldFileLoc));
                        ngrams.setDocument(goldDoc);
                        ngrams.execute();
                        ideal.add(goldDoc);
                        
                    }
                           
                    
                }
                
                eval.setIdealCorpus(ideal);
                eval.setDocument(automatic);
                
                System.out.println("KEY = "+key);
                
                eval.execute();
                
                
            
                
                
                
                
            }
            
            
        } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApplyRougeEval.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ApplyRougeEval.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
}
