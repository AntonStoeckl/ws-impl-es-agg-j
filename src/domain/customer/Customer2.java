package domain.customer;

import domain.customer.command.ChangeCustomerEmailAddress;
import domain.customer.command.ChangeCustomerName;
import domain.customer.command.ConfirmCustomerEmailAddress;
import domain.customer.command.RegisterCustomer;
import domain.customer.event.*;
import domain.customer.value.EmailAddress;
import domain.customer.value.Hash;
import domain.customer.value.PersonName;

import java.util.ArrayList;
import java.util.List;

/*
 This version of a Customer Aggregate is Event-Sourced and each behavior directly returns the events that happened.

 Enable the disabled test cases (remove the @Disabled annotation) in Customer1Test one by one and make them all green!
 The first test case (RegisterCustomer) is already enabled for you to start.
 */
public final class Customer2 {
    private EmailAddress emailAddress;
    private Hash confirmationHash;
    private boolean isEmailAddressConfirmed;
    private PersonName name;

    private List<Event> recordedEvents;

    private Customer2() {
        recordedEvents = new ArrayList<>();
    }

    public static Customer2 register(RegisterCustomer command) {
        var customer = new Customer2();

        customer.recordThat(
                CustomerRegistered.build(
                        command.customerID,
                        command.emailAddress,
                        command.confirmationHash,
                        command.name
                )
        );

        return customer;
    }

    private void recordThat(Event event) {
        recordedEvents.add(event);
//        apply(event);
    }

    public List<Event> getRecordedEvents() {
        return recordedEvents;
    }

    public static Customer2 reconstitute(List<Event> events) {
        var customer = new Customer2();

        for (Event event: events) {
            customer.apply(event);
        }

        return customer;
    }

    public void confirmEmailAddress(ConfirmCustomerEmailAddress command) {
        if (!confirmationHash.equals(command.confirmationHash)) {
            recordThat(
                    CustomerEmailAddressConfirmationFailed.build(command.customerID)
            );
        }

        if (!isEmailAddressConfirmed) {
            recordThat(
                    CustomerEmailAddressConfirmed.build(command.customerID
                    )
            );
        }
    }

    public void changeEmailAddress(ChangeCustomerEmailAddress command) {
        if (!command.emailAddress.equals(emailAddress)) {
            recordThat(
                    CustomerEmailAddressChanged.build(
                            command.customerID, command.emailAddress, command.confirmationHash
                    )
            );
        }
    }

    public void changeName(ChangeCustomerName command) {
        if (!command.name.equals(name)) {
            recordThat(
                    CustomerNameChanged.build(
                            command.customerID, command.name
                    )
            );
        }
    }

    private void apply(Event event) {
        if (event.getClass() == CustomerRegistered.class) {
            emailAddress = ((CustomerRegistered) event).emailAddress;
            confirmationHash = ((CustomerRegistered) event).confirmationHash;
            name = ((CustomerRegistered) event).name;
        } else if (event.getClass() == CustomerEmailAddressConfirmed.class) {
            isEmailAddressConfirmed = true;
        } else if (event.getClass() == CustomerEmailAddressChanged.class) {
            emailAddress = ((CustomerEmailAddressChanged) event).emailAddress;
            confirmationHash = ((CustomerEmailAddressChanged) event).confirmationHash;
            isEmailAddressConfirmed = false;
        } else if (event.getClass() == CustomerNameChanged.class) {
            name = ((CustomerNameChanged) event).name;
        }
    }
}

