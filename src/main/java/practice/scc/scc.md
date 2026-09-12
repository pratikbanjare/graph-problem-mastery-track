# Strongly Connected Components

Definition
- A set of vertices forms strongly connected component, if every vertex in the component can be reached from every other vertex.

Example 
```text
Graph 

1 → 2
↓   ↓
3 → 4

4 → 5
↑   ↓
└───┘

edges 

1 → 2
1 → 3
2 → 4
3 → 4
4 → 5
5 → 4
```

Here strongly connected components are 
```text
{1}, {2}, {3}, {5,4}
```


---

## Kosaraju's algorithm

```text
1. DFS on the original graph.
2. Record vertices by DFS finishing time.
3. Reverse every edge.
4. Process vertices in decreasing finishing-time order.
5. Run DFS again. Each DFS traversal gives one SCC.
```

in SCC, reversing DFS edges does not break mutual reachability.
The first DFS determines the order; reversing the graph makes each second-phase DFS stay inside exactly one SCC.


Mental Model 
```text
Original graph
      ↓
DFS + finishing times
      ↓
Ordering of vertices
      ↓
Reverse every edge
      ↓
Process highest finishing time first
      ↓
Each DFS = one SCC
```

```text
Your Kosaraju implementation needs three major pieces:
First DFS
    visited[]
    DFS through original graph
    add vertex to finishingOrder after its neighbors finish.
Reverse graph
    you've already implemented this.
Second DFS
    create a fresh visited[]
    iterate finishingOrder backwards
    whenever you encounter an unvisited vertex, start a DFS on the reversed graph
    all vertices discovered by that DFS form one SCC
```