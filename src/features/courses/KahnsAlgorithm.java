package features.courses;

import java.util.*;

//
//This class uses Kahn's Algorithm to generate the valid order of courses.

public class KahnsAlgorithm {

    public static List<String> topologicalSort(CourseGraph graph) {

        Map<String, Integer> indegreeCopy = new HashMap<>();
        indegreeCopy.putAll(graph.getIndegree());

        Queue<String> queue = new LinkedList<>();
        List<String> result = new ArrayList<>();

        // Add all courses with no prerequisites
        for (String code : indegreeCopy.keySet()) {
            if (indegreeCopy.get(code) == 0) {
                queue.add(code);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            List<String> neighbors = graph.getAdjacency().get(current);

            if (neighbors != null) {
                for (String next : neighbors) {
                    int newValue = indegreeCopy.get(next) - 1;
                    indegreeCopy.put(next, newValue);

                    if (newValue == 0) {
                        queue.add(next);
                    }
                }
            }
        }

        if (result.size() != graph.getCourses().size()) {
            throw new IllegalStateException("Cycle detected in course prerequisites.");
        }

        return result;
    }
}