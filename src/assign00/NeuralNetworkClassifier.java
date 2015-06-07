package assign00;

import NeuralNetwork.Network;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Iceman
 */
public class NeuralNetworkClassifier extends Classifier {
    Network network;

    @Override
    public void buildClassifier(Instances instances) {
        network = new Network(instances);
        //System.out.println(instances.numClasses());
        //network.print();
        for(int i = 0; i < 2000; i++) {
            network.learnAll(instances);
            }
        
        return;
    }
    
    @Override
    public double classifyInstance(Instance instance) {
        //System.out.println(network.classify(instance));
        return network.classify(instance);
    }
    
}