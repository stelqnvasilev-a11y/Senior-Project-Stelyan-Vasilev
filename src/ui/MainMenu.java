package ui;

import features.MainBuildingRoomHelp;

import java.util.Scanner;

public class MainMenu {

    private final Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> new MainBuildingRoomHelp(sc).run();
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
}