Feature: Find connected components in an undirected graph

  Scenario: Graph with two disconnected groups
    Given a graph with 5 vertices
    And the graph has an edge between 1 and 2
    And the graph has an edge between 2 and 3
    And the graph has an edge between 4 and 5
    When I find the connected components
    Then the connected components should be
      | 1 | 2 | 3 |
      | 4 | 5 |   |

  Scenario: Graph with isolated vertices
    Given a graph with 3 vertices
    When I find the connected components
    Then the connected components should be
      | 1 |
      | 2 |
      | 3 |

  Scenario: Graph with one fully connected component
    Given a graph with 4 vertices
    And the graph has an edge between 1 and 2
    And the graph has an edge between 2 and 3
    And the graph has an edge between 3 and 4
    When I find the connected components
    Then the connected components should be
      | 1 | 2 | 3 | 4 |
