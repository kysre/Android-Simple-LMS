package com.example.androidsimplelms.Model;

import java.util.HashMap;

public class Homework {
    private String name;
    private String question;
    private Course homeworkCourse;
    private HashMap<Student, String> studentAnswers;
    private HashMap<Student, Float> studentMarks;

    public Homework(String name, String question, Course homeworkCourse) {
        this.name = name;
        this.question = question;
        this.homeworkCourse = homeworkCourse;
        this.studentAnswers = new HashMap<>();
        this.studentMarks = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getQuestion() {
        return question;
    }

    public Float getStudentMark(Student student) {
        return studentMarks.get(student);
    }

    public String getStudentAnswer(Student student){
        return studentAnswers.get(student);
    }

    public void setStudentMark(Student student, float mark) {
        studentMarks.put(student, mark);
    }

    public void setStudentAnswers(HashMap<Student, String> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public void setStudentAnswer(Student student, String answer) {
        studentAnswers.put(student, answer);
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public HashMap<Student, String> getStudentAnswers() {
        return studentAnswers;
    }


    public HashMap<Student, Float> getStudentMarks() {
        return studentMarks;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Course getHomeworkCourse() {
        return homeworkCourse;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "name='" + name + '\'' +
                ", question='" + question + '\'' +
                ", homeworkCourse=" + homeworkCourse +
                ", studentAnswers=" + studentAnswers +
                ", studentMarks=" + studentMarks +
                '}';
    }

    public void setHomeworkCourse(Course homeworkCourse) {
        this.homeworkCourse = homeworkCourse;
    }
}
