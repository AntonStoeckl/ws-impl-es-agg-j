package customer.command;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;
import customer.value.PersonName;

public final class RegisterCustomer {
    public final ID customerID;
    public final EmailAddress emailAddress;
    public final Hash confirmationHash;
    public final PersonName name;

    private RegisterCustomer(String emailAddress, String givenName, String familyName) {
        this.customerID = ID.generate();
        this.confirmationHash = Hash.generate();
        this.emailAddress = EmailAddress.build(emailAddress);
        this.name = PersonName.build(givenName, familyName);
    }

    public static RegisterCustomer build(String emailAddress, String givenName, String familyName) {
        return new RegisterCustomer(emailAddress, givenName, familyName);
    }
}
