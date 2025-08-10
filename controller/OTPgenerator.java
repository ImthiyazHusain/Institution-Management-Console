package controller;

import java.util.Random;

public class OTPgenerator {
    public static String generateOTP() {
        int length = 6;
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return otp.toString();
    }
}
