package practice.graph;

public class GraphMain {

    public static void main(String[] args) {

        try {
            Graph graph = new Graph(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Graph graph = new Graph(-1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
