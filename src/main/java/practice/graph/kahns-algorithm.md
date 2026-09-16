# Kahn's Algorithm 

Kahn's algorithm uses BFS and in-degree to perform topological sorting.

In-degree of a vertex v is the number of edges leading to vertex v.

Example 
```text
Given a grpah with 5 vertex 
and edges 
| 1 | 2 |
| 2 | 3 |
| 1 | 3 |
| 2 | 4 |
| 3 | 4 |
| 1 | 4 |
| 3 | 5 |

in degree of each vertex as as follows 
| vertex | in_degree |
| 1      | 0         |
| 2      | 1         |
| 3      | 2         |
| 4      | 3         |
| 5      | 1         |
```

For vertex with in-degree zero, there is no edge leading to it. 

Thus it can be used as starting point for explore topological order. 

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
