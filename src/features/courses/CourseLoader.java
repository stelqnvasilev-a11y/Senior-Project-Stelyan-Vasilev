package features.courses;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//this class will load courses and prerequisites from the csv files
public class CourseLoader {
    public static CourseGraph loadGraph(String coursesFile, String prerequisitesFile) throws IOException{
        CourseGraph graph = new CourseGraph();
        loadCourses(graph, coursesFile);
        loadPrerequisites(graph, prerequisitesFile);
        return Graph;

    }
    private static void loadCourses(CourseGraph graph, String coursesFile) throws IOException{
        List<String>lines = Files.readAllLines(Paths.get(coursesFile));
        for (int i= 1; i < lines.size; i++){
        String line = lines.get(i);
        }
    }

}
