/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import static java.lang.Double.isNaN;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Iceman
 */
public class Tree {
    private Node root;
    
    public Tree(Instances instances){
        List<Instance> instanceList = new ArrayList<>();

        for(int i = 0; i < instances.numInstances(); i++)
            instanceList.add(instances.instance(i));

        boolean [] avaiableAttributeIndexList = new boolean[instances.numAttributes() - 1];

        for(int i = 0; i < avaiableAttributeIndexList.length; i++)
            avaiableAttributeIndexList[i] = true;

        root = new Node(instanceList, avaiableAttributeIndexList, -1);

//        for(int i = 0; i < instances.numInstances(); i++){
//            System.out.println(instances.instance(i).value(1));
//            if(isNaN(instances.instance(i).value(1)))
//                System.out.println("the above is not a number");
//        }
            
        printTree();
    }

    public void printTree(){
        Queue<Node> queue = new LinkedList <>();
        int curLvl = 1;
        int nextLvl = 0;
        
        queue.add(root);
        curLvl = 1;
        
        while(curLvl != 0 || nextLvl != 0){
            Node node = queue.poll();
            curLvl--;
            
            node.print();
            System.out.println(" ");
            
            for(int i = 0; i < node.getChildren().size(); i++) {
                queue.add(node.getChildren().get(i));
                nextLvl++;
            }
            //System.out.println(node.getChildren().size());
            
            if(curLvl == 0) {
                curLvl = nextLvl;
                nextLvl = 0;
                System.out.print("\n");
            } 
        }
    }
    
    public double search(Instance instance){
        return root.search(instance); 
    }
}
