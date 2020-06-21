package customer.command;

import customer.value.Hash;
import customer.value.ID;

public final class ConfirmCustomerEmailAddress {
    public final ID customerID;
    public final Hash confirmationHash;

    public ConfirmCustomerEmailAddress(ID customerID, Hash confirmationHash) {
        this.customerID = customerID;
        this.confirmationHash = confirmationHash;
    }

    public ID customerID() {
        return customerID;
    }

    public Hash confirmationHash() {
        return confirmationHash;
    }
}
