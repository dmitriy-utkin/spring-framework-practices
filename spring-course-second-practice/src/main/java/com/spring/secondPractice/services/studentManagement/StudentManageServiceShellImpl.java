package com.spring.secondPractice.services.studentManagement;

import com.spring.secondPractice.config.InputConfig;
import com.spring.secondPractice.config.StringConfig;
import com.spring.secondPractice.config.ValidationConfig;
import com.spring.secondPractice.events.*;
import com.spring.secondPractice.model.Student;
import com.spring.secondPractice.repositories.StudentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class StudentManageServiceShellImpl implements StudentManageService{

    private final StudentRepository studentRepository;
    private final InputConfig inputConfig;
    private final StringConfig stringConfig;
    private final ValidationConfig validationConfig;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void postConstruct() {
        if (inputConfig.isEnable()) {
            int size = uploadStudents();
            log.info(MessageFormat.format(stringConfig.getUploadingMessageFormat(), size));
        }
    }

    @Override
    @ShellMethod(key = "print", value = "Print saved students")
    public String printStudentList() {
        List<Student> students = studentRepository.findAll();
        if (students.size() == 0) {
            return MessageFormat.format(stringConfig.getEmptyListFormat(), stringConfig.getMessagePrefix());
        }
        Collections.sort(students);
        StringBuilder sb = new StringBuilder();
        students.forEach(student -> sb.append(student).append("\n"));

        applicationEventPublisher.publishEvent(new PrintingEvent(students.size()));

        return sb.toString().trim();
    }

    @Override
    @ShellMethod(key = "add", value = "Add a new students (parameters: firstName lastName age)")
    public String addStudent(@ShellOption(value = "fn") String firstName,
                             @ShellOption(value = "ln") String lastName,
                             @ShellOption(value = "a") String age) {
        Student student = createStudent(firstName, lastName, Integer.parseInt(age));

        Map<Boolean, String> validation = checkIfCorrect(student);

        if (validation.containsKey(false)) {
            return validation.get(false);
        }

        int id = studentRepository.save(student);

        applicationEventPublisher.publishEvent(new AddingEvent(id));

        return MessageFormat.format(stringConfig.getSaveMessageFormat(),
                                    stringConfig.getMessagePrefix(), firstName, lastName);
    }

    @Override
    @ShellMethod(key = "rmById", value = "Remove by student id")
    public String removeStudentById(int id) {
        boolean isSuccess = studentRepository.removeById(id);
        if (!isSuccess) {
            applicationEventPublisher.publishEvent(new RemovingEvent(false, id));
            return MessageFormat.format(stringConfig.getUnsuccessfulStudentRemovingFormat(),
                                        stringConfig.getMessagePrefix(), id);
        }
        applicationEventPublisher.publishEvent(new RemovingEvent(true, id));
        return MessageFormat.format(stringConfig.getSuccessStudentRemovingFormat(),
                                    stringConfig.getMessagePrefix(), id);
    }

    @Override
    @ShellMethod(key = "rmAll", value = "Remove all students")
    public String removeAllStudents() {
        int size = studentRepository.count();
        studentRepository.removeAll();
        applicationEventPublisher.publishEvent(new RemovingAllEvent(size));
        return MessageFormat.format(stringConfig.getRemovingAllStudentsFormat(), stringConfig.getMessagePrefix());
    }

    @Override
    public int uploadStudents() {

        String csvFile = inputConfig.getPath();
        String line = "";
        List<Student> studentList = new ArrayList<>(200);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] studentData = line.split(",");
                if (studentData.length == 4) {
                    studentList.add(createStudent(studentData[0], studentData[1], studentData[2], studentData[3]));
                }
            }
            applicationEventPublisher.publishEvent(new UploadingEvent(true));

            studentRepository.saveAll(studentList);
            return studentList.size();
        } catch (IOException e) {
            applicationEventPublisher.publishEvent(new UploadingEvent(false));
            e.printStackTrace();
            return 0;
        }
    }

    private Map<Boolean, String> checkIfCorrect(Student student) {
        if (student.getAge() < validationConfig.getMinAge() || student.getAge() > validationConfig.getMaxAge()) {
            return Map.of(false, MessageFormat.format(stringConfig.getWrongInputAgeFormat(),
                                                            stringConfig.getMessagePrefix(),
                                                            validationConfig.getMinAge(),
                                                            validationConfig.getMaxAge()));
        }
        if (studentRepository.findAll().contains(student)) {
            return Map.of(false, MessageFormat.format(stringConfig.getAlreadyExistedStudentFormat(),
                                                            stringConfig.getMessagePrefix(),
                                                            student.getFirstName(),
                                                            student.getLastName()));
        }
        return Map.of(true, "OK");
    }

    private Student createStudent(String firstName, String lastName, int age) {
        return Student.builder()
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .age(age)
                .build();
    }

    private Student createStudent(String id, String firstName, String lastName, String age) {
        return Student.builder()
                .id(Integer.parseInt(id))
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .age(Integer.parseInt(age))
                .build();
    }
}
