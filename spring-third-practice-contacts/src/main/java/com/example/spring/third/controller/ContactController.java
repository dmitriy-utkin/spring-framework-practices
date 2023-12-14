package com.example.spring.third.controller;

import com.example.spring.third.model.Contact;
import com.example.spring.third.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contacts", contactService.findAll());
        return "index";
    }

    @GetMapping("/contacts/deleteAll")
    public String deleteAll() {
        contactService.deleteAll();
        return "redirect:/";
    }

    @GetMapping("/contacts/delete/{id}")
    public String deleteById(@PathVariable int id) {
        contactService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/contacts/create")
    public String showForm(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("form", "create");
        return "create-update";
    }

    @GetMapping("/contacts/edit/{id}")
    public String showForm(@PathVariable int id, Model model) {
        model.addAttribute("form", "edit");
        model.addAttribute("contact", contactService.findById(id));
        return "create-update";
    }


    @PostMapping("/contacts/save")
    public String saveTask(@ModelAttribute Contact contact){
        if (contact.getId() == 0) {
            contactService.save(contact);
        } else {
            contactService.update(contact);
        }
        return "redirect:/";
    }

    @GetMapping("/contacts/generic")
    public String handleGenericContactRequest(@RequestParam("count") int count) {
        contactService.createGenericContacts(count);
        return "redirect:/";
    }

}
