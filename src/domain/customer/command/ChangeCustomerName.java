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

    public static ChangeCustomerName build(String customerID, String givenName, String familyName) {
        return new ChangeCustomerName(
                ID.build(customerID),
                PersonName.build(givenName, familyName)
        );
    }
}
