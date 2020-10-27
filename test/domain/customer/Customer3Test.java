package domain.customer;

import domain.customer.command.ChangeCustomerEmailAddress;
import domain.customer.command.ChangeCustomerName;
import domain.customer.command.ConfirmCustomerEmailAddress;
import domain.customer.command.RegisterCustomer;
import domain.customer.value.EmailAddress;
import domain.customer.value.Hash;
import domain.customer.value.ID;
import domain.customer.value.PersonName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Customer3Test {
    // assigned via givenARegisteredCustomer()
    private Customer3 registeredCustomer;
    private ID customerID;
    private Hash confirmationHash;

    // assigned via beforeEach() + used as input in given....() methods and in test cases
    private PersonName name;
    private EmailAddress emailAddress;
    private EmailAddress changedEmailAddress;
    private Hash wrongConfirmationHash;
    private Hash changedConfirmationHash;
    private PersonName changedName;

    @BeforeEach
    public void beforeEach() {
        emailAddress = EmailAddress.build("john@doe.com");
        changedEmailAddress = EmailAddress.build("john+changed@doe.com");
        wrongConfirmationHash = Hash.generate();
        changedConfirmationHash = Hash.generate();
        name = PersonName.build("John", "Doe");
        changedName = PersonName.build("Jayne", "Doe");
    }

    @Test
    public void registerCustomer() {
        // When
        var command = RegisterCustomer.build(emailAddress.value, name.givenName, name.familyName);
        var customer = Customer3.register(command);

        // Then
        assertNotNull(customer);

        // and it should expose the expected state
        var state = customer.toCustomerState();
        assertEquals(command.customerID.value, state.id);
        assertEquals(command.name.givenName, state.givenName);
        assertEquals(command.name.familyName, state.familyName);
        assertEquals(command.emailAddress.value, state.emailAddress);
        assertEquals(command.confirmationHash.value, state.confirmationHash);
        assertFalse(state.isConfirmed);
    }

    @Test
    public void confirmCustomerEmailAddress() {
        // Given
        givenARegisteredCustomer();

        // When / Then
        var command = ConfirmCustomerEmailAddress.build(customerID.value, confirmationHash.value);
        assertDoesNotThrow(() -> registeredCustomer.confirmEmailAddress(command));

        // and the emailAddress should be confirmed
        var state = registeredCustomer.toCustomerState();
        assertTrue(state.isConfirmed);
    }

    @Test
    public void confirmCustomerEmailAddress_withWrongConfirmationHash() {
        // Given
        givenARegisteredCustomer();

        // When / Then
        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(customerID.value, wrongConfirmationHash.value);
        assertThrows(Exception.class, () -> registeredCustomer.confirmEmailAddress(command));

        // and the emailAddress should not be confirmed
        var state = registeredCustomer.toCustomerState();
        assertFalse(state.isConfirmed);
    }

    @Test
    public void confirmCustomerEmailAddress_whenItWasAlreadyConfirmed() {
        // Given
        givenARegisteredCustomer();
        givenEmailAddressWasConfirmed();

        // When / Then
        var command = ConfirmCustomerEmailAddress.build(customerID.value, confirmationHash.value);
        assertDoesNotThrow(() -> registeredCustomer.confirmEmailAddress(command));

        // and the emailAddress should still be confirmed
        var state = registeredCustomer.toCustomerState();
        assertTrue(state.isConfirmed);
    }

    @Test
    public void confirmCustomerEmailAddress_withWrongConfirmationHash_whenItWasAlreadyConfirmed() {
        // Given
        givenARegisteredCustomer();
        givenEmailAddressWasConfirmed();

        // When / Then
        var otherCommand = ConfirmCustomerEmailAddress.build(customerID.value, wrongConfirmationHash.value);
        assertThrows(Exception.class, () -> registeredCustomer.confirmEmailAddress(otherCommand));

        // and the emailAddress should still be confirmed
        var state = registeredCustomer.toCustomerState();
        assertTrue(state.isConfirmed);
    }

    @Test
    public void changeCustomerEmailAddress() {
        // Given
        givenARegisteredCustomer();

        // When / Then
        var command = ChangeCustomerEmailAddress.build(customerID.value, changedEmailAddress.value);
        registeredCustomer.changeEmailAddress(command);

        // and the emailAddress and confirmationHash should be changed and the emailAddress should be unconfirmed
        var state = registeredCustomer.toCustomerState();
        assertEquals(command.emailAddress.value, state.emailAddress);
        assertEquals(command.confirmationHash.value, state.confirmationHash);
        assertFalse(state.isConfirmed);
    }

    @Test
    public void changeCustomerEmailAddress_withUnchangedEmailAddress() {
        // Given
        givenARegisteredCustomer();

        // When / Then
        var command = ChangeCustomerEmailAddress.build(customerID.value, emailAddress.value);
        registeredCustomer.changeEmailAddress(command);

        // and the emailAddress should still be the same
        var state = registeredCustomer.toCustomerState();
        assertEquals(emailAddress.value, state.emailAddress);
    }

    @Test
    public void changeCustomerEmailAddress_whenItWasAlreadyChanged() {
        // Given
        givenARegisteredCustomer();
        givenCustomerEmailAddressWasChanged();

        // When / Then
        var command = ChangeCustomerEmailAddress.build(customerID.value, changedEmailAddress.value);
        registeredCustomer.changeEmailAddress(command);

        // and the emailAddress should still be the same
        var state = registeredCustomer.toCustomerState();
        assertEquals(changedEmailAddress.value, state.emailAddress);
    }

    @Test
    public void confirmCustomerEmailAddress_whenItWasPreviouslyConfirmedAndThenChanged() {
        // Given
        givenARegisteredCustomer();
        givenEmailAddressWasConfirmed();
        givenCustomerEmailAddressWasChanged();

        // When // Then
        var command = ConfirmCustomerEmailAddress.build(customerID.value, changedConfirmationHash.value);
        assertDoesNotThrow(() -> registeredCustomer.confirmEmailAddress(command));

        // and the emailAddress should be confirmed
        var state = registeredCustomer.toCustomerState();
        assertTrue(state.isConfirmed);
    }

    @Test
    public void changeCustomerName() {
        // Given
        givenARegisteredCustomer();

        // When
        var command = ChangeCustomerName.build(customerID.value, changedName.givenName, changedName.familyName);
        registeredCustomer.changeName(command);

        // and it should expose the expected state
        var state = registeredCustomer.toCustomerState();
        assertEquals(command.name.givenName, state.givenName);
        assertEquals(command.name.familyName, state.familyName);
    }

    @Test
    public void changeCustomerName_withUnchangedName() {
        // Given
        givenARegisteredCustomer();

        // When
        var command = ChangeCustomerName.build(customerID.value, name.givenName, name.familyName);
        registeredCustomer.changeName(command);

        // and it should expose the expected state
        var state = registeredCustomer.toCustomerState();
        assertEquals(command.name.givenName, state.givenName);
        assertEquals(command.name.familyName, state.familyName);
    }

    @Test
    public void changeCustomerName_whenItWasAlreadyChanged() {
        // Given
        givenARegisteredCustomer();
        givenCustomerNameWasChanged();

        // When
        var command = ChangeCustomerName.build(customerID.value, changedName.givenName, changedName.familyName);
        registeredCustomer.changeName(command);

        // and it should expose the expected state
        var state = registeredCustomer.toCustomerState();
        assertEquals(command.name.givenName, state.givenName);
        assertEquals(command.name.familyName, state.familyName);
    }

    /**
     * Helper methods to set up the Given state
     */
    private void givenARegisteredCustomer() {
        var register = RegisterCustomer.build(emailAddress.value, name.givenName, name.familyName);
        customerID = register.customerID;
        confirmationHash = register.confirmationHash;
        registeredCustomer = Customer3.register(register);
    }

    private void givenEmailAddressWasConfirmed() {
        var command = ConfirmCustomerEmailAddress.build(customerID.value, confirmationHash.value);

        try {
            registeredCustomer.confirmEmailAddress(command);
        } catch (Exception e) {
            fail("unexpected error in givenEmailAddressWasConfirmed: " + e.getMessage());
        }
    }

    private void givenCustomerEmailAddressWasChanged() {
        var command = ChangeCustomerEmailAddress.build(customerID.value, changedEmailAddress.value);
        changedConfirmationHash = command.confirmationHash;
        registeredCustomer.changeEmailAddress(command);
    }

    private void givenCustomerNameWasChanged() {
        var command = ChangeCustomerName.build(customerID.value, changedName.givenName, changedName.familyName);
        registeredCustomer.changeName(command);
    }
}