package com.example.androidsimplelms.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.androidsimplelms.Model.Course;
import com.example.androidsimplelms.Model.Homework;
import com.example.androidsimplelms.Model.Professor;
import com.example.androidsimplelms.Model.Student;
import com.example.androidsimplelms.Model.User;
import com.example.androidsimplelms.RecyclerViewAdapter;

public class Controller extends Activity {

    private static User onlineUser;

    public static ArrayList<User> addProfessor(String firstName, String lastName,
                                               String university, String username, String password) {
        new Professor(firstName, lastName, username, password, university);
        new Controller();
        return User.getUsers();
    }

    public void saveProfessor(String firstName, String lastName,
                              String university, String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.apply();
    }

    public static ArrayList<User> addStudent(String firstName, String lastName, String studentId,
                                             String username, String password) {
        new Student(firstName, lastName, username, password, studentId);
        ArrayList<User> users = User.getUsers();
        return users;
    }

    public static boolean checkUsernameForRegister(String username) {
        return User.getUserByUsername(username) == null;
    }

    public static boolean loginErrorHandler(String username, String password) {
        User user = User.getUserByUsername(username);
        if (user != null) {
            if (user.checkPassword(password)) {
                onlineUser = user;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkForLogin(String username) {
        return Professor.isThereProfessor(username);
    }

    public static ArrayList<RecyclerViewAdapter.ListItem> getCourseListItems() {
        ArrayList<RecyclerViewAdapter.ListItem> courseList = new ArrayList<>();
        for (Course course : onlineUser.getCourses()) {
            courseList.add(new RecyclerViewAdapter.ListItem
                    (course.getName(), String.valueOf(course.getId())));
        }
        return courseList;
    }

    public static ArrayList<RecyclerViewAdapter.ListItem> getNotJoinedCourseListItems() {
        ArrayList<RecyclerViewAdapter.ListItem> courseList = new ArrayList<>();
        ArrayList<Course> allCourses = Course.getAllCourses();
        for (Course course : allCourses) {
            if (!onlineUser.isCourseJoined(course)) {
                courseList.add(new RecyclerViewAdapter.ListItem
                        (course.getName(), String.valueOf(course.getId())));
            }
        }
        return courseList;
    }

    public static boolean isNumeric(String string) {
        if (string == null || string.equals("")) {
            return false;
        }
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    public static boolean joinCourse(int courseId) {
        if (onlineUser instanceof Student) {
            return ((Student) onlineUser).joinCourse(courseId);
        }
        return false;
    }

    public static boolean joinCourse(String courseName) {
        if (onlineUser instanceof Student) {
            return ((Student) onlineUser).joinCourse(courseName);
        }
        return false;
    }

    public static String getCourseProfessorName(String courseName) {
        Professor professor = Course.getCourseByName(courseName).getProfessor();
        return professor.getFirstname() + " " + professor.getLastname();
    }

    public static String getCourseNameById(int courseId) {
        Course course = Course.getCourseById(courseId);
        if (course != null) {
            return course.getName();
        }
        return null;
    }

    public static boolean isCourseJoinedByOnlineUser(String courseName) {
        return onlineUser.isCourseJoined(Course.getCourseByName(courseName));
    }

    public static ArrayList<RecyclerViewAdapter.ListItem> getHomeworkListItems(String courseName) {
        ArrayList<Homework> homeworks = Course.getCourseByName(courseName).getHomeworks();
        ArrayList<RecyclerViewAdapter.ListItem> listItems = new ArrayList<>();
        for (Homework homework : homeworks) {
            listItems.add(new RecyclerViewAdapter.ListItem(homework.getName(), ""));
        }
        return listItems;
    }

    public static String getHomeworkQuestion(String courseName, String homeworkName) {
        Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
        if (homework != null) {
            return homework.getQuestion();
        }
        return null;
    }

    public static String getPreviousAnswer(String courseName, String homeworkName) {
        Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
        if (homework != null) {
            if (onlineUser instanceof Student) {
                return homework.getStudentAnswer((Student) onlineUser);
            }
        }
        return null;
    }

    public static boolean setAnswerForOnlineUser(String courseName, String homeworkName, String answer) {
        Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
        if (onlineUser instanceof Student && homework != null) {
            Student student = (Student) onlineUser;
            homework.setStudentAnswer(student, answer);
            return true;
        }
        return false;
    }

    public static boolean createNewCourse(String courseName) {
        if (onlineUser instanceof Professor) {
            Professor professor = (Professor) onlineUser;
            if (Course.getCourseByName(courseName) == null) {
                Course course = new Course(courseName, professor);
                professor.addCourse(course);
                return true;
            }
        }
        return false;
    }

    public static boolean createHomeworkForCourse(String courseName, String name, String question) {
        Course course = Course.getCourseByName(courseName);
        if (course != null && !course.doesHomeworkExist(name)) {
            course.addHomework(name, question);
            return true;
        }
        return false;
    }

    public static ArrayList<RecyclerViewAdapter.ListItem> getStudentMarksItemList
            (String courseName, String homeworkName) {

        Course course = Course.getCourseByName(courseName);
        Homework homework = course.getHomeworkByName(homeworkName);
        ArrayList<Student> students = course.getCourseStudents();
        HashMap<Student, Float> studentMarksMap = homework.getStudentMarks();
        ArrayList<RecyclerViewAdapter.ListItem> studentMarksItemList = new ArrayList<>();

        for (Student student : students) {
            if (studentMarksMap.containsKey(student)) {
                studentMarksItemList.add(new RecyclerViewAdapter.ListItem
                        (student.getUsername(), String.valueOf(studentMarksMap.get(student))));
            } else {
                studentMarksItemList.add(new RecyclerViewAdapter.ListItem
                        (student.getUsername(), "-"));

            }
        }
        return studentMarksItemList;
    }

    public static boolean renameHomework(String courseName, String homeworkName, String newName) {
        Course course = Course.getCourseByName(courseName);
        Homework homework = course.getHomeworkByName(homeworkName);
        if (!newName.equals("")) {
            if (course.getHomeworkByName(newName) == null) {
                homework.rename(newName);
                return true;
            }
        }
        return false;
    }

    public static String getStudentMark(String courseName, String homeworkName, String studentUsername) {
        Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
        Student student = (Student) Student.getUserByUsername(studentUsername);
        HashMap<Student, Float> studentMarks = homework.getStudentMarks();
        if (studentMarks.containsKey(student)) {
            return String.valueOf(studentMarks.get(student));
        } else {
            return null;
        }
    }

    public static String getOnlineStudentMark(String courseName, String homeworkName) {
        if (onlineUser instanceof Student) {
            Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
            Student student = (Student) onlineUser;
            HashMap<Student, Float> studentMarks = homework.getStudentMarks();
            if (studentMarks.containsKey(student)) {
                return String.valueOf(studentMarks.get(student));
            }
        }
        return null;
    }

    public static String getStudentAnswer(String courseName, String homeworkName, String studentUsername) {
        Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
        Student student = (Student) Student.getUserByUsername(studentUsername);
        HashMap<Student, String> studentAnswers = homework.getStudentAnswers();
        return studentAnswers.getOrDefault(student, "No Answer!");
    }

    public static void setMark(String courseName, String homeworkName, String studentUsername, float mark) {
        Homework homework = Course.getCourseByName(courseName).getHomeworkByName(homeworkName);
        Student student = (Student) Student.getUserByUsername(studentUsername);
        homework.setStudentMark(student, mark);
    }

    public static void initializer(ArrayList<User> users) {
        for (User user : users) {
            User.addToUsers(user);
            if (user instanceof Professor) {
                Professor.addToAllProfessor((Professor) user);
                ArrayList<Course> courses = user.getCourses();
                for (Course course : courses) {
                    Course.addToCourses(course);
                }
            } else {
                Student.addToStudents((Student) user);
            }
        }
    }
}
