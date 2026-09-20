Feature: Calculate shortest paths in a directed acyclic graph

  Scenario: Select the least-cost route when multiple routes exist
    Given a directed weighted graph with 4 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 5      |
      | 1    | 3  | 2      |
      | 3    | 2  | 1      |
      | 2    | 4  | 1      |
      | 3    | 4  | 5      |
    And the source vertex is 1
    When I calculate the DAG shortest paths
    Then the shortest distances should be:
      | vertex | distance |
      | 1      | 0        |
      | 2      | 3        |
      | 3      | 2        |
      | 4      | 4        |

  Scenario: Support negative edge weights in an acyclic graph
    Given a directed weighted graph with 4 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 4      |
      | 1    | 3  | 2      |
      | 3    | 2  | -3     |
      | 2    | 4  | 2      |
      | 3    | 4  | 7      |
    And the source vertex is 1
    When I calculate the DAG shortest paths
    Then the shortest distances should be:
      | vertex | distance |
      | 1      | 0        |
      | 2      | -1       |
      | 3      | 2        |
      | 4      | 1        |

  Scenario: Return zero for the source vertex
    Given a directed weighted graph with 3 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 6      |
      | 2    | 3  | 4      |
    And the source vertex is 1
    When I calculate the DAG shortest paths
    Then the distance for vertex 1 should be 0

  Scenario: Mark unreachable vertices as unreachable
    Given a directed weighted graph with 4 vertices
    And the following weighted edges:
      | from | to | weight |
      | 1    | 2  | 4      |
      | 2    | 3  | 2      |
    And the source vertex is 1
    When I calculate the DAG shortest paths
    Then the shortest distances should be:
      | vertex | distance    |
      | 1      | 0           |
      | 2      | 4           |
      | 3      | 6           |
      | 4      | unreachable |