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
import gate.util.GateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author horacio
 */
public class ComputeCuePhraseScore {
    
    public static void main(String[] args) {
      //  String inLoc="/home/horacio/temp/test_div_10";
      //  String outLoc="/home/horacio/temp/test_div_11";
    //    String inLoc="/home/horacio/temp/SciSUM-2016/test-last/gold_scores_features";
    //    String outLoc="/home/horacio/temp/SciSUM-2016/test-last/gold_scores_features";
        String inLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        String outLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        Document doc;
        PrintWriter pw;
       
        String workingDir = System.getProperty("user.dir");
	  
        try {
            Gate.init();
            Gate.getCreoleRegister().registerDirectories(new 
        URL("file:///home/horacio/work/software/GATE-8.4.1/plugins/summa_plugin"));
             Gate.getCreoleRegister().registerDirectories(new 
        URL("file:///home/horacio/work/software/GATE-8.4.1/plugins/ANNIE"));
            int count=0;
            FeatureMap cuefm=Factory.newFeatureMap();
            cuefm.put("listLocation",new 
        URL("file:///home/horacio/work/software/DIVFeatureComputation/resources/cue_words.lst"));
            ProcessingResource cue=(ProcessingResource)
                    Factory.createResource("summa.scorer.CuePhraseScorer", cuefm);
            cue.init();
            cue.setParameterValue("annSet", "Analysis");
            //cue.setParameterValue("annSet", "Analysis");
       
            for(File file : flist) {
              //  if(count>=3) break;
                fname=file.getName();
                System.out.println(fname);
                floc=file.getAbsolutePath();
                doc=Factory.newDocument(new URL("file://"+floc), "UTF-8");
                cue.setParameterValue("document", doc);
                cue.execute();
               
                
                pw=new PrintWriter(outLoc+File.separator+fname);
                pw.println(doc.toXml());
                pw.flush();
                pw.close();
                Factory.deleteResource(doc);
                count++;
                
                
            }
            
            
        
          
            
            
        } catch(GateException ge) {
            
            ge.printStackTrace();
        } catch (MalformedURLException murle) {
           murle.printStackTrace();
        } catch (FileNotFoundException ex) {
              ex.printStackTrace();
          }
        
        
    }
    
    
    
}
