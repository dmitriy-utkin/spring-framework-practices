package com.spring.secondPractice.repositories;

import com.spring.secondPractice.model.Student;

import java.util.List;

public interface StudentRepository {
    int save(Student student);
    int count();
    void saveAll(List<Student> collection);
    List<Student> findAll();
    boolean removeById(int id);
    void removeAll();
}
