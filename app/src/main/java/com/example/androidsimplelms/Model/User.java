package com.example.androidsimplelms.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static final HashMap<String, User> userHashmap = new HashMap<>();
    public static ArrayList<User> users = new ArrayList<>();
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private final ArrayList<Course> courses;

    public User(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.courses = new ArrayList<>();
        userHashmap.put(username, this);
        users.add(this);
    }

    public static User getUserByUsername(String username) {
        return userHashmap.get(username);
    }

    public static HashMap<String, User> getUserHashmap() {
        return userHashmap;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public static void addToUsers(User user) {
        users.add(user);
        userHashmap.put(user.getUsername(), user);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public boolean isCourseJoined(Course testCourse) {
        for (Course course : courses) {
            if (course.getName().equals(testCourse.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", courses=" + courses +
                '}';
    }
}
