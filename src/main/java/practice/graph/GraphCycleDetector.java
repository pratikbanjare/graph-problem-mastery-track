package practice.graph;

public class GraphCycleDetector {

    private static final String DFS = "[DFS] ";

    public boolean cycleDetector(Graph graph){

        int[] state = new int[graph.getVertexCount() + 1];
        for (int vertex = 1; vertex <= graph.getVertexCount(); ++vertex){
            if (state[vertex] == 2){
                continue;
            }
            if (cycleDetector(graph, vertex, state)){
                return true;
            }
        }
        return false;
    }

    private boolean cycleDetector(Graph graph, int vertex, int[] state){

        System.out.println(DFS + "[START] State of vertex " + vertex + " is " + state[vertex]);
        state[vertex] = 1;
        System.out.println(DFS + "[STATE CHANGE] Changed state of vertex " + vertex  + " to " + state[vertex]);
        for ( int neighbor : graph.getEdgesOfVertex(vertex)){
            System.out.println(DFS + "[Neighbor] Processing neighbor " + neighbor);
            if (state[neighbor] == 1){
                return true;
            } else if (state[neighbor] == 0) {
                if ( cycleDetector(graph, neighbor, state) ){
                    return true;
                }
            }
        }
        state[vertex] = 2;
        System.out.println(DFS + "[END] State of vertex " + vertex + " is " + state[vertex]);

        return false;
    }
    public boolean dfsCycle(Graph graph){

        int[] state = new int[graph.getVertexCount() + 1];
        for (int vertex = 1; vertex <= graph.getVertexCount(); ++vertex){
            if (state[vertex] == 2){
                continue;
            }
            if (dfsCycle(graph, vertex, state)){
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


