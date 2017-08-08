/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.features;

import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.GateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class ComputeBabelStatistics {
    
    
      public static void main(String[] args) {
        String inLoc="/home/horacio/temp/test_div_6";
        String outLoc="/home/horacio/temp/test_div_7";
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
            
            
            summa.resources.frequency.InvertedTable table=new summa.resources.frequency.InvertedTable();
            summa.resources.frequency.VectorComputation vectors=
                    new summa.resources.frequency.VectorComputation();
            summa.analyser.NormalizeVector normalize=new summa.analyser.NormalizeVector();
            
            table.setEncoding("UTF-8");
            table.setTableLocation(new URL("file:///home/horacio/temp/babelnet.idf"));
            table.init();
            
            summa.resources.frequency.NEFrequency statistics=
                    new summa.resources.frequency.NEFrequency();
            statistics.setAnnSet("Analysis");
            statistics.setAnnType("Entity");
            statistics.setFeatureName("synsetID");
            statistics.setKindF("entity");
            statistics.setKindV("yes");
            statistics.setSentAnn("Sentence");
            statistics.setSentStat("enti_sent");
            statistics.setParaStat("enti_para");
            statistics.setTokenStat("enti_token");
            statistics.setTable(table);
            statistics.init();
            
            
            vectors.setAnnSetName("Analysis");
            vectors.setEncoding("UTF-8");
            vectors.setLowercase(Boolean.FALSE);
            vectors.setSentAnn("Sentence");
            vectors.setStatistics("enti_token_tf_idf");
            vectors.setStopFeature("synsetID");
            vectors.setStopTag("synsetID");
            vectors.setTokenAnn("Entity");
            vectors.setTokenFeature("synsetID");
            vectors.setVecAnn("BabelVec");
            vectors.setStopTagLoc(new 
                URL("file:///home/horacio/work/programs/gate-8.0/plugins/summa_plugin/resources/stop_kind.lst"));
            vectors.setStopWordLoc(new
                URL("file:///home/horacio/work/programs/gate-8.0/plugins/summa_plugin/resources/stop_word.lst"));
           
            vectors.init();
            
            normalize.setAnnSet("Analysis");
            normalize.setVecAnn("BabelVec");
            normalize.init();
            
            
            int count=0;
            
            for(File file : flist) {
               // if(count>=3) break;
                fname=file.getName();
                floc=file.getAbsolutePath();
                doc=Factory.newDocument(new URL("file://"+floc), "UTF-8");
                all=doc.getAnnotations("Analysis");
                entities=doc.getAnnotations("Babelnet");
                all.addAll(entities);
                statistics.setDocument(doc);
                statistics.execute();
                vectors.setDocument(doc);
                vectors.execute();
                
                normalize.setDocument(doc);
                normalize.execute();
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
