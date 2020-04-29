import java.util.*;
class Edge {
    int succ;
    Edge next;
    Edge(int succ, Edge next){
        this.succ = succ;
        this.next = next;
    }
}

class Graph {
    Edge[] A;
    // A[u] points to the head of a linked list;
    // p in the list corresponds to an edge u -> p.succ in the graph
    Graph(int n) {
        // initialize a graph with n vertices and no edges
        A = new Edge[n];
    }
    void addEdge(int u, int v) {
        // add an edge i -> j to the graph
        A[u] = new Edge(v, A[u]);
    }
}

class DFSStarter {
    static boolean boocycle = false;

    static Graph graph;

    static int[] color;// 0 for white, 1 for gray, 2 for black

    static int[] parent;

    static int preIndex = 0;

    static ArrayList<Integer> result = new ArrayList<>();

    static void DFS() {
        color = new int[graph.A.length + 1];
        for(int v = 0; v < graph.A.length; v++){
            color[v] = 0;  // for each vertices in the graph, color of nodes are all white(0)
        }
        //start recDFS here
        for(int v = 0; v < graph.A.length; v++){ // performs a full DFS on graph
            if(color[v] == 0){
                recDFS(v);
            }
        }
    }
    static void recDFS(int u) {
        // perform a recursive DFS, starting at u
        color[u] = 1;
        while(graph.A[u] != null){
            // v = graph.A[u].succ
            if(color[graph.A[u].succ] == 0){
                parent[graph.A[u].succ] = u;
                recDFS(graph.A[u].succ);
            }

            else if(color[graph.A[u].succ] == 1){
                if(preIndex == 0){
                    parent[graph.A[u].succ] = u;
                    preIndex = u;
                }
                boocycle = true;
                break;
            }
            graph.A[u] = graph.A[u].next;
        }
        color[u] = 2;
    }


    static void backtrace(int u){
        int i = 0;
        int firstNode = u;
        boolean found = true;
        while(found){
            //index, parent array back trace
            result.add(i,u);
            u = parent[u];
            i++;
            if(u == firstNode){
                found = false;
            }
        }
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int graphsize = scanner.nextInt() + 1;
        graph = new Graph(graphsize);
        parent = new int[graphsize];

        int parentsize = scanner.nextInt();

        for(int x = 0; x < parentsize; x++){
            int succ = scanner.nextInt();
            int next = scanner.nextInt();
            graph.addEdge(succ,next);
        }

        DFS();
        backtrace(preIndex);
        if(boocycle == true){
            System.out.println("1");

            for(int i = result.size() - 1; i >= 0; i--){
                System.out.print(result.get(i) + " ");
            }
        }
        else{
            System.out.println("0");
        }
    }
}

