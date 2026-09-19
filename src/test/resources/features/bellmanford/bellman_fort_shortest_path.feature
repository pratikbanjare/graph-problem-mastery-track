Feature: Bellman-Ford shortest path calculation

  Scenario: Finds shortest distances when a graph contains a negative edge but no negative cycle
    Given a directed graph with 4 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 4      |
      | 1    | 3  | 5      |
      | 2    | 3  | -2     |
      | 3    | 4  | 3      |
    When I compute the shortest path from source vertex 1
    Then the distances should be:
      | vertex | distance |
      | 1      | 0        |
      | 2      | 4        |
      | 3      | 2        |
      | 4      | 5        |
    And no exception should be thrown

  Scenario: Keeps unreachable vertices as infinite
    Given a directed graph with 5 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 3      |
      | 2    | 3  | 2      |
    When I compute the shortest path from source vertex 1
    Then the distances should be:
      | vertex | distance            |
      | 1      | 0                   |
      | 2      | 3                   |
      | 3      | 5                   |
      | 4      | Integer.MAX_VALUE   |
      | 5      | Integer.MAX_VALUE   |
    And no exception should be thrown

  Scenario: Returns zero for the source and infinite for all other vertices when no edges are connected to the source
    Given a directed graph with 4 vertices
    And the following weighted edges:
      | from | to | weight |
      | 2    | 3  | 1      |
      | 3    | 4  | 2      |
    When I compute the shortest path from source vertex 1
    Then the distances should be:
      | vertex | distance            |
      | 1      | 0                   |
      | 2      | Integer.MAX_VALUE   |
      | 3      | Integer.MAX_VALUE   |
      | 4      | Integer.MAX_VALUE   |
    And no exception should be thrown

  Scenario: Throws an exception when a reachable negative cycle is present
    Given a directed graph with 3 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 1      |
      | 2    | 3  | -2     |
      | 3    | 1  | 0      |
    When I compute the shortest path from source vertex 1
    Then a GraphException should be thrown
    And the exception message should be "Negative Cycle detected!!!!"

  Scenario: Ignores a negative cycle that is not reachable from the selected source
    Given a directed graph with 4 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 2      |
      | 3    | 4  | -1     |
      | 4    | 3  | -1     |
    When I compute the shortest path from source vertex 1
    Then the distances should be:
      | vertex | distance |
      | 1      | 0        |
      | 2      | 2        |
      | 3      | Integer.MAX_VALUE |
      | 4      | Integer.MAX_VALUE |
    And no exception should be thrown

  Scenario: Returns a valid result for a graph with a single vertex
    Given a directed graph with 1 vertex
    And there are no weighted edges in the graph
    When I compute the shortest path from source vertex 1
    Then the distances should be:
      | vertex | distance |
      | 1      | 0        |
    And no exception should be thrown