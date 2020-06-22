package domain.customer.event;

import domain.customer.value.*;

public class CustomerNameChanged implements Event {
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
