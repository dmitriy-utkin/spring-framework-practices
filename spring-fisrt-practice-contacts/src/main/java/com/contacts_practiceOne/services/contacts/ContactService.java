package com.contacts_practiceOne.services.contacts;

import com.contacts_practiceOne.model.Contact;

import java.util.List;
import java.util.Map;

public interface ContactService {
    List<Contact> getAllContacts();
    Map<Boolean, String> addContact(String fullInfo);
    Map<Boolean, String> removeContactByEmail(String email);
}
