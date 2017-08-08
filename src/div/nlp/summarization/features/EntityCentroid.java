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
public class EntityCentroid {
     public static void main(String[] args) {
         String vecName=args[1];
         String feature=args[2];
        String inLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        String outLoc="/home/horacio/work/SciSUM-2017/TEST-DATA-VECS-RPS";
        File inDir=new File(inLoc);
        File[] flist=inDir.listFiles();
        String fname;
        String floc;
        Document doc;
        PrintWriter pw;
        AnnotationSet all;
        AnnotationSet vectors;
        AnnotationSet sentences;
        FeatureMap centroid;
        Set<FeatureMap> fms;
        Long startS, endS;
        FeatureMap sentfm;
        FeatureMap vecfm;
        double cosval;
        AnnotationSet auxsents;
        Annotation sentence;
        
        try {
            Gate.init();
            int count=0;
            
            for(File file : flist) {
              //  if(count>=3) break;
                fname=file.getName();
                System.out.println(fname);
                floc=file.getAbsolutePath();
                doc=Factory.newDocument(new URL("file://"+floc), "UTF-8");
                
                all=doc.getAnnotations("Analysis");
               // vectors=all.get("Vector_Norm");
                vectors=all.get(vecName);
                sentences=all.get("Sentence");
                
                
                
                fms=new HashSet();
                for(Annotation vector : vectors) {
                    fms.add(vector.getFeatures());
                }
            
                centroid=summa.centroid.Centroid.Centroid1(fms);
               
                
                 for(Annotation vector : vectors) {
                    startS=vector.getStartNode().getOffset();
                    endS  =vector.getEndNode().getOffset();
                    vecfm =vector.getFeatures();
                    cosval=summa.scorer.Cosine.cosine1(vecfm,centroid);
                    auxsents=sentences.get(startS,endS);
                    if(auxsents.size()==1) {
                        sentence=auxsents.iterator().next();
                        sentfm=sentence.getFeatures();
                        sentfm.put(feature,cosval+"");

                    }
            }
                
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
