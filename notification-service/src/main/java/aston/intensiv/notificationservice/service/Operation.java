package aston.intensiv.notificationservice.service;

import java.util.Optional;

public enum Operation {
    CREATE, DELETE;

    public static Optional<Operation> operation(String value) {
        for (Operation operation : values()) {
            if (operation.name().equalsIgnoreCase(value)) {
                return Optional.of(operation);
            }
        }
        return Optional.empty();
    }
}
