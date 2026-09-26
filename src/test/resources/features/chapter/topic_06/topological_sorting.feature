Feature: Topological sort for directed graphs

  Scenario: Valid DAG with a simple dependency chain
    Given a directed graph with vertices 1, 2, 3, 4
    And the following edges:
      | from | to |
      | 1    | 2  |
      | 1    | 3  |
      | 2    | 4  |
      | 3    | 4  |
    When the topological sort is calculated
    Then the result should contain each vertex exactly once
    And for every edge in the graph, the source vertex appears before the target vertex

  Scenario: Valid DAG with multiple independent branches
    Given a directed graph with vertices 1, 2, 3, 4, 5
    And the following edges:
      | from | to |
      | 1    | 2  |
      | 1    | 3  |
      | 2    | 4  |
      | 3    | 5  |
    When the topological sort is calculated
    Then the result should contain each vertex exactly once
    And for every edge in the graph, the source vertex appears before the target vertex
    And vertex 1 should appear before both 2 and 3

  Scenario: Valid DAG with disconnected components
    Given a directed graph with vertices 1, 2, 3, 4, 5, 6
    And the following edges:
      | from | to |
      | 1    | 2  |
      | 3    | 4  |
      | 5    | 6  |
    When the topological sort is calculated
    Then the result should contain each vertex exactly once
    And for every edge in the graph, the source vertex appears before the target vertex
    And the ordering should still include all disconnected components in one valid overall sequence

  Scenario: Cycle detection failure
    Given a directed graph with vertices 1, 2, 3
    And the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 3  |
      | 3    | 1  |
    When the topological sort is calculated
    Then it should fail with an IllegalArgumentException
    And the error message should mention a cycle is detected
