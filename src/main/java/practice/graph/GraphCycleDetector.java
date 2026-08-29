package practice.graph;

public class GraphCycleDetector {

    private static final String DFS = "[DFS] ";

    public boolean dfsCycle(Graph graph){

        int[] state = new int[graph.getVertexCount()];
        for (int i = 1; i<=graph.getVertexCount(); ++i){
            if (state[i-1] == 2){
                continue;
            }
            if (dfsCycle(graph, i-1, state)){
                return true;
            }
        }
        return false;
    }

    private boolean dfsCycle(Graph graph, int vertex, int[] state){

        System.out.println(DFS + "[START] State of vertex " + vertex + " is " + state[vertex]);
        state[vertex] = 1;
        System.out.println(DFS + "[STATE CHANGE] Changed state of vertex " + vertex  + " to " + state[vertex]);
        for ( int neighbor : graph.getEdgesOfVertex(vertex)){
            System.out.println(DFS + "[Neighbor] Processing neighbor " + neighbor);
            if (state[neighbor] == 1){
                return true;
            } else if (state[neighbor] == 0) {
                if ( dfsCycle(graph, neighbor, state) ){
                    return true;
                }
            }
        }
        state[vertex] = 2;
        System.out.println(DFS + "[END] State of vertex " + vertex + " is " + state[vertex]);

        return false;
    }
}


