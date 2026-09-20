Feature: Minimum spanning tree using Prim's algorithm

  Scenario: Returns the minimum spanning tree for a connected undirected graph
    Given an undirected graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 2      |
      | 1    | 4  | 1      |
      | 1    | 3  | 4      |
      | 2    | 4  | 3      |
      | 3    | 4  | 1      |
    When Prim's algorithm is called
    Then the minimum spanning tree contains 3 edges
    And the selected edges are
      | from | to | weight |
      | 1    | 4  | 1      |
      | 4    | 3  | 1      |
      | 1    | 2  | 2      |
    And the total weight of the minimum spanning tree is 4

  Scenario: Skips an edge that would revisit an already visited vertex
    Given an undirected graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 1      |
      | 2    | 3  | 2      |
      | 1    | 3  | 10     |
      | 3    | 4  | 3      |
    When Prim's algorithm is called
    Then the minimum spanning tree contains 3 edges
    And the selected edges are
      | from | to | weight |
      | 1    | 2  | 1      |
      | 2    | 3  | 2      |
      | 3    | 4  | 3      |
    And the edge from 1 to 3 is not selected
    And the total weight of the minimum spanning tree is 6

  Scenario: Rejects a disconnected graph
    Given an undirected graph with 4 vertices
    And the graph has weighted edges
      | from | to | weight |
      | 1    | 2  | 4      |
      | 3    | 4  | 5      |
    When Prim's algorithm is called
    Then an IllegalArgumentException is thrown
    And the exception message is "Provided graph is disconnected. MST requires connected graph!!!!"
