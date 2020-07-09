package domain.customer.command;

import domain.customer.value.ID;
import domain.customer.value.PersonName;

public final class ChangeCustomerName {
    public final ID customerID;
    public final PersonName name;

    private ChangeCustomerName(ID customerID, PersonName name) {
        this.customerID = customerID;
        this.name = name;
    }

    public static ChangeCustomerName build(ID customerID, PersonName name) {
        return new ChangeCustomerName(customerID, name);
    }
}
