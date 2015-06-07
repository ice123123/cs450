/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Iceman
 */
public class Network {
    List<Integer> numOfNodesPerLayer = new ArrayList<>();
    double learningRate = .12;
    
    private List<Layer> layers = new ArrayList<>();
    
    public Network(Instances instances){
        //first layer, this is inputs only
        numOfNodesPerLayer.add(instances.numAttributes() - 1);

        //hidden layers
        numOfNodesPerLayer.add(20);
        
        //output layer
        numOfNodesPerLayer.add(instances.numClasses());
        
        //create the layers
        for(int i = 0; i < numOfNodesPerLayer.size(); i++) {
            Layer layer = new Layer(numOfNodesPerLayer.get(i), learningRate);
            layers.add(layer);
        }
        
        //connect the layers going forward
        for(int i = 0; i < layers.size() - 1; i++){
            layers.get(i).connectNextLayer(layers.get(i+1).getNueronsWithoutBios());
        }
        
        //connect the layers going backward
        for(int i = 1; i < layers.size(); i++){
            layers.get(i).connectPrevLayer(layers.get(i-1).getNuerons());
            layers.get(i).initializeWeights();
        }
        
        learnAll(instances);
    }
    
    public void learnAll(Instances instances){
        for(int i = 0; i < instances.numInstances(); i++){
            //System.out.println(i);
            learn(instances.instance(i));
        }        
    }
    
    public void learn(Instance instance){
        //set up the first layer (input layer)
        layers.get(0).setFirstLayer(instance);
        
        //forwward propagate
        forwardPropagate();
        
        //backward propgate starting from the last layer
        layers.get(layers.size() - 1).backwardPropagateOutputLayer((int)instance.classValue());
        
        //backward propgate all other layers (except the first, since its inputs only)
        for(int i = layers.size() - 2; i > 0; i--){
            layers.get(i).backwardPropagate();
        }
        
        //update the weights of all layers (except the first, since its inputs only)
        for(int i = 1; i < layers.size(); i++)
            layers.get(i).updateWeights();
    }
    
    public double classify(Instance instance) {
        //set up the first layer
        layers.get(0).setFirstLayer(instance);
        
        //forward propagate
        forwardPropagate();
        
        //grab the highest value node from the output layer
        return layers.get(layers.size() - 1).highestValueNeuron();
    }
    
    private void forwardPropagate(){
        for(int i = 1; i < layers.size(); i++){
            layers.get(i).forwardPropagate();
        }
    }

   public void print() {
       System.out.println("num of layers : " + layers.size());
       
       for(int i = 0; i < layers.size(); i++) {
           System.out.println("Layer : " + i + "\n");
           layers.get(i).print();
       }
   }
}
