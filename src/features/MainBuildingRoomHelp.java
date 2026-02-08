package features;

import java.util.Scanner;

public class MainBuildingRoomHelp {

    private final Scanner sc;

    public MainBuildingRoomHelp(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        System.out.println("... Main Building: Room Navigation ...");

        System.out.println("Which room are you looking for? ");
        String input = sc.nextLine();
        String result = RoomSelector.getFloorRoomNumber(input);
        System.out.println(result);
        System.out.print("Press ENTER to go back to the menu...");
        sc.nextLine();
    }
}