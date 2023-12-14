package com.spring.secondPractice.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;

@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"firstName", "lastName"})
public class Student implements Comparable<Student> {

    private int id;
    private String firstName;
    private String lastName;
    private int age;

    @Override
    public String toString() {
        return MessageFormat.format("Student with ID-{0}: {1} {2}, {3} y.o.", id, firstName, lastName, age);
    }

    @Override
    public int compareTo(Student otherStudent) {
        return Integer.compare(this.getId(), otherStudent.getId());
    }
}
