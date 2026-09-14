Feature: BipartiteChecker

  Scenario: Graph with one edge is bipartite
    Given a graph whose vertex count is 2
    And edges
      | from | to |
      | 1    | 2 |
    When I check whether the graph is bipartite
    Then the result should be true

  Scenario: Odd cycle graph is not bipartite
    Given a graph whose vertex count is 3
    And edges
      | from | to |
      | 1    | 2 |
      | 2    | 3 |
      | 3    | 1 |
    When I check whether the graph is bipartite
    Then the result should be false

  Scenario: Even cycle graph is bipartite
    Given a graph whose vertex count is 4
    And edges
      | from | to |
      | 1    | 2 |
      | 2    | 3 |
      | 3    | 4 |
      | 4    | 1 |
    When I check whether the graph is bipartite
    Then the result should be true