package features.courses;

import java.util.*;

//
//This class uses Kahn's Algorithm to generate the valid order of courses.

public class KahnsAlgorithm {

    public static List<String> topologicalSort(CourseGraph graph) {

        Map<String, Integer> indegreeCopy = new HashMap<>(); // creates a copy of the indegree map from CourseGraph because Khan will change the values.
        indegreeCopy.putAll(graph.getIndegree());

        Queue<String> queue = new LinkedList<>();// create a queue since Khan is doing it recursivelly from 0
        List<String> result = new ArrayList<>(); // this "result" will keep the final valid order

        // Add all courses with no prerequisites
        for (String code : indegreeCopy.keySet()) { // go through all the courses.
            if (indegreeCopy.get(code) == 0) { //check and therefore get the courses with indg of 0
                queue.add(code); // add them to the queue
            }
        }

        while (!queue.isEmpty()) { // the system goes until the queue is empty
            String current = queue.poll(); // takes the first course from queue
            result.add(current); // ads it to the final valid order

            List<String> neighbors = graph.getAdjacency().get(current); // then we take all the courses that depend on "Current" course-> adjacency=neighbors

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