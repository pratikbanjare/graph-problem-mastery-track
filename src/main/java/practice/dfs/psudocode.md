# DFS psudocode 

```java
dfs(vertex){
    if (visited[vertex]){
        return;
    }
    visited[vertex] = true;
    for (neighbor in edges.get(vertex)){
        dfs(neighbor);
    }
}
```