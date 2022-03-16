package ru.academits.dao;

import org.springframework.stereotype.Repository;
import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class ContactDao {
    private List<Contact> contactList = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact = new Contact();
        contact.setId(getNewId());
        contact.setFirstName("John");
        contact.setLastName("Smith");
        contact.setPhone("9123456789");

        contactList.add(contact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }

    public boolean deleteContacts(ArrayList<Integer> contactsIds) {
        int listSizeBeforeDelete = contactList.size();
        contactList = contactList
                .stream()
                .filter(c -> !contactsIds.contains(c.getId()))
                .collect(Collectors.toList());

        return contactList.size() != listSizeBeforeDelete;
    }

    public boolean toggleImportant(int contactId) {
        Contact contact = contactList
                .stream()
                .filter(c -> c.getId() == contactId)
                .findAny()
                .orElse(null);

        if (contact == null) {
            return false;
        }

        contact.setImportant(!contact.getImportant());
        return true;
    }
}