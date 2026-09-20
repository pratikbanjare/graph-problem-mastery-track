Feature: Shortest path computation for weighted graphs

  As a graph consumer
  I want to compute shortest distances and paths
  So that I can find the cheapest route from a source to any target

  Scenario: Calculate shortest distances from a source in a positive weighted graph
    Given a weighted graph with vertices 1 to 5
      And edges:
        | from | to | weight |
        | 1    | 2  | 4      |
        | 1    | 3  | 2      |
        | 2    | 3  | 1      |
        | 2    | 5  | 5      |
        | 3    | 2  | 2      |
        | 3    | 4  | 3      |
        | 4    | 5  | 1      |
    When the shortest distances are computed from source 1
    Then the distance to vertex 1 should be 0
      And the distance to vertex 2 should be 4
      And the distance to vertex 3 should be 2
      And the distance to vertex 4 should be 5
      And the distance to vertex 5 should be 6

  Scenario: Reconstruct the shortest path to a reachable target
    Given a weighted graph with vertices 1 to 5
      And edges:
        | from | to | weight |
        | 1    | 2  | 4      |
        | 1    | 3  | 2      |
        | 2    | 3  | 1      |
        | 2    | 5  | 5      |
        | 3    | 2  | 2      |
        | 3    | 4  | 3      |
        | 4    | 5  | 1      |
    When the shortest path is requested from source 1 to target 5
    Then the returned path should be [1, 3, 4, 5]

  Scenario: Return no path when the target is unreachable
    Given a weighted graph with vertices 1 to 4
      And edges:
        | from | to | weight |
        | 1    | 2  | 3      |
        | 2    | 3  | 2      |
      And no path exists from vertex 1 to vertex 4
    When the shortest path is requested from source 1 to target 4
    Then the path should be empty

  Scenario: Handle the case where source and target are the same
    Given a weighted graph with vertices 1 to 4
      And edges:
        | from | to | weight |
        | 1    | 2  | 3      |
        | 2    | 3  | 2      |
    When the shortest path is requested from source 3 to target 3
    Then the distance to the source should be 0
      And the path should be [3]

  Scenario: Reject a graph with a negative edge weight
    Given a weighted graph with vertices 1 to 3
      And edges:
        | from | to | weight |
        | 1    | 2  | 4      |
        | 2    | 3  | -2     |
    When validation is performed for Dijkstra's algorithm
    Then the graph should be rejected as invalid
      And an error should be raised indicating negative weight is not allowed

  Scenario: Keep unreachable vertices at infinite distance in a disconnected graph
    Given a weighted graph with vertices 1 to 4
      And edges:
        | from | to | weight |
        | 1    | 2  | 2      |
        | 3    | 4  | 5      |
    When the shortest distances are computed from source 1
    Then the distance to vertex 1 should be 0
      And the distance to vertex 2 should be 2
      And the distance to vertex 3 should be infinity
      And the distance to vertex 4 should be infinity