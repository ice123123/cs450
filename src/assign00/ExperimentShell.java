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
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;


/**
 *
 * @author Iceman
 */
public class ExperimentShell {

    //static final String file = "lib/iris.csv";
    //static final String file = "lib/carData.csv";
    static final String file = "lib/houseVotes.csv";
    //static final String file = "lib/houseVotesTest.csv";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource(file);
        Instances dataSet = source.getDataSet();
        
        //Set up data
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        dataSet.randomize(new Random());
        
        //determine sizes
        int trainingSize = (int) Math.round(dataSet.numInstances() * .7);
        int testSize = dataSet.numInstances() - trainingSize;
        
        Instances training = new Instances(dataSet, 0, trainingSize);
        
        Instances test = new Instances(dataSet, trainingSize, testSize);
        
        Standardize standardizedData = new Standardize();
        standardizedData.setInputFormat(training);        
        
        Instances newTest = Filter.useFilter(test, standardizedData);
        Instances newTraining = Filter.useFilter(training, standardizedData);
        
        DecisionTreeClassifier ID3 = new DecisionTreeClassifier();
        ID3.buildClassifier(newTraining);
        
        Evaluation eval = new Evaluation(newTraining);
        eval.evaluateModel(ID3, newTest);
        
        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
    }
    
}
