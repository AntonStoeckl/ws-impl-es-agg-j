package customer;

import customer.command.ChangeCustomerEmailAddress;
import customer.command.ConfirmCustomerEmailAddress;
import customer.command.RegisterCustomer;
import customer.event.CustomerEmailAddressChanged;
import customer.event.CustomerEmailAddressConfirmationFailed;
import customer.event.CustomerEmailAddressConfirmed;
import customer.event.CustomerRegistered;
import customer.value.EmailAddress;
import customer.value.Hash;
import customer.value.ID;
import customer.value.PersonName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    public void RegisterCustomer() {
        // When RegisterCustomer
        var registerCustomer = new RegisterCustomer("john@doe.com", "John", "Doe");
        var customerRegistered = Customer.register(registerCustomer);

        // Then CustomerRegistered
        assertEquals(CustomerRegistered.class, customerRegistered.getClass());

        //  and the payload should be as expected
        assertTrue(registerCustomer.id().equals(customerRegistered.customerID()));
        assertTrue(registerCustomer.emailAddress().equals(customerRegistered.emailAddress()));
        assertTrue(registerCustomer.confirmationHash().equals(customerRegistered.confirmationHash()));
        assertTrue(registerCustomer.name().equals(customerRegistered.name()));
    }

    @Test
    public void ConfirmEmailAddress() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");

        // Given CustomerRegistered
        var customer = Customer.reconstitute(
            List.of(
                CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
            )
        );

        // When ConfirmCustomerEmailAddress
        var confirmCustomerEmailAddress = new ConfirmCustomerEmailAddress(customerID, confirmationHash);
        var recordedEvents = customer.confirmEmailAddress(confirmCustomerEmailAddress);

        // Then CustomerEmailAddressConfirmed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmed.class, recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        var customerRegistered = (CustomerEmailAddressConfirmed) recordedEvents.get(0);
        assertTrue(customerID.equals(customerRegistered.customerID()));
    }

    @Test
    public void ConfirmEmailAddress_withWrongConfirmationHash() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");

        // Given CustomerRegistered
        var customer = Customer.reconstitute(
            List.of(
                CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
            )
        );

        // When ConfirmCustomerEmailAddress (with wrong confirmationHash)
        var wrongConfirmationHash = Hash.generate();
        var confirmCustomerEmailAddress = new ConfirmCustomerEmailAddress(customerID, wrongConfirmationHash);
        var recordedEvents = customer.confirmEmailAddress(confirmCustomerEmailAddress);

        // Then CustomerEmailAddressConfirmationFailed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmationFailed.class, recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        var customerEmailAddressConfirmationFailed = (CustomerEmailAddressConfirmationFailed) recordedEvents.get(0);
        assertTrue(customerID.equals(customerEmailAddressConfirmationFailed.customerID()));
    }

    @Test
    public void ConfirmEmailAddress_whenItWasAlreadyConfirmed() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");

        // Given CustomerRegistered
        //   and CustomerEmailAddressConfirmed
        var customer = Customer.reconstitute(
            List.of(
                CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                CustomerEmailAddressConfirmed.build(customerID)
            )
        );

        // When ConfirmCustomerEmailAddress
        var confirmCustomerEmailAddress = new ConfirmCustomerEmailAddress(customerID, confirmationHash);
        var recordedEvents = customer.confirmEmailAddress(confirmCustomerEmailAddress);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ConfirmEmailAddress_withWrongConfirmationHash_whenItWasAlreadyConfirmed() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");

        // Given CustomerRegistered
        //   and CustomerEmailAddressConfirmed
        var customer = Customer.reconstitute(
            List.of(
                CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                CustomerEmailAddressConfirmed.build(customerID)
            )
        );

        // When ConfirmCustomerEmailAddress (with wrong confirmationHash)
        var wrongConfirmationHash = Hash.generate();
        var confirmCustomerEmailAddress = new ConfirmCustomerEmailAddress(customerID, wrongConfirmationHash);
        var recordedEvents = customer.confirmEmailAddress(confirmCustomerEmailAddress);

        // Then CustomerEmailAddressConfirmationFailed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmationFailed.class, recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        var customerEmailAddressConfirmationFailed = (CustomerEmailAddressConfirmationFailed) recordedEvents.get(0);
        assertTrue(customerID.equals(customerEmailAddressConfirmationFailed.customerID()));
    }

    @Test
    public void ChangeCustomerEmailAddress() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");
        var changedEmailAddress = EmailAddress.build("john+changed@doe.com");
        var changedConfirmationHash = Hash.generate();

        // Given CustomerRegistered
        var customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ChangeCustomerEmailAddress
        var changeCustomerEmailAddress = new ChangeCustomerEmailAddress(customerID, changedEmailAddress, changedConfirmationHash);
        var recordedEvents = customer.ChangeEmailAddress(changeCustomerEmailAddress);

        // Then CustomerEmailAddressChanged
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressChanged.class, recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        var customerEmailAddressChanged = (CustomerEmailAddressChanged) recordedEvents.get(0);
        assertTrue(customerID.equals(customerEmailAddressChanged.customerID()));
        assertTrue(changedEmailAddress.equals(customerEmailAddressChanged.emailAddress()));
        assertTrue(changedConfirmationHash.equals(customerEmailAddressChanged.confirmationHash()));
    }

    @Test
    public void ChangeCustomerEmailAddress_withUnchangedEmailAddress() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");

        // Given CustomerRegistered
        var customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ChangeCustomerEmailAddress
        var changeCustomerEmailAddress = new ChangeCustomerEmailAddress(customerID, emailAddress, confirmationHash);
        var recordedEvents = customer.ChangeEmailAddress(changeCustomerEmailAddress);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ChangeCustomerEmailAddress_whenItWasAlreadyChanged() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");
        var changedEmailAddress = EmailAddress.build("john+changed@doe.com");
        var changedConfirmationHash = Hash.generate();

        // Given CustomerRegistered
        var customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerEmailAddressChanged.build(customerID, changedEmailAddress, changedConfirmationHash)
                )
        );

        // When ChangeCustomerEmailAddress
        var changeCustomerEmailAddress = new ChangeCustomerEmailAddress(customerID, changedEmailAddress, changedConfirmationHash);
        var recordedEvents = customer.ChangeEmailAddress(changeCustomerEmailAddress);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ConfirmCustomerEmailAddress_whenItWasChanged() {
        var customerID = ID.generate();
        var emailAddress = EmailAddress.build("john@doe.com");
        var confirmationHash = Hash.generate();
        var name = PersonName.build("John", "Doe");
        var changedEmailAddress = EmailAddress.build("john+changed@doe.com");
        var changedConfirmationHash = Hash.generate();

        // Given CustomerRegistered
        var customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerEmailAddressConfirmed.build(customerID),
                        CustomerEmailAddressChanged.build(customerID, changedEmailAddress, changedConfirmationHash)
                )
        );

        // When ConfirmCustomerEmailAddress
        var confirmCustomerEmailAddress = new ConfirmCustomerEmailAddress(customerID, changedConfirmationHash);
        var recordedEvents = customer.confirmEmailAddress(confirmCustomerEmailAddress);

        // Then CustomerEmailAddressConfirmed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmed.class, recordedEvents.get(0).getClass());
    }
}