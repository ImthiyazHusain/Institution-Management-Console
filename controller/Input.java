package controller;

import java.util.Scanner;
import static Util.colors.*;
public class Input {
    static Scanner sc = new Scanner(System.in);
    public static int getInt(){
        int n = 0;
        while(true) {
            try {
                n = sc.nextInt();
                sc.nextLine();
                break;
            } catch (Exception e) {
                sc.nextLine();
                System.out.print(RED+"Error:"+e+RESET+"\nTry Again : ");
            }
        }
        return n;
    }
    public static String getString(){
        String str;
        while(true) {
            try {
                str = sc.nextLine();
                break;
            } catch (Exception e) {
                sc.nextLine();
                System.out.print(RED+"Error:"+e+RESET+"\nTry Again : ");
            }
        }
        return str;
    }
}
