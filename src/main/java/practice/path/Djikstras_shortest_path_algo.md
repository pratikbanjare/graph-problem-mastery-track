# Dijstra's Algorithm 

- Algorithm to find shortest distance in a weighted graph. 
- The algorithm requires non-negative edge weights. 
- **Key Idea**: Maintain an object `distance[v]` to capture best distance found so far from `source` to `v`.
- **Relaxing the edge**: For an edge `u -> v` with weight `w`, if `distance[u] + w < distance[v]`, then `distance[v] = distance[u] + w`.
- We need to repeatedly retrieve the ertex with the **smallest distance**. 
- Instead of normal `Queue`, we need `PriorityQueue`.`

## Required Data Structure

```java
class Node{
    int vertex;
    int distance;
}

PriorityQueue<Node> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.distance, b.distance));
```

# Psudocode 

```text
put (source, 0)

while queue isn't empty:

    poll smallest-distance entry

    if entry is stale:
        continue

    for each weighted edge:

        calculate candidate distance

        if candidate < distance[neighbor]:
            update distance[neighbor]
            add new (neighbor, candidate) to queue

```

## Sample graph 
```text
1 → 2 (10)
1 → 3 (1)
3 → 2 (1)
```

