package customer.event;

import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;

public final class CustomerEmailAddressChanged implements Event {
    public final ID customerID;
    public final EmailAddress emailAddress;
    public final Hash confirmationHash;

    private CustomerEmailAddressChanged(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        this.customerID = customerID;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
    }

    public static CustomerEmailAddressChanged build(ID customerID, EmailAddress emailAddress, Hash confirmationHash) {
        return new CustomerEmailAddressChanged(customerID, emailAddress, confirmationHash);
    }
}
