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
    private final ContactService contactService;
    private static final Logger logger = LoggerFactory.getLogger(PhonebookController.class);

    public PhonebookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(value = {"getContacts/", "getContacts/{term}"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Contact> getContacts(@PathVariable(required = false) String term) {
        // === LOGGING START ===
        String finalTerm = term == null ? "" : term;
        String logMessage = String.format("getContacts is called with term = \"%s\"", finalTerm);
        logger.info(logMessage);
        // === LOGGING END ===

        return contactService.getContacts(term);
    }

    @RequestMapping(value = "addContact", method = RequestMethod.POST)
    @ResponseBody
    public ContactValidation addContact(@RequestBody Contact contact) {
        // === LOGGING START ===
        String logMessage = String.format("New contact is added: first name=%s, last name=%s, phone=%s",
                contact.getFirstName(), contact.getLastName(), contact.getPhone());
        logger.info(logMessage);
        // === LOGGING END ===

        return contactService.addContact(contact);
    }

    @RequestMapping(value = "toggleImportant/{contactId}", method = RequestMethod.POST)
    @ResponseBody
    public boolean toggleImportant(@PathVariable Integer contactId) {
        boolean isImportanceToggled = contactService.toggleImportant(contactId);

        // === LOGGING START ===
        String logMessage;

        if (isImportanceToggled) {
            logMessage = String.format("Toggling importance for contact ID=%d succeeded.", contactId);
        } else {
            logMessage = String.format("Toggling importance for contact ID=%d failed. Possible reason: no such contact in the database.", contactId);
        }

        logger.info(logMessage);
        // === LOGGING END ===

        return isImportanceToggled;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteContacts(@RequestBody ArrayList<Integer> contactIds) {
        boolean contactsAreDeleted = contactService.deleteContacts(contactIds);

        // === LOGGING START ===
        String contactIdsString = contactIds.stream()
                .map(Object::toString)
                .reduce((t, u) -> t + ", " + u)
                .orElse("");

        String logMessage;

        if (contactsAreDeleted) {
            logMessage = String.format("Deleting contacts with IDs = %s succeeded.", contactIdsString);
        } else {
            logMessage = String.format("Deleting contacts with IDs = %s failed. Possible reason: no such contacts in the database.", contactIdsString);
        }

        logger.info(logMessage);
        // === LOGGING END ===

        return contactsAreDeleted;
    }
}