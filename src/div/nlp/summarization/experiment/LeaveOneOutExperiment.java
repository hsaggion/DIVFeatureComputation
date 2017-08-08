/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package div.nlp.summarization.experiment;

import div.nlp.summarization.features.ExtractFeatures;
import div.nlp.summarization.utils.GoldSummarySize;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;
/**
 *
 * @author horacio
 */
public class LeaveOneOutExperiment {
    
    
    public LeaveOneOutExperiment() {
        
    }
    
    
    String outLoc;
    public String getOutLoc() {
        return outLoc;
    }
    public void setOutLoc(String ol) {
        outLoc=ol;
    }
    
    Corpus corpus;
    public Corpus getCorpus() {
        return corpus;
    }
    
    public void loadCorpus(String inDirLoc) {
        try {
            File inDir=new File(inDirLoc);
            File[] files=inDir.listFiles();
            String fname;
            String floc;
            Document doc;
            corpus=Factory.newCorpus("");
            for(int f=0;f<files.length;f++) {
                floc=files[f].getAbsolutePath();
                fname=files[f].getName();
                if(fname.endsWith(".xml")) {
                    System.out.println(fname+"...");
                    try {
                        doc=Factory.newDocument(new URL("file://"+floc));
                        corpus.add(doc);
                    } catch (ResourceInstantiationException ex) {
                        ex.printStackTrace();
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                    
                }
                
            }
        } catch (ResourceInstantiationException ex) {            
            ex.printStackTrace();
        }
        
    }
    
    String arffLoc;
    public String getArffLoc() {
        return arffLoc;
    }
    public void setArffLoc(String an) {
        arffLoc=an;
    }
    
    
    public void writeLRModel(String outName) {
        PrintWriter pw;
        try {
            pw=new PrintWriter(new 
            FileWriter(outLoc+File.separator+outName+".model"));
            for(int f=0;f<features.length;f++) {
                
                pw.println(features[f]+"\t"+weights[f]);
                pw.flush();
                
            }
            pw.close();
         } catch(IOException ioe) {
             ioe.printStackTrace();
         }

        
    }
    
    String[] features;
    Double[] weights;
    
    public void trainLRModel(String outName) {
         
        try {
            Instances mydata=DataSource.read(outLoc+File.separator+outName+".arff");
            LinearRegression lr=new LinearRegression();
            String[] options = new String[1];
            options[0]="";
            lr.setOptions(options);
            mydata.setClassIndex(mydata.numAttributes()-1);
            
            lr.buildClassifier(mydata);
            double[] coheficients=lr.coefficients();
            weights=new Double[coheficients.length-2];
            features=new String[coheficients.length-2];
            // all minus class and independent constant
            for(int c=0;c<coheficients.length-2;c++) {
                weights[c]=new Double(coheficients[c]);
                features[c]=mydata.attribute(c).name();
            }
            Evaluation evaluate=new Evaluation(mydata);
          
            evaluate.evaluateModel(lr, mydata);
            System.out.println(evaluate.toSummaryString());

         
            
        } catch (Exception ex) {
            Logger.getLogger(LeaveOneOutExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        
    
    
    public static void test(String[] args) {
        
        
        try {
            Instances mydata=DataSource.read("/home/horacio/temp/div_features_gs_11.arff");
            LinearRegression lr=new LinearRegression();
            String[] options = new String[1];
            options[0]="";
            lr.setOptions(options);
            mydata.setClassIndex(mydata.numAttributes()-1);
            
            lr.buildClassifier(mydata);
            double[] coheficients=lr.coefficients();
            
            for(int c=0;c<coheficients.length-1;c++) {
                System.out.print(coheficients[c]+"\t");
                System.out.println(mydata.attribute(c).name());
            }
            Evaluation evaluate=new Evaluation(mydata);
          
            evaluate.evaluateModel(lr, mydata);
            System.out.println(evaluate.toSummaryString());

         
            
        } catch (Exception ex) {
            Logger.getLogger(LeaveOneOutExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void doExperiment(int stop) {
        System.out.println("# docs "+corpus.size());
        Corpus training;
        Document[] docList=new Document[corpus.size()];
      
        Document doc;
        Document testDoc;
        String testName;
        for(int d=0;d<docList.length;d++) {
            docList[d]=corpus.get(d);
        }
        if(stop==0) stop=docList.length;
        for(int d1=0;d1<docList.length && d1<stop;d1++) {
            try {
                testDoc=docList[d1];
                training=Factory.newCorpus("");
                for(int d2=0;d2<docList.length;d2++) {
                    if(d2!=d1) {
                        training.add(docList[d2]);
                    }
                    
                }
                
                // testDoc name
                testName=testDoc.getName();
                testName=testName.substring(0, 7);
                generateArff(training,testName);
                
                trainLRModel(testName);
                writeLRModel(testName);
                
                
                
            } catch (ResourceInstantiationException ex) {
                ex.printStackTrace();
            }
            
        }
        
        
    }
     
    public void generateArff(Corpus train,String oneOutName) {
        PrintWriter pw;
       
        // summarization features
        ArrayList<String> featSets=new ArrayList();
       
        
        featSets.add("centroid_sim");
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
        featSets.add("PROB_DRI_Approach");
        featSets.add("PROB_DRI_Background");
        featSets.add("PROB_DRI_Challenge");
        featSets.add("PROB_DRI_FutureWork");
        featSets.add("PROB_DRI_Outcome");
  //      featSets.add("PROB_DRI_Sentence");
        featSets.add("textrank_score_norm");
        featSets.add("tr_sec_score_norm");
        featSets.add("tf_entity");
        featSets.add("entity_cent_sim");
        featSets.add("tr_enti_sec_score_norm");
        featSets.add("norm_cue");
        featSets.add("coref_score");
            
        
        
        Iterator<String> featIte;
        
        String instance;
     
        Document doc;
        String docLoc;
        String docName;
        AnnotationSet all;
        AnnotationSet sentences;
        AnnotationSet sentences_LOA;
        FeatureMap fm;
        
        String feat_value;
        
        PrintWriter pw1;
        // print cases and features
         try {
            pw=new PrintWriter(new 
            FileWriter(outLoc+File.separator+oneOutName+".arff"));

            pw1=new PrintWriter(new 
            FileWriter(outLoc+File.separator+oneOutName+".cvs"));

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
            Iterator<Document> corpusIte=train.iterator();
            Long startS, endS;
            AnnotationSet auxSents;
            Annotation sentence_LOA;
            FeatureMap fm_LOA;
            while(corpusIte.hasNext()) {

                   doc=corpusIte.next();
                   all=doc.getAnnotations("Analysis");
             
                   sentences=all.get("Sentence");
            
                   for(Annotation sentence : sentences) {
                       startS=sentence.getStartNode().getOffset();
                       endS   =sentence.getEndNode().getOffset();
           
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
                       if(fm.containsKey("gs")) {
                          gs=(Double)fm.get("gs");
                          gs_v=gs.doubleValue();
                          instance=instance+gs_v;
                          pw.println(instance);
                          pw.flush();
                          pw1.println(instance);
                          pw1.flush();
                       }
                   }



                   
                }


                    
               pw.close();
               pw1.close();

        
            } catch (IOException ex) {
            Logger.getLogger(ExtractFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            
                
                
       }

        
    //public static String corpusLoc="/home/horacio/temp/div_all";
    public static String 
            corpusLoc="/home/horacio/work/data/dr_inventor_docs/DIV_WITH_AUTO_CLASSES_2";
   // public static String outDir="/home/horacio/temp/output_experiments";
      public static String outDir="/home/horacio/work/data/dr_inventor_docs/output_models_classes";
    public static void main(String[] args) {
        
        try {
            
            Gate.init();
            LeaveOneOutExperiment experiment=new LeaveOneOutExperiment();
            experiment.loadCorpus(corpusLoc);
            experiment.setOutLoc(outDir);
            experiment.doExperiment(0);
            
            
            
        } catch(GateException ge) {
            ge.printStackTrace();
        }
        
        
    }
    
    
   
    
    
    
    
}
