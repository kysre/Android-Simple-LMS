package com.example.androidsimplelms.Model;

import java.util.ArrayList;

public class Course {
    private static ArrayList<Course> courses;
    private static int recentId = 50000;
    private final String name;
    private int id;
    private final Professor professor;
    private ArrayList<Homework> homeworks;
    private final ArrayList<Student> students;

    static {
        courses = new ArrayList<>();
    }

    public Course(String name, Professor professor) {
        this.name = name;
        this.professor = professor;
        this.id = recentId;
        courses.add(this);
        recentId += 1;
        homeworks = new ArrayList<>();
        students = new ArrayList<>();
    }

    public static Course getCourseByName(String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

    public static Course getCourseById(int courseId) {
        for (Course course : courses) {
            if (course.getId() == courseId) {
                return course;
            }
        }
        return null;
    }

    public static ArrayList<Course> getAllCourses() {
        return courses;
    }

    public boolean doesHomeworkExist(String name) {
        for (Homework homework : homeworks) {
            if (homework.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void addHomework(String name, String question) {
        homeworks.add(new Homework(name, question, this));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;

    }

    public Professor getProfessor() {
        return professor;
    }

    public ArrayList<Homework> getHomeworks() {
        return homeworks;
    }

    public Homework getHomeworkByName(String homeworkName) {
        for (Homework homework : homeworks) {
            if (homework.getName().equals(homeworkName)) {
                return homework;
            }
        }
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public ArrayList<Student> getCourseStudents() {
        return students;
    }

    public static void addToCourses(Course course) {
        courses.add(course);
    }
}
