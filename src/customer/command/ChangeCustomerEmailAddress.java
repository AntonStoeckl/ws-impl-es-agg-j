package customer.command;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;

public final class ChangeCustomerEmailAddress {
    public final ID customerID;
    public final EmailAddress emailAddress;
    public final Hash confirmationHash;

    private ChangeCustomerEmailAddress(String customerID, String emailAddress) {
        this.customerID = ID.build(customerID);
        this.emailAddress = EmailAddress.build(emailAddress);
        this.confirmationHash = Hash.generate();
    }

    public static ChangeCustomerEmailAddress build(String customerID, String emailAddress) {
        return new ChangeCustomerEmailAddress(customerID, emailAddress);
    }
}
