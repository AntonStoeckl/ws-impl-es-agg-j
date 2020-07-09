package domain.customer.event;

import domain.customer.value.ID;
import domain.customer.value.PersonName;

public final class CustomerNameChanged implements Event {
    public final ID customerID;
    public final PersonName name;

    private CustomerNameChanged(ID customerID, PersonName name) {
        this.customerID = customerID;
        this.name = name;
    }

    public static CustomerNameChanged build(ID customerID, PersonName name) {
        return new CustomerNameChanged(customerID, name);
    }
}
