# Bipartite Graph

A Bipartite graph is a graph whose every edge can be separated into 2 sets, such that edges in each set does not have any edge to each other. 

Example - Bipartite Graph 
```text
Say, in a graph with 5 vertex,
Edges are 
(1,2)
(2,3)
(3,4)
(3,5)
(1,5)

Set 1 : [1, 3 ] 
Set 2 : [2, 4, 5 ]

In set 1, vertex 1 and 3 have no edge to each other 
And in set 2, vertex 2,4,5 have no edges to each other. 

Thus above graph is a biarptite graph. 
```

Example - non-bipartite graph
```text
Say, in a graph with 5 vertex,
Edges are 
(1,2)
(2,3)
(3,4)
(3,5)
(1,5)
(2,4)

indepemdent vertex set - 
Set 1 : [1,3]
Set 2 : [4,5]
Set 3 : [2]

Here there will be more that 2 independent vertex set.
Thus, above graph is not bipartite. 
```

Psudocode 
```text
color[] // (-1 -> unviisted), (0 -> set 1), (1 -> set 2) 

iterate over all unvisited vertex 
    initialize queue
    color[vertex] = 1
    add vertex to queue
    till queue is not empty 
        take out vertex 'v' from queue (FIFO)
        for each neighbor of vertex v
            if v is unvisited, then assign opposing color to v
                color[neightbor] = 1 - color[v]
            else if neighbor has same color as of v, color[v] == color[neighbor],
                return false  
return true

```

