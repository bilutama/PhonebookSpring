package ru.academits;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;
import ru.academits.model.ContactValidation;
import ru.academits.service.ContactService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PhonebookServiceTest {
    private static ContactService contactService = null;
    //new ContactService(new ContactDao());

    // Set of mock contacts
    private static Contact mockContact1;
    private static Contact mockContact2;
    private static Contact mockContact3;

    @BeforeClass
    public static void initializeMockContacts() {
        // Valid contact
        mockContact1 = new Contact();
        mockContact1.setFirstName("Vasily");
        mockContact1.setLastName("Ivanov");
        mockContact1.setPhone("9131234567");
        mockContact1.setImportant(true);

        // Invalid contact with empty First Name
        mockContact2 = new Contact();
        mockContact2.setFirstName("");
        mockContact2.setLastName("Petrov");
        mockContact2.setPhone("9133333333");
        mockContact2.setImportant(false);

        // Invalid contact with empty Phone
        mockContact3 = new Contact();
        mockContact3.setFirstName("Igor");
        mockContact3.setLastName("Pavlov");
        mockContact3.setPhone("");
        mockContact3.setImportant(false);
    }

    @Before
    public void initService() {
        contactService = new ContactService(new ContactDao());
    }

    @Test
    public void addContact() {
        // Add a valid contact
        ContactValidation contactValidation1 = contactService.addContact(mockContact1);
        assertTrue(contactValidation1.isValid());

        // Add an invalid contact
        ContactValidation contactValidation2 = contactService.addContact(mockContact2);
        assertFalse(contactValidation2.isValid());
        assertEquals("First name is required", contactValidation2.getError());

        // Add an invalid contact
        ContactValidation contactValidation3 = contactService.addContact(mockContact3);
        assertFalse(contactValidation3.isValid());
        assertEquals("Telephone number is required", contactValidation3.getError());

        // Add an existing valid contact again
        ContactValidation contactValidation4 = contactService.addContact(mockContact1);
        assertFalse(contactValidation4.isValid());
        assertEquals("Telephone number cannot duplicate that are already exist", contactValidation4.getError());
    }

    @Test
    public void getContacts() {
        // Add valid and invalid contacts
        contactService.addContact(mockContact1);
        contactService.addContact(mockContact2);
        contactService.addContact(mockContact3);

        List<Contact> contacts = contactService.getContacts("");

        assertEquals(2, contacts.size());

        Contact firstContact = contacts.get(0);
        assertEquals("John", firstContact.getFirstName());
        assertEquals("Smith", firstContact.getLastName());
        assertEquals("9123456789", firstContact.getPhone());
        assertFalse(firstContact.getImportant());

        Contact secondContact = contacts.get(1);
        assertEquals("Vasily", secondContact.getFirstName());
        assertEquals("Ivanov", secondContact.getLastName());
        assertEquals("9131234567", secondContact.getPhone());
        assertFalse(secondContact.getImportant()); // Importance state switched in toggleContactImportanceMethod
    }

    @Test
    public void deleteContacts() {
        List<Contact> contacts = contactService.getContacts(null);
        contactService.addContact(mockContact1);
        assertEquals(2, contacts.size());

        ArrayList<Integer> contactsIds = new ArrayList<>();
        contactsIds.add(contacts.get(1).getId());
        contactService.deleteContacts(contactsIds);

        contacts = contactService.getContacts(null);
        assertEquals(1, contacts.size());
    }

    @Test
    @DisplayName("Toggle contact importance")
    public void toggleContactImportance() {
        contactService.addContact(mockContact1);
        List<Contact> contacts = contactService.getContacts(null);
        assertTrue(contacts.get(1).getImportant());

        contactService.toggleImportant(contacts.get(1).getId());
        assertFalse(contacts.get(1).getImportant());
    }

    @Test
    @DisplayName("Search contacts")
    public void searchContacts() {
        contactService.addContact(mockContact1);

        // Mock search for 'Vasily' name with term 'vasi'
        List<Contact> contacts = contactService.getContacts("vasi");

        assertEquals(1, contacts.size());

        Contact firstContact = contacts.get(0);
        assertEquals("Vasily", firstContact.getFirstName());
        assertEquals("Ivanov", firstContact.getLastName());
        assertEquals("9131234567", firstContact.getPhone());
        assertFalse(firstContact.getImportant()); // Importance state switched in toggleContactImportanceMethod
    }
}