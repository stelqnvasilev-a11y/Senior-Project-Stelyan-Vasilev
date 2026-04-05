package features.courses;

import java.util.*;

//
//This class uses Kahn's Algorithm to generate the valid order of courses.

public class KahnsAlgorithm {

    public static List<String> topologicalSort(CourseGraph graph) {

        Map<String, Integer> indegreeCopy = new HashMap<>(); // creates a copy of the indegree map from CourseGraph because Khan will change the values.
        indegreeCopy.putAll(graph.getIndegree());

        // Priority queue: lower-level courses come first
        PriorityQueue<String> queue = new PriorityQueue<>((a, b) -> {
            int levelA = getLevel(a);
            int levelB = getLevel(b);

            if (levelA != levelB) {
                return Integer.compare(levelA, levelB);
            }

            return a.compareTo(b);
        });

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
    private static int getLevel(String code) {
        if (code != null && code.length() >= 4) {
            char year = code.charAt(3);

            if (Character.isDigit(year)) {
                return Character.getNumericValue(year);
            }
        }

        // fallback value in case of unexpected format or unexpected course. This will place it at the end of the ordering.
        return Integer.MAX_VALUE;
    }
}
