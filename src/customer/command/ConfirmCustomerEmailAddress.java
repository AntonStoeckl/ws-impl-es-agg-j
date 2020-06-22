package customer.command;

import customer.value.Hash;
import customer.value.ID;

public final class ConfirmCustomerEmailAddress {
    public final ID customerID;
    public final Hash confirmationHash;

    private ConfirmCustomerEmailAddress(String customerID, String confirmationHash) {
        this.customerID = ID.build(customerID);
        this.confirmationHash = Hash.build(confirmationHash);
    }

    public static ConfirmCustomerEmailAddress build(String customerID, String confirmationHash) {
        return new ConfirmCustomerEmailAddress(customerID, confirmationHash);
    }
}
