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
        try {
            CourseGraph graph = CourseLoader.loadGraph(
                    "src/data/courses.csv",
                    "src/data/prerequisites.csv",
                    "src/data/conditions.csv"
            );

            List<String> order = KahnsAlgorithm.topologicalSort(graph);

            System.out.println("... COURSE PLAN ...");
            System.out.println("Valid order of courses:");
            System.out.println();

            String previousLevel = "";

            for (int i = 0; i < order.size(); i++) {

                String code = order.get(i);
                Course course = graph.getCourses().get(code);

                // determine level (1xxx, 2xxx, etc.)
                String level = "";
                if (code.length() >= 4) {
                    char year = code.charAt(3);
                    switch (year) {
                        case '1' -> level = "FOUNDATION";
                        case '2' -> level = "INTERMEDIATE";
                        case '3' -> level = "ADVANCED";
                        case '4' -> level = "FINAL";
                    }
                }

                // print level title only when it changes
                if (!level.equals(previousLevel)) {
                    System.out.println("[" + level + "]");
                    previousLevel = level;
                }

                if (course != null) {
                    System.out.print("- " + course.getCode() + " - " + course.getName());

                    List<String> conditions = graph.getConditions().get(code);
                    if (conditions != null && !conditions.isEmpty()) {
                        System.out.print(" (Condition: " + String.join(", ", conditions) + ")");
                    }

                    System.out.println();
                } else {
                    System.out.println("- " + code);
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