package com.contacts_practiceOne.repository;

import com.contacts_practiceOne.model.Contact;

import java.util.List;

public interface ContactsRepository {
    List<Contact> getAllContacts();
    void addContact(Contact contact, String outputPath);
    void removeContact(Contact contact);
}
