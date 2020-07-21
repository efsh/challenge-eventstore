package net.intelie.challenges;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventStoreMemory implements EventStore{

    // thread-safe
    final private Map<String, List<Event>> eventMap = new ConcurrentHashMap<>();

    @Override
    public void insert(final Event event) {

        final List<Event> events;

        if (this.eventMap.containsKey(event.type())) {

            // event type exists, just add new event at the tail
            events = this.eventMap.get(event.type());

        } else {

            // event type doesn't exist, so we need to create a new entry in the hashmap
            events = new LinkedList<>();

        }

        events.add(event);
        this.eventMap.put(event.type(), events);

    }

    @Override
    public void removeAll(final String type) {

        try {
            this.eventMap.remove(type);
        } catch (final NullPointerException e) {
            // just to prevent exception if the specified key is null
        }

    }

    @Override
    public EventIterator query(final String type, final long startTime, final long endTime) {

        Event[] eventsMatch = {};

        if (this.eventMap.containsKey(type)) {

            final List<Event> allEvents = this.eventMap.get(type);

            // filter only events in range [startTime, endTime)
            eventsMatch = allEvents
                    .stream()
                    .filter(e -> e.timestamp() >= startTime && e.timestamp() < endTime)
                    .toArray(Event[]::new);

        }

        return new EventIteratorArray(eventsMatch);
    }
}
