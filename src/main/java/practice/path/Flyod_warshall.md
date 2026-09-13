# Floyd Warshall Algorithm 

- Flyod Warshall allows more and more vertices to be used as intermediate vertices. 
- 

Core Recurrence of Flyod Warshall 
```java
distance[i][j] =
    min(
        distance[i][j],
        distance[i][k] + distance[k][j]
    )
```


## Psudocode 
```java
for k = 1..V
    for i = 1..V
        for j = 1..V
            if i → k is reachable
               AND
               k → j is reachable

                distance[i][j] =
                    min(
                        distance[i][j],
                        distance[i][k] + distance[k][j]
                    )
```
