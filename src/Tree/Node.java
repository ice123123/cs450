/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import static java.lang.Double.isNaN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Iceman
 */
public class Node {
    private List<Node> children = new ArrayList<>();
    private Integer attributeIndex;
    private List<Instance> instanceList = new ArrayList<>();
    private boolean [] avaiableAttributeIndexList;

    public Integer getAttributeIndex() {
        return attributeIndex;
    }

    public List<Instance> getInstanceList() {
        return instanceList;
    }
    
    void print(){
        if(attributeIndex.compareTo(0) >= 0) {
            System.out.print(instanceList.get(0).attribute(attributeIndex).name());
        } else {
            System.out.print("root");
        }
    }
    
    public Node(List<Instance> instanceList, boolean [] avaiableAttributeIndexList, Integer attributeIndex) {
        this.instanceList = instanceList;
        this.avaiableAttributeIndexList = avaiableAttributeIndexList;
        this.attributeIndex = attributeIndex;
        
        if(countAvailClasses() > 1) {
            split();
        }
    }
    
    private int countAvailClasses() {
        boolean [] numClasses = new boolean[instanceList.get(0).numClasses()];
        int num = 0;
        
        for(int i = 0; i < instanceList.size(); i++) {
            numClasses[(int)instanceList.get(i).classValue()] = true;
        }
        
        for(int i = 0; i < numClasses.length; i++) {
            if(numClasses[i] == true) {
                num++;
            }
        }
        
        return num;
    }
    
    public List<Node> getChildren(){
        return children;
    }
    
    private void split(){
        //create usefule variables
        List<List<Instance>> splitLists = new ArrayList<List<Instance>>();
        boolean [] newAvaiableAttributeIndexList = new boolean[avaiableAttributeIndexList.length];
        System.arraycopy(avaiableAttributeIndexList, 0, newAvaiableAttributeIndexList, 0, avaiableAttributeIndexList.length);
        Integer newAttributeIndex = -1;
        
        //Find the next 
        newAttributeIndex = determineAtt(avaiableAttributeIndexList);
//        for(int i = 0; i < avaiableAttributeIndexList.length; i ++) {
//            if(avaiableAttributeIndexList[i]) {
//                newAttributeIndex = i;
//            }
//        }
        
        //check if any attributes are left to split on
        if(newAttributeIndex.compareTo(0) >= 0){
            //go through all instances
            for(int i = 0; i < instanceList.size(); i++) {
                //group by attribute values
                int j;
                for(j = 0; j < splitLists.size() && splitLists.get(j).get(0).value(newAttributeIndex) != instanceList.get(i).value(newAttributeIndex); j++) { 
                    //System.out.println(splitLists.get(j).get(0).value(newAttributeIndex) + "!=" + instanceList.get(i).value(newAttributeIndex));
                }
                
                if(!isNaN(instanceList.get(i).value(newAttributeIndex))) {
                    //First time the tree has seen this attribute value
                    if(j == splitLists.size()){
                        splitLists.add(new ArrayList<Instance>());
                        //System.out.println(j);
                    } 

                    //add the instance to the correct list
                    splitLists.get(j).add(instanceList.get(i));
                }
            }

            //remove the attribute off the list
            newAvaiableAttributeIndexList[newAttributeIndex] = false;
            
            //create the new child
            for(int i = 0; i < splitLists.size(); i++){
                children.add(createChild(splitLists.get(i), newAvaiableAttributeIndexList, newAttributeIndex));
            }
        }    
    }
    
    private Node createChild(List<Instance> instanceList, boolean [] avaiableAttributeIndexList, Integer newAttributeIndex){
        Node node = new Node(instanceList, avaiableAttributeIndexList, newAttributeIndex);
        return node;
    }
    
    public double search(Instance instance){
        //check if there are any children
        if(children.isEmpty()){
            int [] answerList = new int[instanceList.get(0).numClasses()];
            for(int i = 0; i < instanceList.size(); i++) {
                answerList[(int)instanceList.get(i).classValue()]++;
            }
            
            //determine 
            int highestIndex = 0;
            int highestValue = 0;
            
            for(int i = 0; i < answerList.length; i++) {
                if(answerList[i] > highestValue){
                    highestValue = answerList[i];
                    highestIndex = i;
                }
            }
           
            return highestIndex;
        }
        
        Node node = null;
            
        //find the correct child
        for(int i = 0; i < children.size(); i++) {
            if(!isNaN(instance.value(children.get(i).getAttributeIndex()))) {
                if(instance.value(children.get(i).getInstanceList().get(0).attribute(children.get(i).getAttributeIndex())) == children.get(i).getInstanceList().get(0).value(children.get(i).getInstanceList().get(0).attribute(children.get(i).getAttributeIndex()))) {
                    node = children.get(i);
                }
            }
        }
        
        //if the branch doesnt exist, find the branch with the highest ratio
        if(node == null) {
            int highestValue = 0;
            int highestChild = 0;
            
            for(int i = 0; i < children.size(); i++) {
                if(children.size() > highestValue ) {
                    highestValue = children.size();
                    highestChild = i;
                }
            }
            
            node = children.get(highestChild);
        }
        
        return node.search(instance);
    }
    
    private int determineAtt(boolean [] avaiableAttributeIndexList){
        int lowestIndex = -1;
        double lowestValue = Double.MAX_VALUE;
        
        
        for(int i = 0; i < avaiableAttributeIndexList.length; i++) {
            //System.out.println(calculateEntropy(i));
            if (avaiableAttributeIndexList[i] == true) {
               if(lowestValue > calculateEntropy(i)) {
                   lowestValue = calculateEntropy(i);
                   lowestIndex = i;
               }
            } 
        }
        //System.out.println(lowestIndex);
        //System.out.println(instanceList.get(0).attribute(lowestIndex));
        return lowestIndex;
    }
    
    private double calculateEntropy(int attributeIndex) {
        Map <Double, Integer> map = new HashMap<>();
        
        for(int i = 0; i < instanceList.size(); i++) {
            if (map.containsKey(instanceList.get(i).value(attributeIndex))) {
                Integer value = map.get(instanceList.get(i).value(attributeIndex));
                //System.out.println(value);
                value += 1;
                
                map.put(instanceList.get(i).value(attributeIndex), value);
            } else {
                map.put(instanceList.get(i).value(attributeIndex), 1);
            }
        }
        
        double entropy = 0;
        
        for(Double key: map.keySet()) {
            entropy += (double) -map.get(key) / (double) instanceList.size() * (Math.log10((double)map.get(key) / (double)instanceList.size()) / Math.log10(2.0));
        }
        
        //System.out.println(entropy);
        return entropy;
    }
}
