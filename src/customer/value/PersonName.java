package customer.value;

public final class PersonName {
    private final String givenName;
    private final String familyName;

    private PersonName(String givenName, String familyName) {
        this.givenName = givenName;
        this.familyName = familyName;
    }

    public static PersonName build(String givenName, String familyName) {
        return new PersonName(givenName, familyName);
    }

    public boolean equals(PersonName other) {
        if (!this.givenName.equals(other.givenName)) return false;

        return this.familyName.equals(other.familyName);
    }
}
