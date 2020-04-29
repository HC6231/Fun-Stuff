package twoThreeTree;
import com.sun.deploy.security.BadCertificateDialog;

import javax.naming.InsufficientResourcesException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

class Node {
    String guide;
    int value = 0;
    // guide access value of a node
    // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
    Node child0, child1, child2;
}

class LeafNode extends Node {

}

class TwoThreeTree {
    Node root;
    int height;
    TwoThreeTree() {
        root = null;
        height = -1;
    }
}

class WorkSpace {
    // this class is used to hold return values for the recursive doInsrt (Store as temp)
    // routine (see below)
    Node newNode;
    int offset;
    boolean guideChanged;
    Node[] scratch;
}

public class LazyUpdate {
    public static void main(String[] args) throws Exception{
        TwoThreeTree empty = new TwoThreeTree();
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
//        BufferedWriter output2 = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
//        twothree.insert("A",5, empty);
//        twothree.insert("B",20,empty);
//        twothree.insert("C",90,empty);
//        twothree.insert("D",1,empty);
//        twothree.insert("E",10,empty);
//        twothree.insert("F",40,empty);
//        twothree.insert("G",50,empty);
//        twothree.insert("H",60,empty);

        Scanner read = new Scanner(System.in);
//        read in the number of nodes and store
        int numbond = Integer.parseInt(read.nextLine());
        for(int i = 0; i < numbond; i++) {
            String myString = read.nextLine();
            String[] splited = myString.split(" ");
            if (Integer.parseInt(splited[0]) == 1){
                twothree.insert(splited[1], Integer.parseInt(splited[2]), empty);
            }
            else if(Integer.parseInt(splited[0]) == 2){
                if(splited[1].compareTo(splited[2]) <= 0) {
                    twothree.addRange(empty.root, splited[1], splited[2], Integer.parseInt(splited[3]), empty.height);
                }
                else{
                    twothree.addRange(empty.root, splited[2], splited[1], Integer.parseInt(splited[3]), empty.height);
                }
            }
            else if(Integer.parseInt(splited[0]) == 3){
                int seachtemp = 0;
                twothree.search(empty.root,splited[1],empty.height,seachtemp,output);
            }
        }

        output.flush();
        read.close();
    }
}

class twothree {
    static public void insert(String key, int value, TwoThreeTree tree) {
        // insert a key value pair into tree (overwrite existsing value
        // if key is already present)
        int h = tree.height;

        if (h == -1) {  //insert node if 23tree is empty
            LeafNode newLeaf = new LeafNode();
            newLeaf.guide = key;
            newLeaf.value = value;
            tree.root = newLeaf;
            tree.height = 0;
        }
        else {
            WorkSpace ws = doInsert(key, value, tree.root, h);

            if (ws != null && ws.newNode != null) {
                // create a new root

                InternalNode newRoot = new InternalNode();

                if (ws.offset == 0) {
                    newRoot.child0 = ws.newNode;
                    newRoot.child1 = tree.root;
                }

                else {
                    newRoot.child0 = tree.root;
                    newRoot.child1 = ws.newNode;
                }
                resetGuide(newRoot);
                tree.root = newRoot;
                tree.height = h+1;
            }
        }
    }

    static public void addAll(Node p,int h, int va){
        //casting issue here
        if(h == 0){
            ((LeafNode)p).value += va;
        }
        else{
            p.value += va;
        }
    }

    static public void addGE(Node p, int va, String x, int h){
        if(h == 0){
            if (p.guide.compareTo(x) >= 0) {
                ((LeafNode) p).value += va; //access the value and increment with va
//                output.write(p.guide + " " + ((LeafNode) p).value);
//                output.write("\n");
            }
        }

        else if(((InternalNode)p).child0.guide.compareTo(x) >= 0 ){
            addGE(((InternalNode)p).child0, va, x,h-1);
            addAll(((InternalNode)p).child1,h - 1,va);
            if(((InternalNode)p).child2 != null){
                addAll(((InternalNode)p).child2,h - 1,va);
            }
        }

        else if(((InternalNode)p).child2 == null || ((InternalNode)p).child1.guide.compareTo(x) >= 0){
            addGE(((InternalNode)p).child1,va, x, h-1);
            if(((InternalNode)p).child2 != null)
                addAll(((InternalNode)p).child2,h - 1, va);
        }

        else addGE(((InternalNode)p).child2,va,x,h-1);
    }

    static public void addLE (Node p, int va, String x, int h){
        if(h == 0) {
            if (p.guide.compareTo(x) <= 0) {
                ((LeafNode) p).value += va; //access the value and increment with va
//                output.write(p.guide + " " + ((LeafNode) p).value);
//                output.write("\n");
            }
        }
        else if (x.compareTo(((InternalNode)p).child0.guide) <= 0) { //only has one child0
            addLE(((InternalNode)p).child0, va, x,h-1);
        }

        else if(((InternalNode)p).child2 == null || x.compareTo(((InternalNode)p).child1.guide) <= 0) { //has child0 and child1
            addAll(((InternalNode)p).child0,h - 1,va);
            addLE(((InternalNode)p).child1, va, x,h-1);
        }
        else { //has three child0 child1 and child2
            addAll(((InternalNode)p).child0,h - 1,va);
            addAll(((InternalNode)p).child1,h - 1,va);
            addLE(((InternalNode)p).child2, va, x, h-1);
        }
    }

