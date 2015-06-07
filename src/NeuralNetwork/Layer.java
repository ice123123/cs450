/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;

/**
 *
 * @author Iceman
 */
public class Layer {
    private List<Neuron> neurons = new ArrayList<>();
    
    Layer(Integer NumOfNodes, double learningRate) {
        
        Neuron neuron = new Neuron(learningRate);
        
        //bios node
        neuron.setValue(-1.0);
        neurons.add(neuron);
        
        //create all other nodes
        for(int i = 0; i < NumOfNodes; i++){
            neuron = new Neuron(learningRate);
            neurons.add(neuron);
            
        }
    }
    
    public void initializeWeights(){
        for(int i = 1; i < neurons.size(); i++)
            neurons.get(i).initializeWeights();
    }
    
    public List<Neuron> getNuerons(){
        return neurons;
    }
    
    public List<Neuron> getNueronsWithoutBios(){
        
        List <Neuron> neuronsWithoutBios = new ArrayList <>();
        
        for(int i = 1; i < neurons.size(); i++) {
            neuronsWithoutBios.add(neurons.get(i));
        }
        
        return neuronsWithoutBios;
    }
    
    public void connectNextLayer(List<Neuron> neurons) {
        for(int i = 0; i < this.neurons.size(); i++)
            this.neurons.get(i).connectNextNodes(neurons);
    }
 
    public void connectPrevLayer(List<Neuron> neurons) {
        for(int i = 1; i < this.neurons.size(); i++)
            this.neurons.get(i).connectPrevNodes(neurons);
    }
    
    public void setFirstLayer(Instance instance){
        for(int i = 1; i < neurons.size(); i++) {
            neurons.get(i).setValue(instance.value(i - 1));
        }
    }
    
    public void forwardPropagate(){
        for(int i = 1; i < neurons.size(); i++){
            neurons.get(i).forwardPropagate();
        }
    }
    
    public void backwardPropagate(){
        for(int i = 1; i < neurons.size(); i++){
            neurons.get(i).backwardPropagate();
        }
    }
    
    public void backwardPropagateOutputLayer(int correctNode){
        for(int i = 1; i < neurons.size(); i++){
            //add 1 to account for the bios node being in slot 0
            if(i == correctNode + 1) {
                //this neuron was correct
                neurons.get(i).backwardPropagateOutputLayer(true);
            } else {
                //the neuron was incorrect
                neurons.get(i).backwardPropagateOutputLayer(false);
            }
        }
    }
    
    public void updateWeights(){
        for(int i = 1; i < neurons.size(); i++)
            neurons.get(i).updateWeights();
    }
    
    public double highestValueNeuron(){
        double highestValue = -Double.MAX_VALUE;
        double highestValueNeuron = -1;
        
        for(int i = 1; i < neurons.size(); i++){
            if(neurons.get(i).getValue() > highestValue) {
                highestValue = neurons.get(i).getValue();
                highestValueNeuron = i;
            }
        }
        
        return highestValueNeuron - 1;
    }
    
    public void print(){
        for(int i = 0; i < neurons.size(); i++){
            neurons.get(i).print();
        }
    }
}
