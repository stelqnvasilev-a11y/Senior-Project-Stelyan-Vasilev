package features.courses;

import java.util.*;

public class CoursePrerequisiteFinder {


    //Returns all prerequisites for a given course using DFS
     //using sets because we dont want any duplicates and LinkedHashSet because we care for the order
    public static List<String> getAllPrerequisites(CourseGraph graph, String courseCode) {

        Set<String> visited = new LinkedHashSet<>(); // if two courses has the same prerequisite it wont be added twice

        dfs(courseCode, graph, visited);// here we start the dfs- start from the user's course code and dfs check what's before

        // remove the course itself (we only want prerequisites in the resulted list)
        visited.remove(courseCode);

        return new ArrayList<>(visited);
    }
     //DFS traversal
    private static void dfs(String course, CourseGraph graph, Set<String> visited) {

        if (visited.contains(course)) { //if the dfs has already been in this course - stop, do not visit it again
            return;
        }

        visited.add(course); //mark the course as visited

        // get prerequisites of current course
        List<String> prereqs = getPrerequisites(graph, course);

        for (String p : prereqs) {
            dfs(p, graph, visited);
        }
    }

    /*
     * Helper method to find prerequisites of a course
     */
    private static List<String> getPrerequisites(CourseGraph graph, String course) {

        List<String> result = new ArrayList<>();

        // for each course, store the courses that depend on it
        for (Map.Entry<String, List<String>> entry : graph.getAdjacency().entrySet()) {

            String prereq = entry.getKey();
            List<String> dependents = entry.getValue();

            // if this course depends on "prereq"
            if (dependents.contains(course)) {
                result.add(prereq);
            }
        }

        return result;
    }
}