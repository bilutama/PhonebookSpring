package ru.academits.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.academits.model.Contact;
import ru.academits.service.ContactService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RandomContactRemoverScheduler {
    private final ContactService contactService;

    public RandomContactRemoverScheduler(ContactService contactService) {
        this.contactService = contactService;
    }

    @Scheduled(fixedRate = 10000)
    public void deleteRandomContact() {
        List<Contact> contactList = contactService.getContacts("");
        Random rand = new Random();
        Contact randomContact = contactList.get(rand.nextInt(contactList.size()));

        boolean randomContactIsDeleted = contactService.deleteContacts(new ArrayList<>(randomContact.getId()));

        System.out.printf("Random contact deleted: name = %s%n", randomContact.getFirstName());
    }
}