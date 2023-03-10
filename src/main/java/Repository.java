
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Repository {
    public final int NAME = 1;
    public final int FULL_ADDRESS = 2;
    public final int NUMBER = 3;
    Map<String, List<Contacts>> map = new HashMap<>();
    List<Contacts> contacts = new ArrayList<>();
    AddressBook addressBook = new AddressBook();
    Scanner scan = new Scanner(System.in);
    public String filePath = "C:\\Users\\Admin\\IdeaProjects\\AddressBooksMaven\\src\\main\\resources\\contactsInformation.csv";

    public Map<String, List<Contacts>> getReturnAddressBook() {
        return map;
    }

    public List<Contacts> getReturnContacts() { return contacts; }

    void addAddressBook(String addressBookName, Contacts newPerson) throws IOException {
        if (map.containsKey(addressBookName)) {
            contacts = map.get(addressBookName);
            long duplicate = contacts.stream()
                    .map(contacts1 -> contacts1.getFirstName())
                    .filter(n -> n.equals(newPerson.firstName))
                    .count();
            if (duplicate == 0) {
                contacts.add(newPerson);
                map.put(addressBookName, contacts);
            } else {
                System.out.println("Person name already exits");
            }
        } else {
            List<Contacts> newContact = new ArrayList<>();
            newContact.add(newPerson);
            map.put(addressBookName, newContact);
        }
    }

    public void addCsvFileInData(List<Contacts> returnContacts) {
        FileWriter fileWriter = null;
        boolean isFound = false;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.append("First Name , Last Name , Address , City");
            fileWriter.append(" State , Zip Code , Mobile Number , Email");
            for (Contacts newPerson : returnContacts) {
                fileWriter.append(newPerson.getFirstName());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getLastName());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getAddress());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getCity());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getState());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getZipCode());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getPhoneNumber());
                fileWriter.append(" , ");
                fileWriter.append(newPerson.getEmail());
                fileWriter.append(" \n ");
                isFound = true;
            }
            if (isFound){
                List<Contacts> csvContacts =(List<Contacts>) new CsvToBeanBuilder(new FileReader(filePath))
                        .withType(Contacts.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build().parse();
                for (Contacts contactDetails : csvContacts) {
                    System.out.println(contactDetails);
                }
            }else {
                System.out.println("Data Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editContact() {
        Contacts editContactObject = findContactInObject();
        if (editContactObject != null) {
            editContactInformation(editContactObject);
            System.out.println("Successfully Edit Person Details !");

        }
        System.out.println("Name Does Not Exist");
    }

    public void deleteInformation() {
        Contacts removeContact = findContactInObject();
        if (removeContact != null) {
            contacts.remove(removeContact);
            System.out.println("Contact Deleted");
        }
        System.out.println("Name Does Not Exist");
    }

    private Contacts findContactInObject() {
        String bookName = addressBook.choiceAddressBook(map);
        Contacts removeObject = null;
        if (bookName != null) {
            System.out.println("Enter Person First Name");
            String name = scan.next();
            removeObject = map.get(bookName).stream()
                    .filter(n -> n.firstName.equals(name))
                    .findFirst()
                    .orElse(null);
            return removeObject;
        }
        System.out.println(bookName + "Book Not Available");
        return removeObject;
    }

    public void searchPersonBelongCityOrState(String location) {
        System.out.println(":: Person Name\tMobile Number\tSame City Or State ::");
        map.entrySet()
                .forEach(contact -> contact.getValue()
                        .stream()
                        .filter(n -> n.city.contains(location)
                                || n.state.contains(location))
                        .forEach(n -> System.out.println(" " + n.getFirstName()
                                + " " + n.getLastName()
                                + "\t " + n.getPhoneNumber()
                                + "\t" + n.getCity() + " " + n.getState()
                                + "\n")));

        List<Map.Entry<String, List<Contacts>>> countCity;
        countCity = map.entrySet()
                .stream()
                .filter(n -> n.getValue().get(0).getCity().equals(location)
                        || n.getValue().get(0).getState().equals(location))
                .collect(Collectors.toList());
        System.out.println("Total Same City Or State Belong Person :- " + countCity.stream().count());
    }

    private void editContactInformation(Contacts editContactObject) {
        System.out.println("\tWHICH DATA EDIT \n\t1] FULL NAME \n\t2] ADDRESS " +
                "\n\t3] PHONE NUMBER");
        int choice = scan.nextInt();
        switch (choice) {
            case NAME:
                System.out.println("ENTER NEW FIRST NAME AND LAST NAME");
                editContactObject.setFirstName(scan.next());
                editContactObject.setLastName(scan.next());
                break;
            case FULL_ADDRESS:
                System.out.println("ENTER NEW ADDRESS , CITY AND STATE");
                editContactObject.setAddress(scan.next());
                editContactObject.setCity(scan.next());
                editContactObject.setState(scan.next());
                break;
            case NUMBER:
                System.out.println(" ENTER NEW PHONE NUMBER");
                editContactObject.setPhoneNumber(scan.next());
                break;
            default:
                System.out.println("WRONG CHOICE ..THANK YOU");
        }
    }

    public void sortedName() {
        String bookName = addressBook.getAddressBookName();
        List<Contacts> contactsList = map.get(bookName);
        contactsList
                .stream()
                .sorted(Comparator.comparing(Contacts::getFirstName))
                .forEach(i -> System.out.println("PERSON NAME :: "
                        + i.getFirstName() + " " + i.getLastName()
                        + "\t " + i.getPhoneNumber()
                        + "\t" + i.getCity()
                        + " " + i.getState()
                        + "\n"));
    }
}
