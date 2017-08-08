/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.GateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author horacio
 */
public class ComputeBabelNetTFScore {
    
    
      public static void main(String[] args) {
        String inLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        String outLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        Document doc;
        PrintWriter pw;
        AnnotationSet all;
        AnnotationSet entities;
        try {
            Gate.init();
            
            
           summa.scorer.SentenceTermFrequency frequency=new  summa.scorer.SentenceTermFrequency();
           frequency.setAnnSetName("Analysis");
           frequency.setSentAnn("Sentence");
           frequency.setTermFreqFeature("token_tf_idf");
           frequency.setWordAnn("Token");
           frequency.setStatFeature("sent_tf_idf");
           frequency.init();
            
           
            
            
            int count=0;
            
            for(File file : flist) {
             //   if(count>=3) break;
                fname=file.getName();
                System.out.println(fname);
                floc=file.getAbsolutePath();
                doc=Factory.newDocument(new URL("file://"+floc), "UTF-8");
                frequency.setDocument(doc);
                frequency.execute();
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
