package customer.command;

import customer.value.Hash;
import customer.value.ID;

public final class ConfirmCustomerEmailAddress {
    public final ID customerID;
    public final Hash confirmationHash;

    private ConfirmCustomerEmailAddress(ID customerID, Hash confirmationHash) {
        this.customerID = customerID;
        this.confirmationHash = confirmationHash;
    }

    public static ConfirmCustomerEmailAddress build(ID customerID, Hash confirmationHash) {
        return new ConfirmCustomerEmailAddress(customerID, confirmationHash);
    }

    public ID customerID() {
        return customerID;
    }

    public Hash confirmationHash() {
        return confirmationHash;
    }
}
