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

