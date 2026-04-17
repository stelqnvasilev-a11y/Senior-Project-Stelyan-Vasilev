package features.courses;

import java.util.*;

public class MajorProgressPlanner {

    private final Scanner sc;

    public MajorProgressPlanner(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        try {
            CourseGraph graph = CourseLoader.loadGraph(
                    "src/data/courses.csv",
                    "src/data/prerequisites.csv",
                    "src/data/conditions.csv",
                    "src/data/majors.csv" );

            System.out.println("... MAJOR PROGRESS AND PLANNING ...");
            System.out.println("Choose major:");
            System.out.println("1) COS");
            System.out.print("Enter option: ");

            String majorChoice = sc.nextLine().trim();
            String selectedMajor;

            if (majorChoice.equals("1")) {
                selectedMajor = "COS";
            } else {
                System.out.println("Invalid major choice.");
                goBack();
                return;
            }

            System.out.println();
            System.out.println("Are you:");
            System.out.println("1) A first-year student with no completed courses");
            System.out.println("2) A student with completed courses");
            System.out.print("Enter option: ");

            String studentType = sc.nextLine().trim();

            if (studentType.equals("1")) {
                showFirstYearPlan(graph, selectedMajor);
            } else if (studentType.equals("2")) {
                showProgressForExistingStudent(graph, selectedMajor);
            } else {
                System.out.println("Invalid option.");
            }

        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        goBack();
    }

    private void showFirstYearPlan(CourseGraph graph, String major) {
        List<String> majorCourses = getCoursesForMajor(graph, major);
        List<String> orderedCourses = KahnsAlgorithm.topologicalSort(graph);

        System.out.println();
        System.out.println("Major: Computer Science");
        System.out.println("Total major credits required: 36");
        System.out.println("Required courses: 21 credit hours");
        System.out.println("Electives: 15 credit hours");
        System.out.println();

        System.out.println("Required courses for the major:");
        printRequiredCourses(graph);

        System.out.println();
        System.out.println("Elective courses / concentration options:");
        printElectiveInfo();

        System.out.println();
        System.out.println("Recommended starting courses:");

        for (String code : orderedCourses) {
            if (majorCourses.contains(code) && hasNoPrerequisites(graph, code)) {
                Course c = graph.getCourses().get(code);
                if (c != null) {
                    System.out.println("- " + c.getCode() + " - " + c.getName());
                }
            }
        }
    }

    private void showProgressForExistingStudent(CourseGraph graph, String major) {
        System.out.print("Enter completed courses separated by commas: ");
        String input = sc.nextLine().trim().toUpperCase();

        Set<String> completed = new HashSet<>();

        if (!input.isEmpty()) {
            String[] parts = input.split(",");
            for (String part : parts) {
                completed.add(part.trim());
            }
        }

        List<String> majorCourses = getCoursesForMajor(graph, major);
        List<String> remainingRequired = new ArrayList<>();

        for (String code : getRequiredCourses()) {
            if (!completed.contains(code)) {
                remainingRequired.add(code);
            }
        }

        int completedRequiredCredits = (getRequiredCourses().size() - remainingRequired.size()) * 3;
        int completedMajorCredits = countCompletedMajorCredits(majorCourses, completed);

        System.out.println();
        System.out.println("Major: Computer Science");
        System.out.println("Total major credits required: 36");
        System.out.println("Required course credits completed: " + completedRequiredCredits + " / 21");
        System.out.println("Total major credits completed: " + completedMajorCredits + " / 36");
        System.out.println();

        System.out.println("Remaining required courses:");
        if (remainingRequired.isEmpty()) {
            System.out.println("All required courses are completed.");
        } else {
            for (String code : remainingRequired) {
                Course c = graph.getCourses().get(code);
                if (c != null) {
                    System.out.println("- " + c.getCode() + " - " + c.getName());
                } else {
                    System.out.println("- " + code);
                }
            }
        }

        System.out.println();
        System.out.println("Courses you may be able to take next:");

        List<String> nextCourses = findAvailableNextCourses(graph, majorCourses, completed);

        if (nextCourses.isEmpty()) {
            System.out.println("No available next courses found.");
        } else {
            for (String code : nextCourses) {
                Course c = graph.getCourses().get(code);
                if (c != null) {
                    System.out.println("- " + c.getCode() + " - " + c.getName());
                } else {
                    System.out.println("- " + code);
                }
            }
        }
    }

    private List<String> getCoursesForMajor(CourseGraph graph, String major) {
        List<String> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : graph.getMajors().entrySet()) {
            String courseCode = entry.getKey();
            List<String> majors = entry.getValue();

            if (majors.contains(major)) {
                result.add(courseCode);
            }
        }

        return result;
    }

    private List<String> getRequiredCourses() {
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

    private void printRequiredCourses(CourseGraph graph) {
        for (String code : getRequiredCourses()) {
            Course c = graph.getCourses().get(code);
            if (c != null) {
                System.out.println("- " + c.getCode() + " - " + c.getName());
            } else {
                System.out.println("- " + code);
            }
        }
    }

    private void printElectiveInfo() {
        System.out.println("Foundations of Computing Concentration:");
        System.out.println("- 15 credit hours chosen from the approved list");
        System.out.println("Examples: COS3031, COS4040, COS4060, COS4070, COS4081, INF2080");

        System.out.println();
        System.out.println("Software Development Concentration:");
        System.out.println("- 12 credit hours chosen from the software list");
        System.out.println("- 3 credit hours chosen from the business/entrepreneurship list");
        System.out.println("Examples: COS2031, COS3040, INF1030, INF2080, INF3035, INF3075, BUS2060, ENT3000");
    }

    private boolean hasNoPrerequisites(CourseGraph graph, String courseCode) {
        return CoursePrerequisiteFinder.getAllPrerequisites(graph, courseCode).isEmpty();
    }

    private int countCompletedMajorCredits(List<String> majorCourses, Set<String> completed) {
        int count = 0;

        for (String course : majorCourses) {
            if (completed.contains(course)) {
                count += 3;
            }
        }

        return count;
    }

    private List<String> findAvailableNextCourses(CourseGraph graph, List<String> majorCourses, Set<String> completed) {
        List<String> result = new ArrayList<>();

        for (String courseCode : majorCourses) {
            if (completed.contains(courseCode)) {
                continue;
            }

            List<String> prereqs = CoursePrerequisiteFinder.getAllPrerequisites(graph, courseCode);

            if (completed.containsAll(prereqs)) {
                result.add(courseCode);
            }
        }

        return result;
    }

    private void goBack() {
        System.out.println();
        System.out.print("Press ENTER to go back to the menu...");
        sc.nextLine();
    }
}