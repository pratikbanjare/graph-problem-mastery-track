# Graph Cycle Detector 

Given a directed graph, with V vertices and E edges, find if there exist a cycle in it.

Example 1
```text
Verties - 4
edges 
1 -> 2
2 -> 3
1 -> 3
3 -> 4
4 -> 1
```

IN above graph, edge 4->1 creates a cycle, thus it has a cycle. 

Exaomle 2 
```text
Verties - 4
edges 
1 -> 2
2 -> 3
1 -> 3
3 -> 4
```
Above graph has no cycle. 

## Psudocode 
```text
initialize state array 
 (
 state[vertex] = 0 -> unvisited, 
 state[vertex] = 1 -> currently visiting, 
 state]vertex] = 2 -> processed not part of current iteration
 )
 
Iterate over each processed vertex ( state[vertex] !=2)
 cycleDetector(graph, vertex, state)
     change state of vertex to visiting ( sate[vertex] = 1)
     Iterate over each edge u from vertex
      if vertex u is currently visiting (state[u] = 1) 
       cycle detected,
      if vertex u is unvisited (state[vu] == 0)
       cycleDetector(graph, u, state)
    state[vertex] = 2;

```

