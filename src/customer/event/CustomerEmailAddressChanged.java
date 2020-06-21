package customer.event;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;

public final class CustomerEmailAddressChanged implements Event {
    private final ID customerID;
    private final EmailAddress emailAddress;
    private final Hash confirmationHash;

    private CustomerEmailAddressChanged(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        this.customerID = customerID;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
    }

    public static CustomerEmailAddressChanged build(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        return new CustomerEmailAddressChanged(customerID, emailAddress, confirmationHash);
    }

    public ID customerID() {
        return customerID;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }
}
