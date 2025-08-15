package controller;

import DOA.studentDOA;
import static Util.utilities.*;

import DOA.teacherDOA;
import org.json.JSONObject;
import view.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Util.Input.*;
import static Util.colors.*;

public class studentController {
    public static String name;
    public static boolean verify() {
        name = viewStudent.getStudentName();
        String pass = viewStudent.getStudentPass();
        return validateStudent(name,pass);
    }

    public static boolean validateStudent(String name,String pass){
        return studentDOA.getPass(name).equals(pass);
    }

    public static int displayOptions() throws SQLException, IOException {
        int option = viewStudent.getOption();
        if(option==1) { attendanceDetailsOption(); return 1;}
        else if(option==2){  markDetailsOption(); return 1;}
        else if(option==3){  updatePassOption();return 1;}
        else if(option==4){  chatWithBot(); return 1;}
        else return 0;
    }

    static List<String[]> history = new ArrayList<>();
    private static void chatWithBot() throws IOException {
        viewStudent.typePrint("🤖 Iam your personal chatBot you can fell free to discuss anything 'Note' : I can not remember Old Conversation, So your discussion will nor be stored in any dataBase. \nType 'exit' to end the conversation",GREEN);
        while (true) {
            viewStudent.display("You : ");
            String input = getString();
            if (input.equalsIgnoreCase("exit")) break;

            String reply = getGeminiReply(input);
            viewStudent.display("BOT : ");
            viewStudent.typePrint(reply,CYAN);
        }
    }
    public static String getGeminiReply(String prompt) throws IOException {
        history.add(new String[]{"user", prompt});

        StringBuilder contentJson = new StringBuilder("\"contents\": [");
        for (String[] turn : history) {
            contentJson.append("{\"role\": \"")
                    .append(turn[0])
                    .append("\", \"parts\": [{\"text\": \"")
                    .append(turn[1].replace("\"", "\\\""))
                    .append("\"}]},");
        }
        contentJson.deleteCharAt(contentJson.length() - 1); // remove trailing comma
        contentJson.append("]");

        URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = "{" + contentJson.toString() + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = conn.getResponseCode();
        InputStream stream = (status == 200) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line.trim());
        }
        reader.close();

        try {
            JSONObject obj = new JSONObject(response.toString());
            String reply = obj.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text").trim();

            history.add(new String[]{"model", reply}); // Add reply to history
            return reply;
        } catch (Exception e) {
            return "❌ Couldn't parse reply. Full response:\n" + response;
        }
    }
    private static void attendanceDetailsOption() {
        studentDOA.attendanceView(name);
    }
    private static void markDetailsOption() {
        studentDOA.markView(name);
    }

    public static void greet() {
        viewStudent.greet(name);
    }
    public static void updatePassOption(){

        String newPass = viewStudent.getNewPass();
        if(studentDOA.updatePass(name,newPass)){
            viewTeacher.updateStatus(GREEN,"✅Your Password Updated",RESET);
        }else{
            viewTeacher.updateStatus(RED,"❌Error!!!",RESET);
        }
    }
}
