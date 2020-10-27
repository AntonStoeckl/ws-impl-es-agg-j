package domain.customer.command;

import domain.customer.value.ID;
import domain.customer.value.PersonName;

// TODO: should only have factory with scalars (also in master branch)

public final class ChangeCustomerName {
    public final ID customerID;
    public final PersonName name;

    private ChangeCustomerName(ID customerID, PersonName name) {
        this.customerID = customerID;
        this.name = name;
    }

    private ChangeCustomerName(String customerID, String givenName, String familyName) {
        this.customerID = ID.build(customerID);
        this.name = PersonName.build(givenName, familyName);
    }

    public static ChangeCustomerName build(ID customerID, PersonName name) {
        return new ChangeCustomerName(customerID, name);
    }

    public static ChangeCustomerName build(String customerID, String givenName, String familyName) {
        return new ChangeCustomerName(customerID, givenName, familyName);
    }
}
