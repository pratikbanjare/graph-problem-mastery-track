package practice.path;

import practice.exception.GraphException;
import practice.graph.Graph;

public interface IShortestPath {

    public int[] shortestPath(Graph graph, int source) throws GraphException;
}
