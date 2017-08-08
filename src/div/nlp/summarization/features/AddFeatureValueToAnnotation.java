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
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.util.GateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author horacio
 */
public class AddFeatureValueToAnnotation extends AbstractLanguageAnalyser  implements
ProcessingResource, Serializable {
    
    // the document to process
    Document document;
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document d) {
        document=d;
    }
    // the input annotation set
    String inAnnSet;
    public String getInAnnSet() {
        return inAnnSet;
    }
    public void setInAnnSet(String as) {
        inAnnSet=as;
    }
    
    // the entity, token, etc. 
    
    String entityAnn;
    public String getEntityAnn() {
        return entityAnn;
    }
    public void setEntityAnn(String sa) {
        entityAnn=sa;
    }
    
    
    // feature
    
    public String feature;
    public String getFeature(){
        return feature;
    }
    
    public void setFeature(String f) {
        feature=f;
    } 
    
    public String value;
    public String getValue() {
        return value;
    }
    public void setValue(String v) {
        value=v;
    }
    
    public Resource init() {
        return this;
    }
    
    public void execute() {
        
        Document doc=getDocument();
        
        AnnotationSet all;
        
        if(inAnnSet.equals("")) {
            all=doc.getAnnotations();
        } else {
            all=doc.getAnnotations(inAnnSet);
        }
        
        AnnotationSet entities=all.get(entityAnn);
        
        FeatureMap fm;
        
        for(Annotation entity : entities) {
            fm=entity.getFeatures();
            fm.put(feature, value);
            
        }
        
        
    }
    
    
     public static void main(String[] args) {
        Document doc;
        PrintWriter pw;
        String outDir="/home/horacio/temp";
        try {
            Gate.init();
            AddFeatureValueToAnnotation add=new AddFeatureValueToAnnotation();
            doc=Factory.newDocument(
                    new URL("file:/home/horacio/work/DrInventor/resources/gold_review_features/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning_FINAL_1_GATE.xml"));           
            add.setDocument(doc);
            add.setInAnnSet("Babelnet");
            add.setEntityAnn("Entity");
            add.setFeature("entity");
            add.setValue("yes");
            
            add.execute();
            pw=new PrintWriter(new FileWriter(outDir+File.separator+"div_1.xml"));
            pw.println(doc.toXml());
            pw.flush();
            pw.close();
         } catch(GateException ge) {
            ge.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
    }
    
}
