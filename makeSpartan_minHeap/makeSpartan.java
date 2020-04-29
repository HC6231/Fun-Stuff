import java.io.*;
import java.util.*;

public class makeSpartan {
    public static void main(String[] args) throws Exception {
        // The array based min heap starts from 1
        HashMap<String, Node> map = new HashMap<>();
        Scanner scan = new Scanner(System.in);
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);

        long bound = scan.nextInt();// First Line Read Here
        minHeap heap = new minHeap(bound + 1);
        String name;
        int count = 0;
        while(count < bound){
            name = scan.next();
            long score = scan.nextLong();
            Node newSol = new Node(name, score);
            heap.insert(newSol);
            map.put(name, newSol);
            count++;
        }

        int type;
        long plus,limit;
        int size2 = scan.nextInt();// Query Read Here
        for(int i=0; i < size2; i++){
            type = scan.nextInt();
            if(type == 1){
                name = scan.next();
                plus = scan.nextLong();
                map.get(name).score += plus;
                Node temp = map.get(name);
                heap.Heapify(temp.Position);
            }

            else{
                limit = scan.nextLong();// Elimination score read here
                while(heap.Peek()!=null && heap.Peek() < limit ) {
                    heap.remove(limit);
                }
                output.write(heap.currentsize + "\n");
            }
        }
        output.flush();
        output.close();
    }
}

class Node{
    String name;
    long score;
    int Position;

    Node(String name, long score) {
        this.name = name;
        this.score = score;
    }
}

class minHeap{
    Node[] heapArray;
    int currentsize;
    long maxsize;
    int first_pos = 1;

    minHeap(long size){
        this.maxsize = size;
        this.currentsize = 0;
        this.heapArray = new Node[(int) (maxsize + 1)];
        heapArray[0] = new Node("root", 0);
    }

    int getParent(int pos){
        return ( pos / 2 );
    }
    int getleft(int pos){
        return ( pos * 2 );
    }
    int getright(int pos){ return (pos * 2) + 1; }

    boolean isLeaf(int pos){
        if(pos > ( currentsize / 2 ) && pos <= currentsize){
            return true;
        }
        return false;
    }


    void Heapify(int pos){
        // Base case: if it hits the leaf, program stops
        if(isLeaf(pos)){
            return;
        }
        if(!isLeaf(pos)){
            if( (heapArray[getright(pos)]!= null)
                    && (heapArray[getleft(pos)].score < heapArray[pos].score
                    || heapArray[getright(pos)].score < heapArray[pos].score)){
                if(heapArray[getleft(pos)].score > heapArray[getright(pos)].score){
                    swapnode(pos,getright(pos));
                    Heapify(getright(pos));
                }
                else{
                    swapnode(pos,getleft(pos));
                    Heapify(getleft(pos));
                }
            }
        }
    }

    void swapnode(int first, int second){
        heapArray[first].Position = second;
        heapArray[second].Position = first;
        Node temp = heapArray[first];
        heapArray[first] = heapArray[second];
        heapArray[second] = temp;
    }

    void insert(Node n){
        if(currentsize > maxsize ){
            return;
        }
        heapArray[++currentsize] = n;
        n.Position = currentsize;
        int newsize = currentsize;
        while(heapArray[newsize].score < heapArray[getParent(newsize)].score && (currentsize > 1)){
            swapnode(newsize, getParent(newsize));
            newsize = getParent(newsize);
        }
    }

    Long Peek(){
        if(currentsize == 0){
            return null;
        }
        return heapArray[first_pos].score;
    }

    // Take the last one out and insert it back in
    // Heapify afterwards
    void remove(long limit){
        if( currentsize != 0 && heapArray[first_pos].score < limit ){
            heapArray[currentsize].Position = first_pos;
            heapArray[first_pos] = heapArray[currentsize];
            heapArray[currentsize] = null;
            Heapify(first_pos);
            currentsize--;
        }
    }
}
