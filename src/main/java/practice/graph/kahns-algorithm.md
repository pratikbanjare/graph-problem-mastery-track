# Kahn's Algorithm 

Kahn's algorithm uses BFS and in-degree to perform topological sorting.

To identify if a cycle exist in graph, we check `result.size() != grpah.getNumberOfVertices()`. 


Psudocode 
```text
1. Calculate indegree[]

2. Add all indegree-0 vertices to queue

3. BFS:
   poll vertex
   add to result
   decrement neighbors' indegrees
   enqueue neighbors reaching 0

4. Compare result.size() with V
```
