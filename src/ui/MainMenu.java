package ui;

import features.MainBuildingRoomHelp;
import features.courses.CoursePlanner;
import features.courses.CoursePrerequisiteFinder;
import features.courses.CourseGraph;
import features.courses.CourseLoader;
import features.courses.MajorProgressPlanner;

import java.util.List;

import java.util.Scanner;

public class MainMenu {

    private final Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> new MainBuildingRoomHelp(sc).run();

                case 2 -> new CoursePlanner(sc).run();

                case 3 -> showPrerequisites();

                case 4 -> new MajorProgressPlanner(sc).run();

                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option. Try again.");
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("... UNIVERSITY GUIDE ...");
        System.out.println("1) I need help to get to my room in Main Building");
        System.out.println("2) Show valid course order");
        System.out.println("3) Show prerequisites for a course");
        System.out.println("4) Show major progress and planning");
        System.out.println("0) Exit");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private void showPrerequisites() {
        try {
            System.out.print("Enter course: ");
            String course = sc.nextLine().trim().toUpperCase();

            if (course.isEmpty()) {
                System.out.println("Course cannot be empty.");
                return;
            }

            CourseGraph graph = CourseLoader.loadGraph(
                    "src/data/courses.csv",
                    "src/data/prerequisites.csv",
                    "src/data/conditions.csv",
                    "scr/data/majors.csv"
            );

            List<String> prereqs =
                    CoursePrerequisiteFinder.getAllPrerequisites(graph, course);

            System.out.println();
            System.out.println("Prerequisites for " + course + ":");

            if (prereqs.isEmpty()) {
                System.out.println("No prerequisites found.");
            } else {
                for (String p : prereqs) {
                    System.out.println("- " + p);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        System.out.println();
        System.out.print("Press ENTER to go back...");
        sc.nextLine();
    }
}