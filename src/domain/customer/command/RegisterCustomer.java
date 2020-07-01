package domain.customer.command;

import domain.customer.value.ID;
import domain.customer.value.EmailAddress;
import domain.customer.value.Hash;
import domain.customer.value.PersonName;

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
