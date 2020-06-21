package customer.value;

public final class EmailAddress {
    private final String value;

    private EmailAddress(String value) {
        this.value = value;
    }

    public static EmailAddress build(String emailAddress) {
        return new EmailAddress(emailAddress);
    }

    public boolean equals(EmailAddress other) {
        return value == other.value;
    }
}
