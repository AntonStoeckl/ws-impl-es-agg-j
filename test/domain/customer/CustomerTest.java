package domain.customer;

import domain.customer.command.*;
import domain.customer.event.*;
import domain.customer.value.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    private ID customerID;
    private EmailAddress emailAddress;
    private EmailAddress changedEmailAddress;
    private Hash confirmationHash;
    private Hash wrongConfirmationHash;
    private Hash changedConfirmationHash;
    private PersonName name;
    private PersonName changedName;

    @BeforeEach
    public void beforeEach() {
        customerID = ID.generate();
        emailAddress = EmailAddress.build("john@doe.com");
        changedEmailAddress = EmailAddress.build("john+changed@doe.com");
        confirmationHash = Hash.generate();
        wrongConfirmationHash = Hash.generate();
        changedConfirmationHash = Hash.generate();
        name = PersonName.build("John", "Doe");
        changedName = PersonName.build("Jayne", "Doe");
    }

    @Test
    public void RegisterCustomer() {
        // When RegisterCustomer
        RegisterCustomer registerCustomer = RegisterCustomer.build(emailAddress.value, name.givenName, name.familyName);
        CustomerRegistered customerRegistered = Customer.register(registerCustomer);

        // Then CustomerRegistered
        assertNotNull(customerRegistered);

        //  and the payload should be as expected
        assertTrue(customerRegistered.customerID.equals(registerCustomer.customerID));
        assertTrue(customerRegistered.emailAddress.equals(registerCustomer.emailAddress));
        assertTrue(customerRegistered.confirmationHash.equals(registerCustomer.confirmationHash));
        assertTrue(customerRegistered.name.equals(registerCustomer.name));
    }

    @Test
    public void ConfirmEmailAddress() {
        // Given CustomerRegistered
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ConfirmCustomerEmailAddress
        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(customerID.value, confirmationHash.value);
        List<Event> recordedEvents = customer.confirmEmailAddress(command);

        // Then CustomerEmailAddressConfirmed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmed.class, recordedEvents.get(0).getClass());
        assertNotNull(recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        CustomerEmailAddressConfirmed event = (CustomerEmailAddressConfirmed) recordedEvents.get(0);
        assertTrue(event.customerID.equals(command.customerID));
    }

    @Test
    public void ConfirmEmailAddress_withWrongConfirmationHash() {
        // Given CustomerRegistered
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ConfirmCustomerEmailAddress (with wrong confirmationHash)
        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(customerID.value, wrongConfirmationHash.value);
        List<Event> recordedEvents = customer.confirmEmailAddress(command);

        // Then CustomerEmailAddressConfirmationFailed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmationFailed.class, recordedEvents.get(0).getClass());
        assertNotNull(recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        CustomerEmailAddressConfirmationFailed event = (CustomerEmailAddressConfirmationFailed) recordedEvents.get(0);
        assertTrue(event.customerID.equals(command.customerID));
    }

    @Test
    public void ConfirmEmailAddress_whenItWasAlreadyConfirmed() {
        // Given CustomerRegistered
        //   and CustomerEmailAddressConfirmed
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerEmailAddressConfirmed.build(customerID)
                )
        );

        // When ConfirmCustomerEmailAddress
        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(customerID.value, confirmationHash.value);
        List<Event> recordedEvents = customer.confirmEmailAddress(command);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ConfirmEmailAddress_withWrongConfirmationHash_whenItWasAlreadyConfirmed() {
        // Given CustomerRegistered
        //   and CustomerEmailAddressConfirmed
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerEmailAddressConfirmed.build(customerID)
                )
        );

        // When ConfirmCustomerEmailAddress (with wrong confirmationHash)
        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(customerID.value, wrongConfirmationHash.value);
        List<Event> recordedEvents = customer.confirmEmailAddress(command);

        // Then CustomerEmailAddressConfirmationFailed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmationFailed.class, recordedEvents.get(0).getClass());
        assertNotNull(recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        CustomerEmailAddressConfirmationFailed event = (CustomerEmailAddressConfirmationFailed) recordedEvents.get(0);
        assertTrue(event.customerID.equals(command.customerID));
    }

    @Test
    public void ChangeCustomerEmailAddress() {
        // Given CustomerRegistered
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ChangeCustomerEmailAddress
        ChangeCustomerEmailAddress command = ChangeCustomerEmailAddress.build(customerID.value, changedEmailAddress.value);
        List<Event> recordedEvents = customer.changeEmailAddress(command);

        // Then CustomerEmailAddressChanged
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressChanged.class, recordedEvents.get(0).getClass());
        assertNotNull(recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        CustomerEmailAddressChanged event = (CustomerEmailAddressChanged) recordedEvents.get(0);
        assertTrue(event.customerID.equals(command.customerID));
        assertTrue(event.emailAddress.equals(command.emailAddress));
        assertTrue(event.confirmationHash.equals(command.confirmationHash));
    }

    @Test
    public void ChangeCustomerEmailAddress_withUnchangedEmailAddress() {
        // Given CustomerRegistered
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ChangeCustomerEmailAddress
        ChangeCustomerEmailAddress command = ChangeCustomerEmailAddress.build(customerID.value, emailAddress.value);
        List<Event> recordedEvents = customer.changeEmailAddress(command);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ChangeCustomerEmailAddress_whenItWasAlreadyChanged() {
        // Given CustomerRegistered
        //   and CustomerEmailAddressChanged
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerEmailAddressChanged.build(customerID, changedEmailAddress, changedConfirmationHash)
                )
        );

        // When ChangeCustomerEmailAddress
        ChangeCustomerEmailAddress command = ChangeCustomerEmailAddress.build(customerID.value, changedEmailAddress.value);
        List<Event> recordedEvents = customer.changeEmailAddress(command);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ConfirmCustomerEmailAddress_whenItWasPreviouslyConfirmedAndThenChanged() {
        // Given CustomerRegistered
        //   and CustomerEmailAddressConfirmed
        //   and CustomerEmailAddressChanged
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerEmailAddressConfirmed.build(customerID),
                        CustomerEmailAddressChanged.build(customerID, changedEmailAddress, changedConfirmationHash)
                )
        );

        // When ConfirmCustomerEmailAddress
        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(customerID.value, changedConfirmationHash.value);
        List<Event> recordedEvents = customer.confirmEmailAddress(command);

        // Then CustomerEmailAddressConfirmed
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerEmailAddressConfirmed.class, recordedEvents.get(0).getClass());
        assertNotNull(recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        CustomerEmailAddressConfirmed event = (CustomerEmailAddressConfirmed) recordedEvents.get(0);
        assertTrue(event.customerID.equals(command.customerID));
    }

    @Test
    public void ChangeCustomerName() {
        // Given CustomerRegistered
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ChangeCustomerName
        ChangeCustomerName command = ChangeCustomerName.build(customerID, changedName);
        List<Event> recordedEvents = customer.changeName(command);

        // Then CustomerNameChanged
        assertEquals(1, recordedEvents.size());
        assertEquals(CustomerNameChanged.class, recordedEvents.get(0).getClass());
        assertNotNull(recordedEvents.get(0).getClass());

        //  and the payload should be as expected
        CustomerNameChanged event = (CustomerNameChanged) recordedEvents.get(0);
        assertTrue(event.customerID.equals(command.customerID));
        assertTrue(event.name.equals(command.name));
    }

    @Test
    public void ChangeCustomerName_withUnchangedName() {
        // Given CustomerRegistered
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name)
                )
        );

        // When ChangeCustomerName
        ChangeCustomerName command = ChangeCustomerName.build(customerID, name);
        List<Event> recordedEvents = customer.changeName(command);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }

    @Test
    public void ChangeCustomerName_whenItWasAlreadyChanged() {
        // Given CustomerRegistered
        //   and CustomerNameChanged
        Customer customer = Customer.reconstitute(
                List.of(
                        CustomerRegistered.build(customerID, emailAddress, confirmationHash, name),
                        CustomerNameChanged.build(customerID, changedName)
                )
        );

        // When ChangeCustomerName
        ChangeCustomerName command = ChangeCustomerName.build(customerID, changedName);
        List<Event> recordedEvents = customer.changeName(command);

        // Then no event
        assertEquals(0, recordedEvents.size());
    }
}