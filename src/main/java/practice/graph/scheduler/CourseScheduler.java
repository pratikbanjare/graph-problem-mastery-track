package practice.graph.scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CourseScheduler {
    /*
    1. Build adjacency list
    2. Build indegree[]
    3. Put indegree-0 courses into queue
    4. Process queue
    5. Count processed courses
    6. processed == numCourses → true
       otherwise → false
     */
    public List<Integer> canFinish(int numCourses, int[][] prerequisites) {

        List<List<Integer>> adjacencyList = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        // initialize adjacency list
        for (int i = 0; i< numCourses; ++i){
            adjacencyList.add(new ArrayList<>());
        }

        // populate adjacency list and in-degree array
        for (int preresusite = 0; preresusite< prerequisites.length; ++preresusite){
            int a = prerequisites[preresusite][0];
            int b = prerequisites[preresusite][1];

            adjacencyList.get(b).add(a);
            inDegree[a]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();

        // populate queue
        for (int i = 0; i<numCourses; ++i) {
            if (inDegree[i] == 0){
                queue.add(i);
            }
        }

        // BFS to find if courses completion is possible or not ?
        List<Integer> completionOrder = new ArrayList<>();
        while (!queue.isEmpty()){
            int vertex = queue.poll();
            for (Integer neighbor : adjacencyList.get(vertex)){
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0){
                    queue.add(neighbor);
                }
            }
            completionOrder.add(vertex);
        }
        if (numCourses == completionOrder.size()){
            return completionOrder;
        }
        return null;
    }
}
