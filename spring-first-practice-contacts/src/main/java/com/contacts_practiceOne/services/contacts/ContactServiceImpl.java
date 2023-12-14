package com.contacts_practiceOne.services.contacts;

import com.contacts_practiceOne.config.ErrorConfig;
import com.contacts_practiceOne.config.IOConfig;
import com.contacts_practiceOne.config.RegexConfig;
import com.contacts_practiceOne.model.Contact;
import com.contacts_practiceOne.repository.ContactsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Component
public class ContactServiceImpl implements ContactService {

    private final ErrorConfig errorConfig;
    private final RegexConfig regexConfig;
    private final ContactsRepository contactsRepository;
    private final IOConfig ioConfig;

    @Override
    public List<Contact> getAllContacts() {
        return contactsRepository.getAllContacts();
    }

    @Override
    public Map<Boolean, String> addContact(String fullInfo) {

        fullInfo = fullInfo.trim().toLowerCase();

        String[] fullInfoParts = fullInfo.split("; ");

        if (fullInfoParts.length != 3) {
            return Map.of(false, errorConfig.getInvalidFullInfo());
        }
        if (!fullInfoParts[0].replace(";", "").trim().matches(regexConfig.getFullName())) {
            return Map.of(false, errorConfig.getInvalidFullName());
        }
        if (!fullInfoParts[1].replace(";", "").trim().matches(regexConfig.getPhoneNumber())) {
            return Map.of(false, errorConfig.getInvalidPhoneNumber());
        }
        if (!fullInfoParts[2].replace(";", "").trim().matches(regexConfig.getEmail())) {
            return Map.of(false, errorConfig.getInvalidEmail());
        }
        Contact contact = createContact(fullInfoParts);
        if (contactsRepository.getAllContacts().contains(contact)) {
            return Map.of(false, errorConfig.getAlreadyExist());
        }

        contactsRepository.addContact(contact, ioConfig.getOutput());
        return Map.of(true, "OK");
    }

    @Override
    public Map<Boolean, String> removeContactByEmail(String email) {
        List<Contact> contacts = contactsRepository.getAllContacts();
        if (contacts.size() == 0) {
            return Map.of(false, errorConfig.getEmptyContactBook());
        }
        String preparedEmail = email.toLowerCase().trim();
        if (!email.matches(regexConfig.getEmail())) {
            return Map.of(false, errorConfig.getInvalidEmail());
        }
        Contact contact = contacts.stream()
                .filter(item -> preparedEmail.equals(item.getEmail()))
                .findFirst().orElse(null);
        if (contact == null) {
            return Map.of(false, errorConfig.getEmailNotFound());
        }
        contactsRepository.removeContact(contact);
        return Map.of(true, "OK");
    }


    private Contact createContact(String[] fullInfoParts) {
        return Contact.builder().fullName(getPreparedFullName(fullInfoParts[0]))
                .phoneNumber(getPreparedPhoneNumber(fullInfoParts[1]))
                .email(fullInfoParts[2].trim().replace(";", "")).build();
    }

    private String getPreparedFullName(String fullName) {
        fullName = fullName.replace(";", "");
        StringBuilder preparedName = new StringBuilder();
        String[] nameParts = fullName.split(" ");
        for (String part : nameParts) {
            preparedName.append(part.substring(0, 1).toUpperCase()).append(part, 1, part.length()).append(" ");
        }
        return preparedName.toString().trim();
    }

    private String getPreparedPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace(";", "");
        if (phoneNumber.startsWith("8")) {
            phoneNumber = phoneNumber.replaceFirst("8", "+7");
        }
        return phoneNumber.trim();
    }


}
