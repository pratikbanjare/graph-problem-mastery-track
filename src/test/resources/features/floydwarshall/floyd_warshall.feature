Feature: Floyd-Warshall shortest paths

  Scenario: Calculate shortest paths in a directed graph
    Given a directed graph with 4 vertices
    And the edges:
      | from | to | weight |
      | 1    | 2  | 4      |
      | 2    | 3  | 3      |
      | 1    | 3  | 10     |
      | 3    | 4  | 2      |
    When I run Floyd-Warshall on the graph
    Then the shortest distance from 1 to 3 should be 7
    And the shortest distance from 1 to 4 should be 9
    And the shortest distance from 2 to 4 should be 5

  Scenario: Prefer a shorter indirect path over a direct edge
    Given a directed graph with 3 vertices
    And the edges:
      | from | to | weight |
      | 1    | 2  | 4      |
      | 2    | 3  | -6     |
      | 1    | 3  | 3      |
    When I run Floyd-Warshall on the graph
    Then the shortest distance from 1 to 3 should be -2

  Scenario: Detect a negative cycle
    Given a graph with 3 vertices
    And the edges:
      | from | to | weight |
      | 1    | 2  | 3      |
      | 2    | 3  | -4     |
      | 3    | 1  | 0      |
    When I run Floyd-Warshall on the graph
    Then a GraphException should be thrown with message containing "Negative Cycle exist"

  Scenario: Keep distance from a vertex to itself as zero when no negative cycle exists
    Given a directed graph with 3 vertices
    And the edges:
      | from | to | weight |
      | 1    | 2  | 5      |
      | 2    | 3  | 2      |
    When I run Floyd-Warshall on the graph
    Then the shortest distance from 1 to 1 should be 0
    And the shortest distance from 2 to 2 should be 0
    And the shortest distance from 3 to 3 should be 0
