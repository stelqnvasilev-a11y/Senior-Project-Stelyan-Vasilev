package features.courses;

import java.util.List;
import java.util.Scanner;

/*
 * This class handles the course planner functionality.
 * It loads the course graph and displays a valid course order.
 */
public class CoursePlanner {

    private final Scanner sc;

    public CoursePlanner(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        System.out.println("... COURSE PLANNER ...");
        System.out.println("Generating a valid course order...");
        System.out.println();

        try {
            CourseGraph graph = CourseLoader.loadGraph(
                    "src/data/courses.csv",
                    "src/data/prerequisites.csv",
                    "src/data/conditions.csv"
            );

            List<String> order = KahnsAlgorithm.topologicalSort(graph);

            for (int i = 0; i < order.size(); i++) {
                String code = order.get(i);
                Course course = graph.getCourses().get(code);

                if (course != null) {
                    System.out.print((i + 1) + ". " + course.getCode() + " - " + course.getName());

                    // show conditions if they exist
                    List<String> conditions = graph.getConditions().get(code);
                    if (conditions != null && !conditions.isEmpty()) {
                        System.out.print(" (Condition: " + String.join(", ", conditions) + ")");
                    }

                    System.out.println();
                } else {
                    System.out.println((i + 1) + ". " + code);
                }
            }

        } catch (Exception e) {
            System.out.println("Error while loading course data: " + e.getMessage());
        }

        System.out.println();
        System.out.print("Press ENTER to go back to the menu...");
        sc.nextLine();
    }
}