import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AddressBookMain {
    public static final int NEW_ADDRESSBOOK = 1;
    public static final int ADD_CONTACT = 2;
    public static final int ADD_CONTACT_CSV = 3;
    public static final int EDIT_CONTACT = 4;
    public static final int DELETE_CONTACT = 5;
    public static final int SEARCH = 6;
    public static final int SORTING = 7;
    public static final int DISPLAY = 8;
    public static final int EXIT = 9;
    public static Repository repository = new Repository();
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println(":: Wel-Come Address Book ::");
        AddressBookMain main = new AddressBookMain();
        AddressBook addressBook = new AddressBook();
        Map<String, List<Contacts>> map = repository.getReturnAddressBook();
        while (true) {
            String bookName;
            if (map.isEmpty()) {
                System.out.println("Book Is Empty First Add Address Book");
                bookName = addressBook.getAddressBookName();
                main.addPerson(bookName);
            } else {
                switch (addressBook.showChoiceBoard()) {
                    case NEW_ADDRESSBOOK:
                        bookName = addressBook.getAddressBookName();
                        if (addressBook.checkDuplicateAddressBook(bookName, map) != null) {
                            main.addPerson(bookName);
                        }
                        break;
                    case ADD_CONTACT:
                        bookName = addressBook.choiceAddressBook(map);
                        if (bookName != null) {
                            main.addPerson(bookName);
                        } else {
                            System.out.println(bookName + "Book Not Available");
                        }
                        break;
                    case ADD_CONTACT_CSV:
                        repository.addCsvFileInData(repository.getReturnContacts());
                        break;
                    case EDIT_CONTACT:
                        repository.editContact();
                        break;
                    case DELETE_CONTACT:
                        repository.deleteInformation();
                        break;
                    case SEARCH:
                        repository.searchPersonBelongCityOrState(addressBook.getLocation());
                        break;
                    case SORTING:
                        repository.sortedName();
                        break;
                    case DISPLAY:
                        addressBook.showAddressBook(map);
                        break;
                    case EXIT:
                        System.exit(0);
                    default:
                        System.out.println("Wrong Choice");
                }
            }
        }
    }

    public void addPerson(String addressBookName) throws IOException {
        System.out.println("ENTER PERSON FIRST NAME AND LAST NAME");
        String firstName = scan.next();
        String lastName = scan.next();
        System.out.println("ENTER ADDRESS AND CITY NAME");
        String address = scan.next();
        String city = scan.next();
        System.out.println("ENTER STATE AND ZIP");
        String state = scan.next();
        String zip = scan.next();
        System.out.println("ENTER PERSON PHONE NUMBER");
        String phoneNumber = scan.next();
        System.out.println("ENTER PERSON EMAIL ID");
        String emailId = scan.next();
        Contacts person = new Contacts(firstName, lastName, address, city, state, zip, phoneNumber, emailId);
        repository.addAddressBook(addressBookName, person);
    }
}
