import util.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ContactsApplication {
    static Input input = new Input();
    static String directory = "src/data";
    static String filename = "contacts.txt";
    static Path dataDirectory = Paths.get(directory);
    static Path dataFilepath = Paths.get(directory, filename);
    static ArrayList<Contact> contacts = new ArrayList<>();

    public static void methodHandler(){
        dataSetup();
        readContacts();
        do {
            int option = displayMenu();
            if (option == 1){
                printContacts();
            } else if (option == 2){
                addContact();
            } else if (option == 3){
                searchContacts();
            } else if (option == 4){
                deleteContact();
            } else if (option == 5){
                exitContacts();
                break;
            }
        } while (true);
    }
    private static int displayMenu(){
        return input.getInt("1. View contacts.\n" +
                "2. Add a new contact.\n" +
                "3. Search a contact by name.\n" +
                "4. Delete an existing contact.\n" +
                "5. Exit.\n" +
                "Enter an option (1, 2, 3, 4 or 5): ", 1, 5);
    }
    private static void printContacts(){
        System.out.println("Name       | Phone Number |\n" +
                "---------------------------");
        for (Contact contact : contacts) {
            printContact(contact);
        }
    }
    private static void printContact(Contact contact){
        String contactNumber = String.valueOf(contact.getNumber());
        String numberOutput = contactNumber;
        if (contactNumber.length() >= 7){
            numberOutput = addChar(numberOutput, 3);
            if (contactNumber.length() == 10){
                numberOutput = addChar(numberOutput, 7);
            }
        }
        System.out.printf("%-10s | %-12s |\n", contact.getName(), numberOutput);
    }
    private static String addChar(String str, int position) {
        return str.substring(0, position) + '-' + str.substring(position);
    }
    private static void addContact(){
        String contactName = input.getString("Enter contact name: ");
        for (Contact contact : contacts){
            if (contactName.equalsIgnoreCase(contact.getName())){
                if (input.yesNo("There's already a contact named " + contactName +
                        ". Do you want to overwrite it? (Yes/No)")) {
                    int index = contacts.indexOf(contact);
                    overwriteContact(index, contactName);
                }
                return;
            }
        }
        long contactNumber = input.getLong("Enter contact number: ");
        Contact contact = new Contact(contactName, contactNumber);
        contacts.add(contact);
    }
    private static void overwriteContact(int index, String contactName){
        contacts.remove(index);
        long contactNumber = input.getLong("Enter contact number: ");
        Contact contact = new Contact(contactName, contactNumber);
        contacts.add(index, contact);
    }
    private static void searchContacts(){
        String userInput = input.getString("Enter contact to search: ");
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(userInput)) {
                printContact(contact);
                return;
            }
        }
        System.out.println("Sorry, contact not found.");
    }
    private static void deleteContact(){
        String userInput = input.getString("Enter contact to delete: ");
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(userInput)) {
                contacts.remove(contact);
                System.out.println("Contact deleted");
                return;
            }
        }
        System.out.println("Sorry, contact not found.");
    }
    private static void exitContacts(){
        writeContacts(contacts);
        System.out.println("Goodbye, and have a wonderful day!");
    }
    private static void readContacts(){
        try {
            ArrayList<String> contactStrings = new ArrayList<>(Files.readAllLines(dataFilepath));
            for (String s : contactStrings) {
                String[] arrOfStr = s.split(" ", 0);
                long contactNumber;
                try {
                    contactNumber = Long.parseLong(arrOfStr[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Error: enter a long");
                    continue;
                }
                Contact contact = new Contact(arrOfStr[0], contactNumber);
                contacts.add(contact);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private static void writeContacts(ArrayList<Contact> contacts){
        try {
            // first create an ArrayList of Strings from the robots
            ArrayList<String> contactStrings = new ArrayList<>();
            for(Contact contact : contacts) {
                contactStrings.add(contact.toString());
            }

            // write the arraylist of robot strings to the file
            Files.write(dataFilepath, contactStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void dataSetup(){
        try {
            if (Files.notExists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            if (!Files.exists(dataFilepath)) {
                Files.createFile(dataFilepath);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
