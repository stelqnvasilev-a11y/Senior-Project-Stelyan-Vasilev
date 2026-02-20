package features;

import java.util.List;
import java.util.Scanner;

public class MainBuildingRoomHelp {

    private final Scanner sc;

    public MainBuildingRoomHelp(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        System.out.println(" Main Building: Room Navigation ");

        // 1) Read room input
        System.out.print("Which room are you looking for? ");
        String input = sc.nextLine();

        // 2) Validate input format using RoomSelector
        String validation = RoomSelector.getFloorRoomNumber(input);
        if (validation.toLowerCase().startsWith("invalid")) {
            System.out.println(validation);
            System.out.println();
            System.out.print("Press ENTER to go back to the menu...");
            sc.nextLine();
            return;
        }

        // 3) Normalize room code (e.g., "4b" -> "4B")
        String roomCode = input.trim().toUpperCase();

        // 4) Call the A* pathfinder
        List<String> steps = RoomPathFinder.findFastestRouteInMainBuilding(roomCode);

        // 5) Print the result
        for (String line : steps) {
            System.out.println(line);
        }

        // 6) Back to menu
        System.out.println();
        System.out.print("Press ENTER to go back to the menu...");
        sc.nextLine();
    }
}