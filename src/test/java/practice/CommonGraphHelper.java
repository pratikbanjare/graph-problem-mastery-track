package practice;

import io.cucumber.datatable.DataTable;
import practice.graph.Graph;

import java.util.List;

public class CommonGraphHelper {

    public Graph createGraph(int vertices) {
        return new Graph(vertices);
    }

    public void addEdges(Graph graph, DataTable dataTable) {
        for (List<String> row : dataTable.asLists()) {
            if (row.size() < 2) {
                continue;
            }

            String fromCell = row.get(0);
            String toCell = row.get(1);

            if ("from".equalsIgnoreCase(fromCell) || "to".equalsIgnoreCase(fromCell)) {
                continue;
            }

            graph.addEdge(Integer.parseInt(fromCell), Integer.parseInt(toCell));
        }
    }
}