package ru.academits.service;

import org.springframework.stereotype.Service;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;
import ru.academits.model.ContactValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private final ContactDao contactDao;

    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = contactDao.getAllContacts();

        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }

        return false;
    }

    private ContactValidation validateContact(Contact contact) {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);

        if (contact.getFirstName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Имя должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Фамилия должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getPhone().isEmpty()) {
            contactValidation.setValid(false);
            contactValidation.setError("Поле Телефон должно быть заполнено.");
            return contactValidation;
        }

        if (isExistContactWithPhone(contact.getPhone())) {
            contactValidation.setValid(false);
            contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return contactValidation;
        }

        return contactValidation;
    }

    public ContactValidation addContact(Contact contact) {
        ContactValidation contactValidation = validateContact(contact);

        if (contactValidation.isValid()) {
            contactDao.add(contact);
        }

        return contactValidation;
    }

    public List<Contact> getContacts(String term) {
        // Return all contact if term is empty or null
        if (term == null || term.equals("")) {
            return contactDao.getAllContacts();
        }

        String finalTerm = term.toLowerCase(Locale.ROOT);

        return contactDao.getAllContacts().stream()
                .filter(c -> c.getFirstName().toLowerCase(Locale.ROOT).contains(finalTerm) ||
                        c.getLastName().toLowerCase(Locale.ROOT).contains(finalTerm) ||
                        c.getPhone().toLowerCase(Locale.ROOT).contains(finalTerm))
                .collect(Collectors.toList());
    }

    public boolean toggleImportant(int contactId) {
        return contactDao.toggleImportant(contactId);
    }

    public void deleteContacts(ArrayList<Integer> contactsIds) {
        contactDao.deleteContacts(contactsIds);
    }
}
