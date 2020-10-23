package domain.customer;

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
    void registerCustomer() {
        // Given
        RegisterCustomer command = RegisterCustomer.build(emailAddress.value, name.givenName, name.familyName);

        // When
        Customer3 customer = Customer3.register(command);

        // Then
        assertNotNull(customer);
    }

    @Test
    void confirmEmailAddress() {
        // Given
        RegisterCustomer register = RegisterCustomer.build(emailAddress.value, name.givenName, name.familyName);
        Customer3 customer = Customer3.register(register);

        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(register.customerID.value, register.confirmationHash.value);

        // When / Then
        assertDoesNotThrow(()->{customer.confirmEmailAddress(command);});

        fail();
    }

    @Test
    public void confirmEmailAddress_withWrongConfirmationHash() {
        // Given
        RegisterCustomer register = RegisterCustomer.build(emailAddress.value, name.givenName, name.familyName);
        Customer3 customer = Customer3.register(register);

        ConfirmCustomerEmailAddress command = ConfirmCustomerEmailAddress.build(register.customerID.value, wrongConfirmationHash.value);

        // When / Then
        assertThrows(Exception.class, ()->{customer.confirmEmailAddress(command);});
    }

    @Test
    public void confirmEmailAddress_whenItWasAlreadyConfirmed() {

    }

    @Test
    public void confirmEmailAddress_withWrongConfirmationHash_whenItWasAlreadyConfirmed() {

    }
}