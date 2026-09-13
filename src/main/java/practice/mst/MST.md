# Minimum Spanning Tree 

MST vs Shortest path 
- Shortest path finds cheapest path from one vertex to another. 
- Minimum Spanning tree connects all edges with minimum total edge weights, without creating cycle. 

Major Algorithm 
- Kruskal's Algorithm - uses **Union-Find / Disjoint union set**
- Prim's Algorithm - uses a **Priority Queue**

Features to enhance performance - 
- Path compression `parent[]`
- **union by rank** attach smaller tree under larger tree - `rank[]`
  - Different ranks: attach smaller rank under larger rank; don't change the larger rank.
  - Equal ranks: attach either one under the other, then increase the new root's rank by 1.


Kruskal's Algo steps 
```text
1. Make sure graph is UNDIRECTED
2. Get a separate edge list
3. Sort edges by weight
4. Create DSU
5. Create MST result
6. Process sorted edges
      ├── union succeeds → select edge
      └── union fails    → skip edge
7. Stop at V - 1 selected edges
8. Decide what to do if fewer than V - 1 edges were selected
```

## Prim's Algorithm

```text
        2
   1 ------- 2
   | \       |
  4|  \1     |3
   |   \     |
   3 ------- 4
        1
```

Fundamental Idea
- "I already have a tree. What is the cheapest edge that connects my tree to a vertex outside it?"

Algorithm
```text
start vertex
    ↓
mark visited
    ↓
add its edges to min-PQ
    ↓
while PQ not empty
    ↓
poll cheapest edge
    ↓
destination visited?
  yes → skip
  no  → accept
          ↓
       mark visited
          ↓
       add its edges to PQ
          ↓
       V - 1 edges?
       yes → done
       no  → continue
    ↓
PQ empty before V - 1?
    ↓
throw exception
```


```text
start = 1
visited[start] = true

Add every edge from start to the PriorityQueue

while PQ is not empty:

    edge = PQ.poll()

    if edge.to is already visited:
        skip it

    otherwise:
        accept edge
        mark edge.to as visited
        add all edges from edge.to to PQ

    stop when MST contains V - 1 edges
```
