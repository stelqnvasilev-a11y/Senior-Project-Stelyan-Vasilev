package features.courses;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//this class will load courses and prerequisites from the csv files
public class CourseLoader {
    public static CourseGraph loadGraph(String coursesFile, String prerequisitesFile, String conditionsfile, String majorsFile) throws IOException{
        CourseGraph graph = new CourseGraph();
        loadCourses(graph, coursesFile);
        loadPrerequisites(graph, prerequisitesFile);
        loadConditions(graph, conditionsfile);
        loadMajors(graph, majorsFile);
        return graph;

    }
    private static void loadCourses(CourseGraph graph, String coursesFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(coursesFile));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",", 2); //in course.csv we have only name and code thats why i have 2 parts
            String code = parts[0].trim();
            String name = parts[1].trim();
            Course course = new Course(code, name);
            graph.addCourse(course);
        }
    }
    private static void loadPrerequisites(CourseGraph graph, String prerequisitesFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(prerequisitesFile));

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 3);
            String courseCode = parts[0].trim();
            String type = parts[1].trim();
            String values = "";
            if (parts.length == 3) {
                values = parts[2].trim();
            }
            if (type.equalsIgnoreCase("NONE") || values.isEmpty()) {
                continue;
            }

            String[] prerequisites = values.split("\\|");
            for (String prereq : prerequisites) {
                String p = prereq.trim();

                //
                if (graph.getCourses().containsKey(p)) {
                    graph.addDependency(p, courseCode);
                }
            }
        }
    }
    private static void loadConditions(CourseGraph graph, String conditionsFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(conditionsFile));

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 2);

            String courseCode = parts[0].trim();
            String condition = parts[1].trim();

            graph.addCondition(courseCode, condition);
        }
    }
    private static void loadMajors(CourseGraph graph, String majorsFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(majorsFile));

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 2);

            String major = parts[0].trim();
            String courseCode = parts[1].trim();

            graph.addMajor(courseCode, major);
        }
    }
}
