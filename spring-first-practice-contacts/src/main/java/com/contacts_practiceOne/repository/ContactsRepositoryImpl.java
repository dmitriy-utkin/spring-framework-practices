package com.contacts_practiceOne.repository;

import com.contacts_practiceOne.model.Contact;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContactsRepositoryImpl implements ContactsRepository {


    private List<Contact> contactBook = new ArrayList<>();

    @Override
    public List<Contact> getAllContacts() {
        return contactBook;
    }

    @Override
    public void addContact(Contact contact, String outputPath) {
        contactBook.add(contact);
        saveContactToFile(contact, outputPath);
    }

    @Override
    public void removeContact(Contact contact) {
        contactBook.remove(contact);
    }

    public void saveContactToFile(Contact contact, String outputPath) {
        String fileName = outputPath;
        String data = contact.toString().replace(" | ", ";") + "\n";

        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
