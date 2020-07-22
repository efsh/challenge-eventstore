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
 *
 *     In a multiple thread scenario ConcurrentHashMap it is similarly effective to operating HashMap in a single thread.
 *
 *     With ConcurrentHasMap we get a constant time complexity O(1) for search, insert and deletion operations in the
 *     average case and O(n) in the worst case (when collisions occur). So, it's important to implement a proper
 *     .hashCode() method to avoid collisions. The space complexity is O(n).
 *
 * </p>

 */
public class EventStoreInMemory implements EventStore{

    //
    final private Map<String, List<Event>> eventMap = new ConcurrentHashMap<>();

    @Override
    public void insert(final Event event) {

        // Creates a List with the new event
        List<Event> eventList = new ArrayList<>(1);
        eventList.add(event);

        // As we can see in the Javadoc, the entire merge() method invocation is performed atomically,
        // so we can prevent the multithread side-effects

        // Two flows are possible:
        // 1 - If event type doesn't exist, create new one with 'eventList' value
        // 2 - If event type exists, append the new value
        // At the end, we return the entire new list to eventMap list
        this.eventMap.merge(event.type(), eventList, (previousEvents, newEvent) -> {
            previousEvents.addAll(newEvent);
            return previousEvents;
        });

    }

    @Override
    public void removeAll(final String type) {

        // Removes the type from the map, performed atomically
        // If (event type present) return null, i.e remove the entire value for this key
        // Else do nothing
        this.eventMap.computeIfPresent(type, (k, v) -> null);

    }

    @Override
    public EventIterator query(final String type, final long startTime, final long endTime) {

        List<Event> eventsMatch = null;

        try {
            final List<Event> allEvents = this.eventMap.get(type);

            // filter only events in the range [startTime, endTime) and return a new list to eventsMatch
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
