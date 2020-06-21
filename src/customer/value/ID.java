package customer.value;

import java.util.UUID;

public final class ID {
    private final String value;

    private ID(String value) {
        this.value = value;
    }

    public static ID generate() {
        return new ID(UUID.randomUUID().toString());
    }

    public boolean equals(ID other) {
        return this.value == other.value;
    }
}
