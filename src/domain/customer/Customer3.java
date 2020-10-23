package domain.customer;

import domain.customer.command.ConfirmCustomerEmailAddress;
import domain.customer.command.RegisterCustomer;
import domain.customer.value.EmailAddress;
import domain.customer.value.Hash;
import domain.customer.value.PersonName;

public class Customer3 {
    private EmailAddress emailAddress;
    private Hash confirmationHash;
    private boolean isEmailAddressConfirmed;
    private PersonName name;

    private Customer3(EmailAddress emailAddress, Hash confirmationHash, PersonName name) {
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
        this.name = name;
    }

    public static Customer3 register(RegisterCustomer command) {
        return new Customer3(command.emailAddress, command.confirmationHash, command.name);
    }

    public void confirmEmailAddress(ConfirmCustomerEmailAddress command) throws Exception {
        if (!command.confirmationHash.equals(confirmationHash)) {
            throw new Exception("confirmation hash does not match");
        }

        isEmailAddressConfirmed = true;
    }
}
