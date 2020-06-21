package customer.event;

import customer.value.ID;

public final class CustomerEmailAddressConfirmed implements Event {
    public final ID customerID;

    private CustomerEmailAddressConfirmed(ID customerID) {
        this.customerID = customerID;
    }

    public static CustomerEmailAddressConfirmed build(ID customerID) {
        return new CustomerEmailAddressConfirmed(customerID);
    }
}
