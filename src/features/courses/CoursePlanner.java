package features.courses;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * This class handles the course planner functionality.
 * It loads the course graph and displays a valid order
 * only for the required courses of the selected major.
 */
public class CoursePlanner {

    private final Scanner sc;

    public CoursePlanner(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        try {
            System.out.println("Choose major:");
            System.out.println("1) COS");
            System.out.println("2) INF");
            System.out.print("Enter option: ");

            String choice = sc.nextLine().trim();

            List<String> requiredCourses;
            String majorName;

            if (choice.equals("1")) {
                majorName = "COS";
                requiredCourses = getRequiredCoursesForCOS();
            } else if (choice.equals("2")) {
                majorName = "INF";
                requiredCourses = getRequiredCoursesForINF();
            } else {
                System.out.println("Invalid major choice.");
                System.out.println();
                System.out.print("Press ENTER to go back to the menu...");
                sc.nextLine();
                return;
            }

            CourseGraph graph = CourseLoader.loadGraph(
                    "src/data/courses.csv",
                    "src/data/prerequisites.csv",
                    "src/data/conditions.csv",
                    "src/data/majors.csv"
            );

            List<String> order = KahnsAlgorithm.topologicalSort(graph);

            System.out.println();
            System.out.println("... COURSE PLAN FOR " + majorName + " ...");
            System.out.println("Valid order of required courses:");
            System.out.println();

            String previousLevel = "";

            for (String code : order) {

                if (!requiredCourses.contains(code)) {
                    continue;
                }

                Course course = graph.getCourses().get(code);

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

    private List<String> getRequiredCoursesForCOS() {
        List<String> required = new ArrayList<>();
        required.add("COS1020");
        required.add("COS1050");
        required.add("COS2021");
        required.add("COS2030");
        required.add("COS2035");
        required.add("COS3015");
        required.add("COS4091");
        return required;
    }

    private List<String> getRequiredCoursesForINF() {
        List<String> required = new ArrayList<>();
        required.add("INF1050");
        required.add("INF2040");
        required.add("INF2070");
        required.add("INF2080");
        required.add("INF3035");
        required.add("INF3070");
        required.add("INF3075");
        required.add("INF4040");
        required.add("INF4080");
        required.add("INF4081");
        required.add("INF4091");
        return required;
    }
}