package model;

public class student {
    private String name;
    private int age;
    private int hscMark;
    private courses course;
    private String email;

    public student (String name, int age, int hscMark, courses course){
        this.name = name;
        this.age = age;
        this.hscMark = hscMark;
        this.course = course;
    }

    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public int getHscMark(){
        return hscMark;
    }
    public courses getCourse(){
        return course;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", hscMark=" + hscMark +
                ", course=" + course +
                ", Email=" + email +
                '}';
    }
}
