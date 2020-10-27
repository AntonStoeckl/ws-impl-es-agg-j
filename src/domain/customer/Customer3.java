package domain.customer;

import domain.customer.command.ChangeCustomerEmailAddress;
import domain.customer.command.ChangeCustomerName;
import domain.customer.command.ConfirmCustomerEmailAddress;
import domain.customer.command.RegisterCustomer;
import domain.customer.value.EmailAddress;
import domain.customer.value.Hash;
import domain.customer.value.ID;
import domain.customer.value.PersonName;

public class Customer3 {
    private final ID id;
    private EmailAddress emailAddress;
    private Hash confirmationHash;
    private boolean isEmailAddressConfirmed;
    private PersonName name;

    private Customer3(ID id, EmailAddress emailAddress, Hash confirmationHash, PersonName name) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
        this.name = name;
    }

    public static Customer3 register(RegisterCustomer command) {
        return new Customer3(command.customerID, command.emailAddress, command.confirmationHash, command.name);
    }

    public void confirmEmailAddress(ConfirmCustomerEmailAddress command) throws Exception {
        if (!command.confirmationHash.equals(confirmationHash)) {
            throw new Exception("confirmation hash does not match");
        }

        if (isEmailAddressConfirmed) {
            return;
        }

        isEmailAddressConfirmed = true;
    }

    public void changeEmailAddress(ChangeCustomerEmailAddress command) {
        if (command.emailAddress.equals(emailAddress)) {
            return;
        }

        emailAddress = command.emailAddress;
        confirmationHash = command.confirmationHash;
        isEmailAddressConfirmed = false;
    }

    public void changeName(ChangeCustomerName command) {
        if (command.name.equals(name)) {
            return;
        }

        name = command.name;
    }

    public CustomerState toCustomerState() {
        return new CustomerState(id.value, emailAddress.value, confirmationHash.value, name.givenName, name.familyName, isEmailAddressConfirmed);
    }
}
