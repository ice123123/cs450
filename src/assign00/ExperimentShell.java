/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign00;

import java.util.Random;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


/**
 *
 * @author Iceman
 */
public class ExperimentShell {

    static String file = "lib/iris.csv";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource(file);
        Instances dataSet = source.getDataSet();
        
        //Set up data
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        dataSet.randomize(new Random(1));
        
        //determine sizes
        int trainingSize = (int) Math.round(dataSet.numInstances() * .7);
        int testSize = dataSet.numInstances() - trainingSize;
        
        Instances training = new Instances(dataSet, 0, trainingSize);
        
        Instances test = new Instances(dataSet, trainingSize, testSize);
        
        HardCodedClassifier hcc = new HardCodedClassifier();
        hcc.buildClassifier(training);
        
        Evaluation eval = new Evaluation(training);
        eval.evaluateModel(hcc, test);
        
        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
    }
    
}
