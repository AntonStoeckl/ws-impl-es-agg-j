package customer.command;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;
import customer.value.PersonName;

public final class RegisterCustomer {
    private final ID id;
    private final EmailAddress emailAddress;
    private final Hash confirmationHash;
    private final PersonName name;

    private RegisterCustomer(String emailAddress, String givenName, String familyName) {
        this.id = ID.generate();
        this.confirmationHash = Hash.generate();
        this.emailAddress = EmailAddress.build(emailAddress);
        this.name = PersonName.build(givenName, familyName);
    }

    public static RegisterCustomer build(String emailAddress, String givenName, String familyName) {
        return new RegisterCustomer(emailAddress, givenName, familyName);
    }

    public ID id() {
        return id;
    }

    public EmailAddress emailAddress() {
        return emailAddress;
    }

    public Hash confirmationHash() {
        return confirmationHash;
    }

    public PersonName name() {
        return name;
    }
}
