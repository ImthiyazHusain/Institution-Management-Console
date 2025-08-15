package view;

import static Util.Input.*;
import static Util.colors.*;

public class viewStudent {
    public static String getStudentName() {
        System.out.print("Enter your Name : ");
        return getString();
    }

    public static String getStudentPass() {
        System.out.print("Enter your Password : ");
        return getString();
    }

    public static int getOption() {
        System.out.print(BRIGHT_CYAN + "\n1. Attendance Details \n2. Mark Details"+"\n3. Change Password "+"\n4. DisplayMarks "+"\n5. Personal BOT"+ RED + "\n6. Exit" + RESET + "\n\nEnter Your Choice : ");
        int opt = getInt();
        while (opt <= 0 || opt > 6) {
            System.out.print(RED + "Invalid Option!!!❌\nTry Again : " + RESET);
            opt = getInt();
        }
        return opt;
    }

    public static void greet(String name) {
        viewAdmin.updateStatus(BRIGHT_GREEN,"Welcome "+name+" 👋",RESET);
    }

    public static void printRow(String[] row, int[] colWidths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < row.length; i++) {
            sb.append(" ").append(String.format("%-" + colWidths[i] + "s", row[i])).append(" |");
        }
        System.out.println(sb);
    }
}
