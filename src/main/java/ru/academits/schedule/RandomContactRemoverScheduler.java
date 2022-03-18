package ru.academits.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.academits.model.Contact;
import ru.academits.phonebook.PhonebookController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RandomContactRemoverScheduler {
    private final PhonebookController phonebookController;
    private static final Logger logger = LoggerFactory.getLogger(PhonebookController.class);

    public RandomContactRemoverScheduler(PhonebookController phonebookController) {
        this.phonebookController = phonebookController;
    }

    @Scheduled(fixedRate = 10000)
    public void deleteRandomContact() {
        List<Contact> contactList = phonebookController.getContacts("");

        if (contactList.size() == 0) {
            // === LOGGING START ===
            logger.info("Random contact scheduled delete: no contacts in the Phonebook, nothing to delete.");
            // === LOGGING END ===

            return;
        }

        Random rand = new Random();
        Contact randomContact = contactList.get(rand.nextInt(contactList.size()));

        ArrayList<Integer> contactIds = new ArrayList<>();
        contactIds.add(randomContact.getId());

        phonebookController.deleteContacts(contactIds);

        // === LOGGING START ===
        String logMessage = String.format("Random contact scheduled delete: contact with Id = %d set as subject to delete.", contactIds.get(0));
        logger.info(logMessage);
        // === LOGGING END ===
    }
}