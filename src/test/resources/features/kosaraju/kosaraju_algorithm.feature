Feature: Finding strongly connected components with Kosaraju's algorithm

  Kosaraju's algorithm should identify groups of vertices where
  every vertex is reachable from every other vertex in the group.

  Scenario: Find multiple strongly connected components
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 2  |
      | 1    | 3  |
      | 2    | 4  |
      | 3    | 4  |
      | 4    | 5  |
      | 5    | 4  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |
      | 2         |
      | 3         |
      | 4,5       |

  Scenario: Find one strongly connected component in a directed cycle
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 3  |
      | 3    | 1  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1,2,3     |

  Scenario: Treat disconnected vertices as separate components
    Given a directed graph with 4 vertices and no edges
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |
      | 2         |
      | 3         |
      | 4         |

  Scenario: Find a self-loop as a single vertex component
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 1  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |

  Scenario: Keep one-way connected vertices in separate components
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 2  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |
      | 2         |

  Scenario: Reject an undirected graph
    Given an undirected graph with the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 3  |
    When I attempt to find the strongly connected components
    Then an IllegalArgumentException should be thrown
    And the exception message should be:
      """
      Kosaraju's algorithm requires Directed Graph!!!
      """

  Scenario: Find strongly connected components in a graph with multiple cycles
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 1  |
      | 2    | 3  |
      | 3    | 4  |
      | 4    | 3  |
      | 4    | 5  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1,2       |
      | 3,4       |
      | 5         |