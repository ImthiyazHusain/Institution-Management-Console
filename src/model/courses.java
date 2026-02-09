package model;

public class courses {
    private int id;
    private String courseName;
    private int fee;

    public courses(int id, String courseName, int fee){
        this.id = id;
        this.courseName = courseName;
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getFee() {
        return fee;
    }
}
