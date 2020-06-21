package customer.command;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;

public final class ChangeCustomerEmailAddress {
    public final ID customerID;
    public final EmailAddress emailAddress;
    public final Hash confirmationHash;

    private ChangeCustomerEmailAddress(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        this.customerID = customerID;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
    }

    public static ChangeCustomerEmailAddress build(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        return new ChangeCustomerEmailAddress(customerID, emailAddress, confirmationHash);
    }
}
