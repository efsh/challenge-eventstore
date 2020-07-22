package net.intelie.challenges;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This is an in-memory implementation of EventStore
 * <p>
 *     Uses a ConcurrentHashMap (thread-safe) to hold the Events.
 *     <ul>
 *         <li>The <strong>key</strong> is represented by event type</li>
 *         <li>The <strong>value</strong> is a List that holds all Events of the same type</li>
 *     </ul>
 *     Although we are using ConcurrentHashMap, there is not any support for locking the entire table in a way that
 *     prevents all access, so we must use methods whose invocation is performed atomically
 * </p>

 */
public class EventStoreInMemory implements EventStore{

    //
    final private Map<String, List<Event>> eventMap = new ConcurrentHashMap<>();

    @Override
    public void insert(final Event event) {

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        // As says in Javadoc, the entire method invocation is performed atomically,
        // so we can prevent the multithread side-effects

        // If event type doesn't exist, create new one with 'eventList' value
        // If event type exists, append the value with 'eventList'
        this.eventMap.merge(event.type(), eventList, (previousEvents, newEvent) -> {
            previousEvents.addAll(newEvent);
            return previousEvents;
        });

    }

    @Override
    public void removeAll(final String type) {

        // Removes the type from the map, performed atomically
        this.eventMap.computeIfPresent(type, (k, v) -> null);

    }

    @Override
    public EventIterator query(final String type, final long startTime, final long endTime) {

        List<Event> eventsMatch = new LinkedList<>();

        try {
            final List<Event> allEvents = this.eventMap.get(type);

            // filter only events in the range [startTime, endTime)
            eventsMatch = allEvents
                    .stream()
                    .filter(e -> e.timestamp() >= startTime && e.timestamp() < endTime)
                    .collect(Collectors.toList());

        } catch (final NullPointerException e) {
            // Prevents if there isn't the type
        }


        return new EventIteratorList(eventsMatch);
    }
}
