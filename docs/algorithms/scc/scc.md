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

## Tarjan's Algorithm

Tarjan manages to find SCCs with one DFS on the original graph.

During a DFS, how can we tell that a vertex is the “root” of an SCC?

Consider the graph
```text
1 → 2 → 3
    ↑   ↓
    └───┘

3 → 4 → 5
    ↑   ↓
    └───┘
```

Imagine DFS starts at 1:

```text
1
└── 2
└── 3
├── 2   ← back edge
└── 4
└── 5
└── 4
```

discoveryTime - "When did DFS first discover me?"

lowLink[v] - "What is the earliest-discovered vertex that I can reach while staying connected through the DFS structure?"

stack - //The stack contains vertices whose SCC has not been finalized yet.

````text
discoveryTime → when was I discovered?
lowLink       → how far back can I reach?
stack         → which vertices have unresolved SCC membership?
onStack       → is this vertex still in that unresolved set?
````

hree cases :
v → u

A. u unvisited
- DFS(u)
-  lowLink[v] = min(lowLink[v], lowLink[u])

B. u visited + on stack
- lowLink[v] = min(lowLink[v], discoveryTime[u])

C. u visited + NOT on stack
- do nothing

And after all neighbors of v have been processed:

```text
if (lowLink[v] == discoveryTime[v])
// v is the root of an SCC
// pop stack until v
```

Example 
```text
1 → 2 → 3
    ↑   ↓
    └───┘
```

tarjan's algorithm on above graph - 

```text
DFS(1)
  |
  v
DFS(2)
  |
  v
DFS(3)
  |
  | 3 → 2 (back edge)
  v
lowLink[3] = 1
  |
  v
return to 2
  |
  v
lowLink[2] = min(1, 1) = 1

lowLink[2] == discoveryTime[2]
        ↓
     SCC root
        ↓
    pop 3, pop 2
```

```text
dfs(v)

    assign discoveryTime[v]
    initialize lowLink[v]
    increment time

    push v
    mark v onStack

    for each outgoing edge v → u

        if u is unvisited
            dfs(u)
            lowLink[v] = min(lowLink[v], lowLink[u])

        else if u is on stack
            lowLink[v] = min(lowLink[v], discoveryTime[u])

        else
            do nothing

    if lowLink[v] == discoveryTime[v]

        while v has not been popped
            pop u
            mark u not onStack
            add u to current SCC
```