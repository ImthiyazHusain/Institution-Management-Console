package Service;

import view.viewAdmin;

public class verifyEmail {
    public static String verifyEmailWithOTP() {
        while (true) {
            String email = viewAdmin.getMail();
            String otp = OTPgenerator.generateOTP();
            EmailSender.sendEmail(email, "Verification", "Your OTP is: " + otp,true);
            if (viewAdmin.verifyOtp(otp)) {
                viewAdmin.mailVerified();
                return email;
            }
        }
    }
}
