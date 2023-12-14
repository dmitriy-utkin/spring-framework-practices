package com.example.spring.third.service;

import com.example.spring.third.config.InputConfig;
import com.example.spring.third.model.Contact;
import com.example.spring.third.repositiry.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Profile({"default", "prod"})
public class ProdContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final InputConfig inputConfig;

    @Override
    public List<Contact> findAll() {
        log.debug("ProdContactServiceImpl -> findAll");
        return contactRepository.findAll();
    }

    @Override
    public Contact findById(int id) {
        log.debug("ProdContactServiceImpl -> findById");
        return contactRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Contact contact) {
        log.debug("ProdContactServiceImpl -> save");
        contactRepository.save(contact);
    }

    @Override
    public void update(Contact contact) {
        log.debug("ProdContactServiceImpl -> update");
        contactRepository.update(contact);
    }

    @Override
    public void deleteById(int id) {
        log.debug("ProdContactServiceImpl -> deleteById");
        contactRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        log.debug("ProdContactServiceImpl -> deleteAll");
        contactRepository.deleteAll();
    }

    @Override
    public boolean existsById(int id) {
        log.debug("ProdContactServiceImpl -> existsById");
        return contactRepository.existsById(id);
    }

    @Override
    public void createGenericContacts(int count) {
        log.debug("ProdContactServiceImpl -> createGenericContacts");
        List<String> firstNames = getListFromSrc(inputConfig.getFirstNamePath());
        List<String> lastNames = getListFromSrc(inputConfig.getLastNamePath());
        List<String> emails = getListFromSrc(inputConfig.getEmailPath());
        List<String> phones = getListFromSrc(inputConfig.getPhonePath());

        List<Contact> contacts = new ArrayList<>(count);

        int id = contactRepository.maxId() + 1;

        while (contacts.size() < count) {
            contacts.add(createContact(id, firstNames, lastNames, emails, phones));
            id++;
        }
        contactRepository.batchInsert(contacts);
    }

    private Contact createContact(int id,
                                  List<String> firstNames,
                                  List<String> lastNames,
                                  List<String> emails,
                                  List<String> phones) {
        Collections.shuffle(firstNames);
        Collections.shuffle(lastNames);
        Collections.shuffle(emails);
        Collections.shuffle(phones);
        return new Contact(id, firstNames.get(1), lastNames.get(1), emails.get(1), phones.get(1));
    }

    private List<String> getListFromSrc(String path) {
        String line = "";
        List<String> raws = new ArrayList<>(150);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                raws.add(line);
            }
            return raws;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
