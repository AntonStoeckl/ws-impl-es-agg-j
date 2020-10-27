package domain.customer;

public class CustomerState {
    public final String id;
    public final String emailAddress;
    public final String confirmationHash;
    public final String givenName;
    public final String familyName;
    public final Boolean isConfirmed;

    public CustomerState(String id, String emailAddress, String confirmationHash, String givenName, String familyName, Boolean isConfirmed) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.confirmationHash = confirmationHash;
        this.givenName = givenName;
        this.familyName = familyName;
        this.isConfirmed = isConfirmed;
    }
}
