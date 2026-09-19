Feature: Topological sort using Kahn's algorithm

  Scenario: Returns a valid topological ordering for a directed acyclic graph
    Given a directed graph with 4 number of vertices
    And edges
      | from | to |
      | 1    | 2  |
      | 1    | 3  |
      | 2    | 4  |
      | 3    | 4  |
    When kahns algorithm is called
    Then the topological ordering is
      | vertex |
      | 1      |
      | 2      |
      | 3      |
      | 4      |

  Scenario: Returns a valid ordering when graph has multiple valid topological results
    Given a directed graph with 4 number of vertices
    And edges
      | from | to |
      | 1    | 3  |
      | 2    | 3  |
      | 3    | 4  |
    When kahns algorithm is called
    Then the topological ordering is
      | vertex |
      | 1      |
      | 2      |
      | 3      |
      | 4      |

  Scenario: Returns a valid ordering for a disconnected graph
    Given a directed graph with 5 number of vertices
    And edges
      | from | to |
      | 1    | 2  |
      | 3    | 4  |
    When kahns algorithm is called
    Then the topological ordering is
      | vertex |
      | 1      |
      | 3      |
      | 5      |
      | 2      |
      | 4      |

  Scenario: Returns correct ordering for a graph with a single root node
    Given a directed graph with 4 number of vertices
    And edges
      | from | to |
      | 1    | 2  |
      | 2    | 3  |
      | 3    | 4  |
    When kahns algorithm is called
    Then the topological ordering is
      | vertex |
      | 1      |
      | 2      |
      | 3      |
      | 4      |

  Scenario: Throws GraphException when the graph contains a cycle
    Given a directed graph with 3 number of vertices
    And edges
      | from | to |
      | 1    | 2  |
      | 2    | 3  |
      | 3    | 1  |
    When kahns algorithm is called
    Then a GraphException is thrown

  Scenario: Throws GraphException for a graph with self-loop
    Given a directed graph with 3 number of vertices
    And edges
      | from | to |
      | 1    | 1  |
      | 2    | 3  |
    When kahns algorithm is called
    Then a GraphException is thrown