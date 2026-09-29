Feature: Finding strongly connected components with Tarjan's algorithm

  Tarjan's algorithm should identify groups of vertices where
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

  Scenario: Find singleton components in an acyclic directed graph
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 3  |
      | 3    | 4  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |
      | 2         |
      | 3         |
      | 4         |

  Scenario: Keep isolated vertices as singleton components
    Given a directed graph with 4 vertices and no edges
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |
      | 2         |
      | 3         |
      | 4         |

  Scenario: Keep isolated vertices separate from a nontrivial component
    Given a directed graph with 4 vertices and the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 1  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1,2       |
      | 3         |
      | 4         |

  Scenario: Keep an edge to a completed component from lowering low-link
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 2  |
      | 2    | 1  |
      | 3    | 1  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1,2       |
      | 3         |

  Scenario: Treat a self-loop as one singleton component
    Given a directed graph with the following edges:
      | from | to |
      | 1    | 1  |
    When I find the strongly connected components
    Then the strongly connected components should be:
      | component |
      | 1         |

  Scenario: Reject an undirected graph
    Given an undirected graph with the following edges:
      | from | to |
      | 1    | 2  |
    When I attempt to find the strongly connected components
    Then an IllegalArgumentException should be thrown
