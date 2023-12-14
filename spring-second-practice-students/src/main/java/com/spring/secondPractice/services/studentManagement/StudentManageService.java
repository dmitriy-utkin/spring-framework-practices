package com.spring.secondPractice.services.studentManagement;

import org.springframework.shell.standard.ShellMethod;

public interface StudentManageService {

    int uploadStudents();
    String addStudent(String firstName, String lastName, String age);
    @ShellMethod(key = "rmById")
    String removeStudentById(int id);
    String removeAllStudents();
    String printStudentList();

}
