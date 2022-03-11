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
@RequestMapping("/phonebook/rpc/api/v1")
public class PhonebookController {
    private static final Logger logger = LoggerFactory.getLogger(PhonebookController.class);

    private final ContactService contactService;

    public PhonebookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = "getAllContacts", method = RequestMethod.POST)
    @ResponseBody
    public List<Contact> getContacts(@RequestParam String term) {
        // === LOGGER START
        if (term == null) {
            logger.info("getContacts method was called with no term");
        } else {
            // TODO: upd
            String logMessage = String.format("getContacts method was called with term = %s", term);
            logger.info(logMessage);
        }
        // === LOGGER END

        List<Contact> contactList = contactService.getAllContacts();

        // Filter contacts if term is passed
        if (term == null || term.equals("")) {
            return contactList;
        }

        String finalTerm = term.toLowerCase(Locale.ROOT);

        return contactList.stream()
                .filter(c -> c.getFirstName().toLowerCase(Locale.ROOT).contains(finalTerm) ||
                        c.getLastName().toLowerCase(Locale.ROOT).contains(finalTerm) ||
                        c.getPhone().toLowerCase(Locale.ROOT).contains(finalTerm))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    @ResponseBody
    public ContactValidation addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }
}