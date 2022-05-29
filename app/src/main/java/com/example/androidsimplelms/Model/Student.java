package com.example.androidsimplelms.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Student extends User {
    private static ArrayList<Student> students = new ArrayList<>();
    private String studentId;

    public Student(String firstname, String lastname, String username, String password, String studentId) {
        super(firstname, lastname, username, password);
        this.studentId = studentId;
        students.add(this);

    }

    public static void addToStudents(Student student) {
        students.add(student);
    }

    public static void setStudents(ArrayList<Student> students) {
        Student.students = students;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean joinCourse(int courseId) {
        Course courseToJoin = Course.getCourseById(courseId);
        if (courseToJoin == null) {
            return false;
        }
        for (Course course : getCourses()) {
            if (course.getId() == courseId) {
                return false;
            }
        }
        this.addCourse(courseToJoin);
        return true;
    }

    public boolean joinCourse(String courseName) {
        Course courseToJoin = Course.getCourseByName(courseName);
        if (courseToJoin == null) {
            return false;
        }
        for (Course course : getCourses()) {
            if (course.getName().equals(courseName)) {
                return false;
            }
        }
        this.addCourse(courseToJoin);
        return true;
    }

    @Override
    public void addCourse(Course course) {
        super.addCourse(course);
        course.addStudent(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getUsername().equals(student.getUsername());
    }
}
