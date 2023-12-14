package com.spring.secondPractice.repositories;

import com.spring.secondPractice.model.Student;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class StudentRepositoryImpl implements StudentRepository {

    private List<Student> students;

    public StudentRepositoryImpl() {
        this.students = new ArrayList<>();
    }

    @Override
    public int save(Student student) {
        Student studentWithMaxId = students.stream().max(Comparator.comparingInt(Student::getId)).orElse(null);
        int id = studentWithMaxId == null ? 1 : studentWithMaxId.getId() + 1;
        student.setId(id);
        students.add(student);
        return id;
    }

    @Override
    public int count() {
        return students.size();
    }

    @Override
    public void saveAll(List<Student> collection) {
        students.addAll(collection);
    }

    @Override
    public List<Student> findAll() {
        return students;
    }

    @Override
    public boolean removeById(int id) {
        Student student = students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
        if (student == null) {
            return false;
        }
        students.remove(student);
        return true;
    }

    @Override
    public void removeAll() {
        students.clear();
    }
}
