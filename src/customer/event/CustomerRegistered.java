package customer.event;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;
import customer.value.PersonName;

public final class CustomerRegistered implements Event {
    private final ID customerID;
    private final EmailAddress emailAddress;
    private final Hash confirmationHash;
    private final PersonName name;

    private CustomerRegistered(ID customerID, EmailAddress emailAddress, Hash confirmationHash, PersonName name) {
        this.customerID = customerID;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
        this.name = name;
    }

    public static CustomerRegistered build(
            ID id,
            EmailAddress emailAddress,
            Hash confirmationHash,
            PersonName name
    ) {
        return new CustomerRegistered(id, emailAddress, confirmationHash, name);
    }

    public ID customerID() {
        return customerID;
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
