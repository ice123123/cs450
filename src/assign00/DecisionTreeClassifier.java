package assign00;

import Tree.Tree;
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
public class DecisionTreeClassifier extends Classifier {

    private Instances data;
    private Tree tree;
    
    @Override
    public void buildClassifier(Instances i) {
        data = i;
        
        //create tree
        tree = new Tree(data);
        return;
    }
    
    @Override
    public double classifyInstance(Instance instance) {
        return tree.search(instance);
    }
    
}
