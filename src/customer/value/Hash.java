package customer.value;

import java.util.UUID;

public class Hash {
    private final String value;

    private Hash(String value) {
        this.value = value;
    }

    public static Hash generate() {
        return new Hash(UUID.randomUUID().toString());
    }

    public boolean equals(Hash other) {
        return this.value == other.value;
    }
}
