/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

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

/**
 *
 * @author horacio
 */
public class ComputeFeatures {
    public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/temp/test_div_2";
       
        String inLoc="/home/horacio/temp/test_div_1";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        InSecCentroid inSecCent=new InSecCentroid();
        CitMarkerCount citCounter=new CitMarkerCount();
        try {
            Gate.init();
            
         
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
                        inSecCent.setDocument(doc);
                        inSecCent.setSentenceAnnSet("Analysis");
                        inSecCent.setSentAnn("Sentence");
                        inSecCent.setVecAnn("Vector_Norm");
                        inSecCent.setSecAnn("Section");
                        inSecCent.setInSecCentroid("in_sec_centroid");
                        inSecCent.execute();
                        citCounter.setSentenceAnnSet("Analysis");
                        citCounter.setSentAnn("Sentence");
                        citCounter.setDocument(doc);
                        citCounter.setCitAnn("CitMarker");
                        citCounter.setCitCount("citCount");
                        citCounter.setCitFeat("citRatio");
                        citCounter.execute();
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
     public static void mainBack(String[] args) {
        Document doc;
        PrintWriter pw;
        String outLoc="/home/horacio/temp/test_div";
       
        String inLoc="/home/horacio/work/DrInventor/resources/gold_review_features";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;

        try {
            Gate.init();
            
            MainOrSecondarySection section=new MainOrSecondarySection();
            section.setSentenceAnnSet("Analysis");
            section.setSentAnn("Sentence");
            
            AddFeatureValueToAnnotation add=new AddFeatureValueToAnnotation();
            add.setInAnnSet("Babelnet");
            add.setEntityAnn("Entity");
            add.setFeature("entity");
            add.setValue("yes");
            
            CorefChainScore scorer=new CorefChainScore();
            scorer.setSentenceAnnSet("Analysis");
            scorer.setSentAnn("Sentence");
            scorer.setCorefChainSet("CorefChains");
           
            DocumentStructure ds=new DocumentStructure();
            ds.setSentAnn("Sentence");
            ds.setSentenceAnnSet("Analysis");
            
            for(File file: flist) {
                fname=file.getName();
                floc=file.getAbsolutePath();
                if(fname.endsWith(".xml")) {
                    if(!(new File(outLoc+File.separator+fname).exists()))
                        System.out.println("Computing "+fname+"...");
                        doc=Factory.newDocument(
                            new URL("file:"+floc));
                        section.setDocument(doc);
                        section.execute();
                        add.setDocument(doc);
                        add.execute();
                        scorer.setDocument(doc);
                        scorer.execute();
                        ds.setDocument(doc);
                        ds.execute();
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
