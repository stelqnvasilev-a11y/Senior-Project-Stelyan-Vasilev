package features;

import java.util.Scanner;
public class RoomSelector {

    public static String getFloorRoomNumber(String input) {
        String room = input.trim();

        if (!room.matches("\\d{1,4}[A-Za-z]?")) {
            return "Invalid format of the room, it can only consist of up to 4 digits and 1 letter max";
        } else if (room.contentEquals("Red_Room")) {
            return "The Red Room is at the 4th floor";
        }
        int floor;
        //If the room number is 1 digit ( or if it has a letter (like our 4a and 4b))//
        if (room.length() == 1 || (room.length() == 2 && Character.isLetter(room.charAt(1)))) {
            floor = 1;
        } else {
            floor = Character.getNumericValue(room.charAt(0));
        }
        return "Room " + room + " is on Floor " + floor;
    }
}