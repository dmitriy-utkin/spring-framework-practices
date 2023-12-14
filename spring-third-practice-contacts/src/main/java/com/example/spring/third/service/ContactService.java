package com.example.spring.third.service;

import com.example.spring.third.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> findAll();
    Contact findById(int id);
    void save(Contact contact);
    void update(Contact contact);
    void deleteById(int id);
    void deleteAll();
    boolean existsById(int id);
    void createGenericContacts(int count);
}
