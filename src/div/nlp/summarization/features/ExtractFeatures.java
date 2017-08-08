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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author horacio
 */
public class ExtractFeatures {
    
    
    

    
    
    
    public static void main(String[] args) {
        String gold=args[0];
        PrintWriter pw;
                     
        
        //String inDir="/home/horacio/temp/div_training";
        String inDir="/home/horacio/work/SciSUM-2016/TEST-FEATURES-1-OUT";
        // summarization features
        ArrayList<String> featSets=new ArrayList();
       
        
        /*featSets.add("centroid_sim");
        featSets.add("position_score");
        featSets.add("relInSecBottom");
        featSets.add("relInSecTop");
        featSets.add("relSecBottom");
        featSets.add("relSecTop");
        featSets.add("tf_score");
        featSets.add("title_sim");
        featSets.add("coref_density");
        featSets.add("coref_density_unique");
        featSets.add("abs_sim");
        featSets.add("citRatio");
        featSets.add("in_sec_centroid");
*/
        featSets.add("PROB_DRI_Approach");
        featSets.add("PROB_DRI_Background");
        featSets.add("PROB_DRI_Challenge");
        featSets.add("PROB_DRI_FutureWork");
        featSets.add("PROB_DRI_Outcome");
      //  featSets.add("acl_avg");
   /*     featSets.add("PROB_DRI_Sentence");
        featSets.add("textrank_score_norm");
        featSets.add("tr_sec_score_norm");
         featSets.add("tf_entity");
          featSets.add("entity_cent_sim");
          featSets.add("tr_enti_sec_score_norm");
            featSets.add("norm_cue");
            featSets.add("coref_score");
*/
            
        //featSets.add("abs_sim");
        
        Iterator<String> featIte;
        
        String instance;
        
        File inFiles=new File(inDir);
        File[] flist=inFiles.listFiles();
        Document doc;
        String docLoc;
        String docName;
        AnnotationSet all;
        AnnotationSet sentences;
        FeatureMap fm;
        
        String feat_value;
        
        PrintWriter pw1;
        // print cases and features
       try {
        pw=new PrintWriter(new 
        FileWriter("/home/horacio/work/SciSUM-2016/ARFFs-2017/test.arff"));
        
        pw1=new PrintWriter(new 
        FileWriter("/home/horacio/work/SciSUM-2016/ARFFs-2017/test.cvs"));
        
        pw.println("@relation REL");
        pw.println();
        for(String feat_name : featSets) {
                pw.println("@attribute "+feat_name+" Numeric");
                pw1.print(feat_name+",");
        }
        
        pw1.println("gs");
        pw1.flush();
        

        pw.println("@attribute gs Numeric");
        pw.println();
        pw.println("@data");
       
            Double gs;
            double gs_v;
          
         
            Gate.init();
            for(int f=0;f<flist.length && f<4;f++) {
                docLoc=flist[f].getAbsolutePath();
                docName=flist[f].getName();
                System.out.println(docName);
                if(docName.endsWith(".xml")) {
                   doc=Factory.newDocument(new URL("file:///"+docLoc),"UTF-8");
                   all=doc.getAnnotations("Analysis");
                   sentences=all.get("Sentence");
                   for(Annotation sentence : sentences) {
                       fm=sentence.getFeatures();
                       instance="";
                       for(String feat_name : featSets) {
                           if(fm.containsKey(feat_name)) {
                            feat_value=fm.get(feat_name).toString();
                            instance=instance+feat_value+",";
                           } else {
                               instance=instance+"0.0"+",";
                           }
                       }
                       if(fm.containsKey(gold)) {
                          gs=(new Double((String)fm.get(gold)));
                          gs_v=gs.doubleValue();
                          //if(gs_v>=4) {
                            instance=instance+gs_v;
                         // } else {
                         //     instance=instance+"0.0";
                         // }
                       
                          pw.println(instance);
                          pw.flush();
                          pw1.println(instance);
                          pw1.flush();
                       }
                   }
                  
                   
                   
                    Factory.deleteResource(doc);
                }
              
                   
                }
               pw.close();
               pw1.close();
               
            } catch (GateException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                 ex.printStackTrace();
            } catch (IOException ex) {
            Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            
                
                
            }
            
            
            
            
            
          
        
        
        
    }
    


    
    

