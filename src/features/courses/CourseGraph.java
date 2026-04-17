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
    private final Map<String, List<String>> adjacency = new HashMap<>(); // Adjacency list: prerequisite - list of courses depending on it
    private final Map<String, Integer> indegree = new HashMap<>();// this stores how many prerequisites each course has
    private final Map<String, List<String>> majors = new HashMap<>();

    private final Map<String, List<String>> conditions = new HashMap<>(); //stores the conditions for the courses such as Senior Standing
    public void addCourse(Course course) {
        courses.putIfAbsent(course.getCode(), course);
        adjacency.putIfAbsent(course.getCode(), new ArrayList<>());
        indegree.putIfAbsent(course.getCode(), 0); // Adds a course to the graph
        conditions.putIfAbsent(course.getCode(), new ArrayList<>());
        majors.putIfAbsent(course.getCode(), new ArrayList<>());

}
    /*
     * Adds a dependency:
     * prerequisite -> course
     */
    public void addDependency(String prerequisite, String courseCode) { // the prerequisite always points at the course

        adjacency.putIfAbsent(prerequisite, new ArrayList<>());// if not present in the graph
        adjacency.putIfAbsent(courseCode, new ArrayList<>()); // if the course is not present in the graph

        indegree.putIfAbsent(prerequisite, 0);
        indegree.putIfAbsent(courseCode, 0);

        adjacency.get(prerequisite).add(courseCode); // adds the prerequisite adjacency to its course

        // Increase indegree of the dependent course
        indegree.put(courseCode, indegree.get(courseCode) + 1);
    }
    public void addCondition(String courseCode, String condition) {
        conditions.putIfAbsent(courseCode, new ArrayList<>());
        conditions.get(courseCode).add(condition);
    }
    public void addMajor(String courseCode, String major) {
        majors.putIfAbsent(courseCode, new ArrayList<>());
        majors.get(courseCode).add(major);
    }


    //getters for other files to be able to use the data from Graph.java

    public Map<String, Course> getCourses() {
        return courses;
    }

    public Map<String, List<String>> getAdjacency() {
        return adjacency;
    }

    public Map<String, Integer> getIndegree() {
        return indegree;
    }

    public Map<String, List<String>> getConditions() {
        return conditions;
    }

    public Map<String, List<String>> getMajors() {
        return majors;
    }
}
