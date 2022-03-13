package ru.academits.phonebook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.academits.model.Contact;
import ru.academits.model.ContactValidation;
import ru.academits.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/phonebook/rpc/api/v1")
public class PhonebookController {
    private static final Logger logger = LoggerFactory.getLogger(PhonebookController.class);

    private final ContactService contactService;

    public PhonebookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = {"getContacts/", "getContacts/{term}"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Contact> getContacts(@PathVariable(required = false) String term) {
        // === LOGGER START ===
        if (term == null || term.equals("")) {
            logger.info("getContacts method is called with empty term");
        } else {
            String logMessage = String.format("getContacts method was called with term = %s", term);
            logger.info(logMessage);
        }
        // === LOGGER END ===

        return contactService.getContacts(term);
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    @ResponseBody
    public ContactValidation addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }

    @RequestMapping(value = "toggleImportant/{contactId}", method = RequestMethod.POST)
    @ResponseBody
    public boolean toggleImportant(@PathVariable Integer contactId) {
        return contactService.toggleImportant(contactId);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public void DeleteContacts(@RequestBody ArrayList<Integer> contactIds) {
        contactService.deleteContacts(contactIds);
    }
}