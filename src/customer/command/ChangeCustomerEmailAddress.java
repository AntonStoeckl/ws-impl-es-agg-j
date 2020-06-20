package customer.command;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;

public class ChangeCustomerEmailAddress {
    public final ID customerID;
    public final EmailAddress emailAddress;
    public final Hash confirmationHash;

    public ChangeCustomerEmailAddress(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        this.customerID = customerID;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
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
}
