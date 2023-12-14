package com.example.spring.third.repositiry;

import com.example.spring.third.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    List<Contact> findAll();
    Optional<Contact> findById(int id);
    void save(Contact contact);
    void update(Contact contact);
    void deleteById(int id);
    void batchInsert(List<Contact> contacts);
    int count();
    int maxId();
    void deleteAll();
    boolean existsById(int id);
}
