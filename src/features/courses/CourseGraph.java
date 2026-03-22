package features.courses;
import java.util.*;
/*
 * This class represents the graph of courses and their prerequisites.
 * Nodes = courses
 * Edges = prerequisite relationships
 */
public class CourseGraph {
    // Stores all courses by code
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, List<String>> adjacency = new HashMap<>(); // Adjacency list: prerequisite -> list of courses depending on it
    private final Map<String, Integer> indegree = new HashMap<>(); // this stores how many prerequisites each course has
    public void addCourse(Course course) {
        courses.putIfAbsent(course.getCode(), course);
        adjacency.putIfAbsent(course.getCode(), new ArrayList<>());
        indegree.putIfAbsent(course.getCode(), 0); // Adds a course to the graph

}
    /*
     * Adds a dependency:
     * prerequisite -> course
     */
    public void addDependency(String prerequisite, String courseCode) {

        adjacency.putIfAbsent(prerequisite, new ArrayList<>());
        adjacency.putIfAbsent(courseCode, new ArrayList<>());

        indegree.putIfAbsent(prerequisite, 0);
        indegree.putIfAbsent(courseCode, 0);

        adjacency.get(prerequisite).add(courseCode);

        // Increase indegree of the dependent course
        indegree.put(courseCode, indegree.get(courseCode) + 1);
    }

    public Map<String, Course> getCourses() {
        return courses;
    }

    public Map<String, List<String>> getAdjacency() {
        return adjacency;
    }

    public Map<String, Integer> getIndegree() {
        return indegree;
    }
}
