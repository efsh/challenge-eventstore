package net.intelie.challenges;

import java.util.Objects;

/**
 * This is just an event stub, feel free to expand it if needed.
 */
public class Event {
    private final String type;
    private final long timestamp;

    public Event(String type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public String type() {
        return type;
    }

    public long timestamp() {
        return timestamp;
    }

    @Override
    // improving the standard equals(Object) implementation we have a reliable method
    // call containsKey() in the hashmap
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return type.equals(event.type);
    }

    @Override
    // improving the standard hashCode() implementation we avoid/reduce key collisions
    // in the hashmap
    public int hashCode() {
        return Objects.hash(type);
    }
}
