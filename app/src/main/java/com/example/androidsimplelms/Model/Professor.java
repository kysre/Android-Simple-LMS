package com.example.androidsimplelms.Model;

import java.util.ArrayList;

public class Professor extends User {
    private static ArrayList<Professor> allProfessor = new ArrayList<>();
    private String universityName;

    public Professor(String firstname, String lastname, String username, String password
            , String universityName) {
        super(firstname, lastname, username, password);
        this.universityName = universityName;
        allProfessor.add(this);
    }

    public static boolean isThereProfessor(String username) {
        for (int i = 0; i < allProfessor.size(); i++) {
            if (allProfessor.get(i).getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public static ArrayList<Professor> getAllProfessor() {
        return allProfessor;
    }

    public static void setAllProfessor(ArrayList<Professor> allProfessor) {
        Professor.allProfessor = allProfessor;
    }

    public static void addToAllProfessor(Professor professor) {
        allProfessor.add(professor);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "universityName='" + universityName + '\'' +
                '}';
    }
}
