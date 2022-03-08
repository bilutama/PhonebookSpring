package ru.academits.phonebook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.academits.model.Contact;
import ru.academits.model.ContactValidation;
import ru.academits.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/phoneBook/rpc/api/v1")
public class PhoneBookController {
    private static final Logger logger = LoggerFactory.getLogger(PhoneBookController.class);

    private final ContactService contactService;

    public PhoneBookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "getAllContacts", method = RequestMethod.GET)
    @ResponseBody
    public List<Contact> getContacts(@RequestParam String term) {
        // === LOGGER START
        if (term == null) {
            logger.info("called method getContacts with empty term");
        } else {
            // TODO: upd
            logger.info("called method getContacts with term");
        }
        // === LOGGER END

        List<Contact> contactList = contactService.getAllContacts();

        // Filter contacts if term is passed
        if (term != null) {
            String finalTerm = term.toLowerCase(Locale.ROOT);

            contactList = contactList.stream()
                    .filter(c -> c.getFirstName().toLowerCase(Locale.ROOT).contains(finalTerm) ||
                            c.getLastName().toLowerCase(Locale.ROOT).contains(finalTerm) ||
                            c.getPhone().toLowerCase(Locale.ROOT).contains(finalTerm))
                    .collect(Collectors.toList());
        }

        return contactList;
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    @ResponseBody
    public ContactValidation addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }
}