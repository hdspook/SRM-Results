package com.integrationlabs.results;

public class User {

    public String courseName,semester,credits,marks,grade,result;

    public User() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User(String courseName, String semester, String credits, String marks, String grade, String result) {
        this.courseName = courseName;
        this.semester = semester;
        this.credits = credits;
        this.marks = marks;
        this.grade = grade;
        this.result = result;

    }
}
