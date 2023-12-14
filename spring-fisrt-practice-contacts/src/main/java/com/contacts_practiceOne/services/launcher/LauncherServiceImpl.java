package com.contacts_practiceOne.services.launcher;

import com.contacts_practiceOne.config.ErrorConfig;
import com.contacts_practiceOne.config.LauncherConfig;
import com.contacts_practiceOne.model.Contact;
import com.contacts_practiceOne.services.contacts.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@AllArgsConstructor
@Component
public class LauncherServiceImpl implements LauncherService {

    private final LauncherConfig launcherConfig;
    private final ErrorConfig errorConfig;
    private final ContactService contactService;

    @Override
    public void launch() {
        System.out.println(launcherConfig.getGreetings());

        String input;

        while (true){
            System.out.println(launcherConfig.getBaseInstruction());

            input = input();

            if (input.equals("1")) {
                printAllContact();
            }

            if (input.equals("2")) {
                addContact();
            }

            if (input.equals("3")) {
                removeContact();
            }

            if (!input.matches("[1-4]{1}")) {
                System.out.println(launcherConfig.getInvalidRequestMainPage());
                System.out.println("\n");
            }

            if (input.equals("4")) {
                System.out.println("Application was stopped.");
                break;
            }
        }
    }

    private void removeContact() {
        System.out.println(launcherConfig.getExitApp());
        System.out.println(launcherConfig.getEmailFormat());
        String input;
        Map<Boolean, String> result;
        while (true) {
            input = input();
            result = contactService.removeContactByEmail(input);
            if (result.containsKey(true)) {
                System.out.println(" ->>>> Contact removed");
                System.out.println("\n");
                break;
            }
            System.out.println("Error: " + result.get(false) + "\nTry again");
            System.out.println("\n");
            if (input.equals("4")) {
                System.out.println("Removing contact menu was stopped.");
                break;
            }
        }
    }


    private String input() {
        return new Scanner(System.in).nextLine().toLowerCase().trim();
    }

    private void printAllContact() {
        List<Contact> contacts = contactService.getAllContacts();
        if (contacts.size() == 0) {
            System.out.println(errorConfig.getEmptyContactBook());
            System.out.println("\n");
            return;
        }
        System.out.println("All contacts in the book:");
        contacts.forEach(contact -> System.out.println(" -> " + contact));
        System.out.println("\n");
    }

    private void addContact() {
        System.out.println(launcherConfig.getExitApp());
        System.out.println(launcherConfig.getFinalInputFormat());
        String input;
        Map<Boolean, String> result;
        while (true) {
            input = input();
            result = contactService.addContact(input);
            if (result.containsKey(true)) {
                System.out.println(" ->>>> Contact saved");
                System.out.println("\n");
                break;
            }
            System.out.println("Error: " + result.get(false) + "\nTry again");
            System.out.println("\n");
            if (input.equals("4")) {
                System.out.println("Adding contact menu was stopped.");
                break;
            }
        };
    }
}
