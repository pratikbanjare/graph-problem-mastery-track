Feature: Minimum spanning tree using Kruskal's algorithm

  Scenario: Returns the minimum spanning tree for a connected undirected graph
    Given an undirected graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 10     |
      | 1    | 3  | 6      |
      | 1    | 4  | 5      |
      | 2    | 4  | 15     |
      | 3    | 4  | 4      |
    When Kruskal's algorithm is called
    Then the minimum spanning tree contains 3 edges
    And the selected edges are
      | from | to | weight |
      | 3    | 4  | 4      |
      | 1    | 4  | 5      |
      | 1    | 2  | 10     |
    And the total weight of the minimum spanning tree is 19

  Scenario: Skips an edge that creates a cycle
    Given an undirected graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 1      |
      | 2    | 3  | 2      |
      | 1    | 3  | 10     |
      | 3    | 4  | 3      |
    When Kruskal's algorithm is called
    Then the minimum spanning tree contains 3 edges
    And the selected edges are
      | from | to | weight |
      | 1    | 2  | 1      |
      | 2    | 3  | 2      |
      | 3    | 4  | 3      |
    And the edge from 1 to 3 is not selected
    And the total weight of the minimum spanning tree is 6

  Scenario: Rejects a directed graph
    Given a directed graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 1      |
      | 2    | 3  | 2      |
      | 3    | 4  | 3      |
    When Kruskal's algorithm is called
    Then an IllegalArgumentException is thrown
    And the exception message is "Undirected graphs are not allowed in Kruskal"

  Scenario: Rejects a disconnected graph
    Given an undirected graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 4      |
      | 3    | 4  | 5      |
    When Kruskal's algorithm is called
    Then an IllegalArgumentException is thrown
    And the exception message contains "Graph is disconnected"