    static void search(Node p, String x, int h, int temp, BufferedWriter output) throws IOException {
        if(h == 0){
            if(x.equals(((LeafNode)p).guide)){
                output.write(((LeafNode)p).value + temp + "\n");
            }
            else{
                output.write("-1" + "\n");
            }
        }
        else if(x.compareTo(((InternalNode)p).child0.guide) <= 0){
            search(((InternalNode)p).child0,x,h-1,temp + p.value, output);
        }

        else if(((InternalNode)p).child2 == null || x.compareTo(((InternalNode)p).child1.guide) <= 0){
            search(((InternalNode)p).child1,x,h-1,temp + p.value, output);
        }
        else{
            search(((InternalNode)p).child2,x,h-1,temp + p.value, output);
        }
    }

    static public void addRange(Node p, String x, String y, int va, int h){
        if(h == 0){
            if(p.guide.compareTo(x) >= 0 && p.guide.compareTo(y) <= 0){
                ((LeafNode)p).value += va; //access the value and increment with va
//                output.write(p.guide + " " + ((LeafNode) p).value);
//                output.write("\n");
            }
        }
        else if(y.compareTo(((InternalNode)p).child0.guide) <= 0){
            addRange(((InternalNode)p).child0,x,y, va,h-1);
        }

        else if(((InternalNode)p).child2 == null || y.compareTo(((InternalNode)p).child1.guide) <= 0 ){
            if(x.compareTo(((InternalNode)p).child0.guide) <= 0){
                addGE(((InternalNode)p).child0, va, x,h-1);
                addLE(((InternalNode)p).child1, va, y,h-1);
            }
            else{
                addRange(((InternalNode)p).child1,x, y, va,h-1);
            }
        }

        else{
            if(x.compareTo(((InternalNode)p).child0.guide) <= 0){
                addGE(((InternalNode)p).child0, va, x,h-1);
                addAll(((InternalNode)p).child1,h - 1,va);
                addLE(((InternalNode)p).child2,va,y,h-1);
            }
            else if(x.compareTo(((InternalNode)p).child1.guide) <= 0){
                addGE(((InternalNode)p).child1, va, x,h-1);
                addLE(((InternalNode)p).child2, va, y,h-1);
            }
            else{
                addRange(((InternalNode)p).child2,x,y,va,h-1);
            }
        }
    }

    static WorkSpace doInsert(String key, int value, Node p, int h) {
        // auxiliary recursive routine for insert
        if (h == 0) {
            // we're at the leaf level, so compare and
            // either update value or insert new leaf

            LeafNode leaf = (LeafNode) p;  //downcast, temporally store the value of p into the leaf value
            int cmp = key.compareTo(leaf.guide);

            if (cmp == 0) {
                leaf.value = value;
                return null;
            }

            // create new leaf node and insert into tree
            LeafNode newLeaf = new LeafNode();
            newLeaf.guide = key;          //store the key and value into the leaf node
            newLeaf.value = value;

            int offset = (cmp < 0) ? 0 : 1;
            // offset == 0 => newLeaf inserted as left sibling
            // offset == 1 => newLeaf inserted as right sibling

            WorkSpace ws = new WorkSpace();
            ws.newNode = newLeaf;
            ws.offset = offset;          // offset value is here to determine left or right
            ws.scratch = new Node[4];

            return ws;
        } //the next insert is added onto the leaf

        else {
            InternalNode q = (InternalNode) p; // cast leaf_node to Internal_node
            int tempvar = p.value;             // store the value in the leaf_node
            q.child0.value += tempvar;
            q.child1.value += tempvar;         ///////

            if(q.child2 != null){              // add temp to children
                q.child2.value += tempvar;     ///////
            }
            p.value = 0;                       // set the current value to 0
            int pos;
            WorkSpace ws;
            // after the insertion, the sum of the value along the path is stored within its child

            if (key.compareTo(q.child0.guide) <= 0) {
                pos = 0;
                ws = doInsert(key, value, q.child0, h-1);
            }
            else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
                pos = 1;
                ws = doInsert(key, value, q.child1, h-1);
            }
            else {
                pos = 2;
                ws = doInsert(key, value, q.child2, h-1);
            }

            if (ws != null) {
                if (ws.newNode != null) {
                    // make ws.newNode child # pos + ws.offset of q

                    int sz = copyOutChildren(q, ws.scratch);
                    insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
                    if (sz == 2) {
                        ws.newNode = null;
                        ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
                    }
                    else {
                        ws.newNode = new InternalNode();
                        ws.offset = 1;
                        resetChildren(q, ws.scratch, 0, 2);
                        resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
                    }
                }
                else if (ws.guideChanged) {
                    ws.guideChanged = resetGuide(q);
                }
            }
            return ws;
        }
    }

    static int copyOutChildren(InternalNode q, Node[] x) {
        // copy children of q into x, and return # of children
        int sz = 2;
        x[0] = q.child0; x[1] = q.child1;
        if (q.child2 != null) {
            x[2] = q.child2;
            sz = 3;
        }
        return sz;
    }

    static void insertNode(Node[] x, Node p, int sz, int pos) {
        // insert p in x[0..sz) at position pos,
        // moving existing extries to the right

        for (int i = sz; i > pos; i--)
            x[i] = x[i-1];

        x[pos] = p;
    }

    static boolean resetGuide(InternalNode q) {
        // reset q.guide, and return true if it changes.

        String oldGuide = q.guide;
        if (q.child2 != null)
            q.guide = q.child2.guide;
        else
            q.guide = q.child1.guide;

        return q.guide != oldGuide;
    }


    static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
        q.child0 = x[pos];
        q.child1 = x[pos+1];
        if (sz == 3)
            q.child2 = x[pos+2];
        else
            q.child2 = null;
        return resetGuide(q);
    }
}




