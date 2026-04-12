package features;

import java.util.List;
import java.util.Scanner;

public class MainBuildingRoomHelp {

    private final Scanner sc;

    public MainBuildingRoomHelp(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        System.out.println(" Main Building Navigation ");
        System.out.println("1. Find route from Entrance to a room");
        System.out.println("2. Find route from one room to another room");
        System.out.print("Choose option: ");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1":
                routeFromEntrance();
                break;
            case "2":
                routeBetweenRooms();
                break;
            default:
                System.out.println("Invalid option.");
                System.out.println();
                System.out.print("Press ENTER to go back to the menu...");
                sc.nextLine();
        }
    }

    private void routeFromEntrance() {
        System.out.print("Which room are you looking for? ");
        String input = sc.nextLine().trim().toUpperCase();

        if (input.isEmpty()) {
            System.out.println("Room cannot be empty.");
            goBack();
            return;
        }

        List<String> steps = RoomPathFinder.findFastestRouteInMainBuilding(input);

        printSteps(steps);
        goBack();
    }

    private void routeBetweenRooms() {
        System.out.print("Enter the starting room: ");
        String startRoom = sc.nextLine().trim().toUpperCase();

        System.out.print("Enter the destination room: ");
        String endRoom = sc.nextLine().trim().toUpperCase();

        if (startRoom.isEmpty() || endRoom.isEmpty()) {
            System.out.println("Both rooms must be entered.");
            goBack();
            return;
        }

        List<String> steps = RoomPathFinder.findRouteBetweenRooms(startRoom, endRoom);

        printSteps(steps);
        goBack();
    }

    private void printSteps(List<String> steps) {
        System.out.println();

        for (String step : steps) {
            System.out.println(step);
        }
    }

    private void goBack() {
        System.out.println();
        System.out.print("Press ENTER to go back to the menu...");
        sc.nextLine();
    }
}