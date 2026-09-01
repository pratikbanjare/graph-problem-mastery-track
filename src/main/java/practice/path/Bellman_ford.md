# Bellman Ford's Algorithm 

- It can detect negative-weighted-cycle.
- Given a graph with `V` nodes, it can contain at most `V-1` edges to shortest path. 
- If a path contains `V` or more edges, some vertex must repeat creating a cycle.

Relaxation of edges 
```java
for every edge (u → v, weight):
if distance[u] + weight < distance[v]:
distance[v] = distance[u] + weight
```

- One iteration can propogate information across several edges depending on e edge ordering. 
- **Negative Cycle Detection**: after `V-1` round, if we perform one additional round and still relax the edges, then something is wrong. 
- IF `distance[u] = Integer.MAX_VALUE`, then `u` is unreachable from source. 


##  Psudocode

```java
initialize distance

for V-1 passes:

    changed = false

    for every vertex:
        for every outgoing WeightedEdge:

            if source vertex is reachable:
                calculate candidate

                if candidate improves destination:
                    update distance
                    changed = true

    if !changed:
        break

if all V-1 passes completed:
    scan all edges once more
    detect reachable negative cycle
```

